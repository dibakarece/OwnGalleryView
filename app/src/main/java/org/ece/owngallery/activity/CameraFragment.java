package org.ece.owngallery.activity;

import java.util.ArrayList;

import org.ece.owngallery.ApplicationOwnGallery;
import org.ece.owngallery.R;
import org.ece.owngallery.adapter.BaseFragmentAdapter;
import org.ece.owngallery.component.PhoneMediaControl;
import org.ece.owngallery.component.PhoneMediaControl.PhotoEntry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class CameraFragment extends Fragment {

	public static final String PACKAGE = "org.ece.owngallery";
    private TextView emptyView;
	private GridView mView;
	private Context mContext;
	
	
	public static ArrayList<PhotoEntry> photos = new ArrayList<PhotoEntry>();
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
		albumsSorted=GalleryFragment.albumsSorted;
		photos=GalleryFragment.albumsSorted.get(0).photos;
		
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
            	Intent mIntent=new Intent(mContext,PhotoPreviewActivity.class);
            	Bundle mBundle=new Bundle();
            	mBundle.putInt("Key_FolderID", 0);
            	mBundle.putInt("Key_ID", position);
            	mIntent.putExtras(mBundle);
            	startActivity(mIntent);
            	
            }
        });
        
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
	}
	
	private class ListAdapter extends BaseFragmentAdapter {
		private Context mContext;
		private LayoutInflater layoutInflater;
		private DisplayImageOptions options;
		private ImageLoader imageLoader = ImageLoader.getInstance();

		public ListAdapter(Context context) {
			this.mContext = context;
			this.layoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
			return photos != null ? photos.size() : 0;
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
			viewHolder mHolder;
			if (view == null) {
				mHolder = new viewHolder();
				view = layoutInflater.inflate(R.layout.album_image, viewGroup,false);
				mHolder.imageView = (ImageView) view.findViewById(R.id.album_image);
				ViewGroup.LayoutParams params = view.getLayoutParams();
				params.width = itemWidth;
				params.height = itemWidth;
				view.setLayoutParams(params);
				mHolder.imageView.setTag(i);
				
				view.setTag(mHolder);
			} else {
				mHolder = (viewHolder) view.getTag();
			}
			PhotoEntry mPhotoEntry = photos.get(i);
			String path = mPhotoEntry.path;
			if (path != null && !path.equals("")) {
				ImageLoader.getInstance().displayImage("file://" + path, mHolder.imageView);
			}

			return view;
		}

		@Override
		public int getItemViewType(int i) {
			return 0;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public boolean isEmpty() {
			return albumsSorted == null || albumsSorted.isEmpty();
		}

		class viewHolder {
			public ImageView imageView;
		}

	}
}
