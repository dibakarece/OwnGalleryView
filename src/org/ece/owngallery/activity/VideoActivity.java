package org.ece.owngallery.activity;

import java.util.ArrayList;

import org.ece.owngallery.ApplicationOwnGallery;
import org.ece.owngallery.R;
import org.ece.owngallery.adapter.BaseFragmentAdapter;
import org.ece.owngallery.component.PhoneMediaVideoController;
import org.ece.owngallery.component.PhoneMediaVideoController.VideoDetails;
import org.ece.owngallery.component.PhoneMediaVideoController.loadAllVideoMediaInterface;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class VideoActivity extends ActionBarActivity implements loadAllVideoMediaInterface{

	private Toolbar toolbar;
	private Context mContext;
	private GridView mView;
	private ListAdapter listAdapter;
	private int itemWidth = 100;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		 mContext=VideoActivity.this;
		 initializeActionBar();
		 initializeView();
		 loadData();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0,0);
		finish();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void initializeActionBar() {
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		toolbar.setTitle("Video");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	private void initializeView(){
		mView=(GridView)findViewById(R.id.grid_view);
     
		mView.setAdapter(listAdapter = new ListAdapter(VideoActivity.this));

        int position = mView.getFirstVisiblePosition();
        int columnsCount = 3;
        mView.setNumColumns(columnsCount);
        itemWidth = (ApplicationOwnGallery.displaySize.x - ((columnsCount + 1) * ApplicationOwnGallery.dp(4))) / columnsCount;
        mView.setColumnWidth(itemWidth);

        listAdapter.notifyDataSetChanged();
        mView.setSelection(position);
        mView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            	startActivity(new Intent(VideoActivity.this,VideoActivity.class)); 
            }
        });
        
        loadData();
	}
	
	private void loadData() {
		PhoneMediaVideoController mPhoneMediaVideoController = new PhoneMediaVideoController();
		mPhoneMediaVideoController.setLoadallvideomediainterface(this);
		mPhoneMediaVideoController.loadAllVideoMedia(mContext);
	}

	@Override
	public void loadVideo(ArrayList<VideoDetails> arrVideoDetails) {
		arrayVideoDetails=arrVideoDetails;
		if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
	}
	
	
	private ArrayList<VideoDetails> arrayVideoDetails = null;
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
			return arrayVideoDetails != null ? arrayVideoDetails.size() : 0;
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
				LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = li.inflate(R.layout.photo_picker_album_layout,viewGroup, false);
			}
			ViewGroup.LayoutParams params = view.getLayoutParams();
			params.width = itemWidth;
			params.height = itemWidth;
			view.setLayoutParams(params);

			VideoDetails mVideoDetails = arrayVideoDetails.get(i); 
			final ImageView imageView = (ImageView) view.findViewById(R.id.media_photo_image);
			final String videoPath=mVideoDetails.path;
			
			if (mVideoDetails.curThumb != null) {
				imageView.setImageBitmap(mVideoDetails.curThumb);
//				imageLoader.displayImage("file://"+mVideoDetails.path, imageView, options,new ImageLoadingListener() {
//					
//					@Override
//					public void onLoadingStarted(String imageUri, View view) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onLoadingFailed(String imageUri, View view,
//							FailReason failReason) {
//						Log.e("TAG", failReason.toString());
//					}
//					
//					@Override
//					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//						
//					}
//					
//					@Override
//					public void onLoadingCancelled(String imageUri, View view) {
//						
//					}
//				});
				
			} else {
				imageView.setImageResource(R.drawable.nophotos);
			}
			TextView textView = (TextView) view.findViewById(R.id.album_name);
			textView.setText(mVideoDetails.displayname);
			textView = (TextView) view.findViewById(R.id.album_count);

			
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						if (videoPath == null) {
							return;
						}
						Intent tostart = new Intent(Intent.ACTION_VIEW);
						tostart.setDataAndType(Uri.parse(videoPath), "video/*");
						startActivity(tostart);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			return view;
		}


	}
}
