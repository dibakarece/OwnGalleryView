package org.ece.owngallery.component;

import java.util.ArrayList;
import java.util.HashMap;

import org.ece.owngallery.ApplicationOwnGallery;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class PhoneMediaControl {

	private static Context context;

	public static void loadGalleryPhotosAlbums(Context mContext , final int guid) {
		context=mContext;
		
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<AlbumEntry> albumsSorted = new ArrayList<AlbumEntry>();
                HashMap<Integer, AlbumEntry> albums = new HashMap<Integer, AlbumEntry>();
                AlbumEntry allPhotosAlbum = null;
                String cameraFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + "Camera/";
                Integer cameraAlbumId = null;

                Cursor cursor = null;
                try {
                    cursor = MediaStore.Images.Media.query(ApplicationOwnGallery.applicationContext.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projectionPhotos, "", null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
                    if (cursor != null) {
                        int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                        int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
                        int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                        int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        int dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                        int orientationColumn = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);

                        while (cursor.moveToNext()) {
                            int imageId = cursor.getInt(imageIdColumn);
                            int bucketId = cursor.getInt(bucketIdColumn);
                            String bucketName = cursor.getString(bucketNameColumn);
                            String path = cursor.getString(dataColumn);
                            long dateTaken = cursor.getLong(dateColumn);
                            int orientation = cursor.getInt(orientationColumn);

                            if (path == null || path.length() == 0) {
                                continue;
                            }

                            PhotoEntry photoEntry = new PhotoEntry(bucketId, imageId, dateTaken, path, orientation);

                            if (allPhotosAlbum == null) {
                                allPhotosAlbum = new AlbumEntry(0, "AllPhotos", photoEntry);
                                albumsSorted.add(0, allPhotosAlbum);
                            }
                            if (allPhotosAlbum != null) {
                                allPhotosAlbum.addPhoto(photoEntry);
                            }

                            AlbumEntry albumEntry = albums.get(bucketId);
                            if (albumEntry == null) {
                                albumEntry = new AlbumEntry(bucketId, bucketName, photoEntry);
                                albums.put(bucketId, albumEntry);
                                if (cameraAlbumId == null && cameraFolder != null && path != null && path.startsWith(cameraFolder)) {
                                    albumsSorted.add(0, albumEntry);
                                    cameraAlbumId = bucketId;
                                } else {
                                    albumsSorted.add(albumEntry);
                                }
                            }

                            albumEntry.addPhoto(photoEntry);
                        }
                    }
                } catch (Exception e) {
                	Log.e("tmessages", e.toString());
                } finally {
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Exception e) {
                            Log.e("tmessages", e.toString());
                        }
                    }
                }
                runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                    	if (loadalbumphoto!=null) {
                    		loadalbumphoto.loadPhoto(albumsSorted);
						}
                    }
                });
            }
        }).start();
    }
	
	 public static void runOnUIThread(Runnable runnable) {
	        runOnUIThread(runnable, 0);
	 }
	 
	public static void runOnUIThread(Runnable runnable, long delay) {
		if (delay == 0) {
			ApplicationOwnGallery.applicationHandler.post(runnable);
		} else {
			ApplicationOwnGallery.applicationHandler.postDelayed(runnable,delay);
		}
	}
	 
	
	 private static final String[] projectionPhotos = {
         MediaStore.Images.Media._ID,
         MediaStore.Images.Media.BUCKET_ID,
         MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
         MediaStore.Images.Media.DATA,
         MediaStore.Images.Media.DATE_TAKEN,
         MediaStore.Images.Media.ORIENTATION
	 };
	
	public static class AlbumEntry {
		public int bucketId;
		public String bucketName;
		public PhotoEntry coverPhoto;
		public ArrayList<PhotoEntry> photos = new ArrayList<PhotoEntry>();

		public AlbumEntry(int bucketId, String bucketName, PhotoEntry coverPhoto) {
			this.bucketId = bucketId;
			this.bucketName = bucketName;
			this.coverPhoto = coverPhoto;
		}

		public void addPhoto(PhotoEntry photoEntry) {
			photos.add(photoEntry);
		}
	}

	public static class PhotoEntry {
		public int bucketId;
		public int imageId;
		public long dateTaken;
		public String path;
		public int orientation;

		public PhotoEntry(int bucketId, int imageId, long dateTaken,String path, int orientation) {
			this.bucketId = bucketId;
			this.imageId = imageId;
			this.dateTaken = dateTaken;
			this.path = path;
			this.orientation = orientation;
		}
	}
	
	
	public static loadAlbumPhoto loadalbumphoto; 

	public loadAlbumPhoto getLoadalbumphoto() {
		return loadalbumphoto;
	}

	public void setLoadalbumphoto(loadAlbumPhoto loadalbumphoto) {
		this.loadalbumphoto = loadalbumphoto;
	}

	public interface loadAlbumPhoto {
		public void loadPhoto(ArrayList<AlbumEntry> albumsSorted); 
	}
	
}
