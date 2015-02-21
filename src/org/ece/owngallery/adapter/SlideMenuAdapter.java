package org.ece.owngallery.adapter;

import org.ece.owngallery.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SlideMenuAdapter extends BaseAdapter{

	private Context context;
	private String[] titles_;
	private LayoutInflater inflater;
	
	public SlideMenuAdapter(Context mContext,String[] titles){
		this.context=mContext; 
		this.titles_=titles;
		this.inflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return titles_.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder mViewHolder;
		if (convertView==null) {
			mViewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.inflate_slidingrow, null);
			convertView.setTag(mViewHolder); 
		}else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (slidemenuadapterinterface!=null) {
					slidemenuadapterinterface.slideRowClickEvent(position);
				}
			}
		});
		return convertView;
	}
	
	private class ViewHolder{
		public TextView txtName;
		public ImageView imgLogo;
	}

	public SlideMenuAdapterInterface slidemenuadapterinterface;

	public SlideMenuAdapterInterface getSlidemenuadapterinterface() {
		return slidemenuadapterinterface;
	}

	public void setSlidemenuadapterinterface(
			SlideMenuAdapterInterface slidemenuadapterinterface) {
		this.slidemenuadapterinterface = slidemenuadapterinterface;
	}

	public interface SlideMenuAdapterInterface {
		void slideRowClickEvent(int postion);
	}
	
}
