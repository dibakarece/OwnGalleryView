package org.ece.owngallery.activity;

import java.util.ArrayList;

import org.ece.owngallery.ApplicationOwnGallery;
import org.ece.owngallery.R;
import org.ece.owngallery.adapter.BaseFragmentAdapter;
import org.ece.owngallery.component.PhoneMediaControl;
import org.ece.owngallery.component.PhoneMediaControl.AlbumEntry;
import org.ece.owngallery.component.PhoneMediaControl.loadAlbumPhoto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class GalleryFragment extends Fragment {

	public static final String PACKAGE = "org.ece.owngallery";
    private TextView emptyView;
	private GridView mView;
	private Context mContext;
	
	
	public static ArrayList<PhoneMediaControl.AlbumEntry> albumsSorted = null;
	private Integer cameraAlbumId = null;
	private PhoneMediaControl.AlbumEntry selectedAlbum = null;
	private int itemWidth = 100;
	private ListAdapter listAdapter;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		/** Inflating the layout for this fragment **/
		mContext = this.getActivity();
		View v = inflater.inflate(R.layout.fragment_gallery, null);
		initializeView(v);
		return v;
	}
	
	private void initializeView(View v){ 
		mView=(GridView)v.findViewById(R.id.grid_view);
        emptyView = (TextView)v.findViewById(R.id.searchEmptyView);
        emptyView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        emptyView.setText("NoPhotos");
		mView.setAdapter(listAdapter = new ListAdapter(mContext));

        int position = mView.getFirstVisiblePosition();
        int columnsCount = 2;
        mView.setNumColumns(columnsCount);
        itemWidth = (ApplicationOwnGallery.displaySize.x - ((columnsCount + 1) * ApplicationOwnGallery.dp(4))) / columnsCount;
        mView.setColumnWidth(itemWidth);

        listAdapter.notifyDataSetChanged();
        mView.setSelection(position);
        mView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            	Intent mIntent=new Intent(mContext,AlbumActivity.class);
            	Bundle mBundle=new Bundle();
            	mBundle.putString("Key_ID", position+"");
            	mBundle.putString("Key_Name", albumsSorted.get(position).bucketName+"");
            	mIntent.putExtras(mBundle);
            	mContext.startActivity(mIntent);
            }
        });
        
		LoadAllAlbum();
	}
	
	
	private void LoadAllAlbum(){
		PhoneMediaControl mediaControl=new PhoneMediaControl();
		mediaControl.setLoadalbumphoto(new loadAlbumPhoto() {
			
			@Override
			public void loadPhoto(ArrayList<AlbumEntry> albumsSorted_) {
				albumsSorted =new ArrayList<PhoneMediaControl.AlbumEntry>(albumsSorted_);
				if (mView != null && mView.getEmptyView() == null) {
					mView.setEmptyView(null);
		        }
		        if (listAdapter != null) {
		            listAdapter.notifyDataSetChanged();
		        }
			}
		});
		mediaControl.loadGalleryPhotosAlbums(mContext,0);
	}
	
	

	private class ListAdapter extends BaseFragmentAdapter {
		private Context mContext;
		private DisplayImageOptions options;
		private ImageLoader imageLoader = ImageLoader.getInstance();

		public ListAdapter(Context context) {
			mContext = context;
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.nophotos)
					.showImageForEmptyUri(R.drawable.nophotos)
					.showImageOnFail(R.drawable.nophotos).cacheInMemory(true)
					.cacheOnDisc(true).considerExifParams(true).build();
			imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		}

		@Override
		public boolean areAllItemsEnabled() {
			return true;
		}

		@Override
		public boolean isEnabled(int i) {
			return true;
		}

		@Override
		public int getCount() {
			if (selectedAlbum != null) {
				return selectedAlbum.photos.size();
			}
			return albumsSorted != null ? albumsSorted.size() : 0;
		}

		@Override
		public Object getItem(int i) {
			return null;
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			if (view == null) {
				LayoutInflater li = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = li.inflate(R.layout.photo_picker_album_layout,
						viewGroup, false);
			}
			ViewGroup.LayoutParams params = view.getLayoutParams();
			params.width = itemWidth;
			params.height = itemWidth;
			view.setLayoutParams(params);

			PhoneMediaControl.AlbumEntry albumEntry = albumsSorted.get(i);
			final ImageView imageView = (ImageView) view
					.findViewById(R.id.media_photo_image);
			if (albumEntry.coverPhoto != null
					&& albumEntry.coverPhoto.path != null) {
				imageLoader.displayImage(
						"file://" + albumEntry.coverPhoto.path, imageView,
						options);
			} else {
				imageView.setImageResource(R.drawable.nophotos);
			}
			TextView textView = (TextView) view.findViewById(R.id.album_name);
			textView.setText(albumEntry.bucketName);
			if (cameraAlbumId != null && albumEntry.bucketId == cameraAlbumId) {

			} else {

			}
			textView = (TextView) view.findViewById(R.id.album_count);
			textView.setText("" + albumEntry.photos.size());

			return view;
		}

		@Override
		public int getItemViewType(int i) {
			if (selectedAlbum != null) {
				return 1;
			}
			return 0;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public boolean isEmpty() {
			if (selectedAlbum != null) {
				return selectedAlbum.photos.isEmpty();
			}
			return albumsSorted == null || albumsSorted.isEmpty();
		}
	}
}
