package org.ece.owngallery.adapter;

import java.util.ArrayList;

import org.ece.owngallery.R;
import org.ece.owngallery.model.SlideData;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SlideMenuAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<SlideData> SlideDataS;
	private LayoutInflater inflater;
	
	public SlideMenuAdapter(Context mContext,ArrayList<SlideData> SlideDatas_){
		this.context=mContext; 
		this.SlideDataS=SlideDatas_;
		this.inflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return SlideDataS.size();
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
			mViewHolder.imgLogo=(ImageView)convertView.findViewById(R.id.inflate_imgLogo);
			mViewHolder.txtName=(TextView)convertView.findViewById(R.id.inflate_textLogo);
			
			convertView.setTag(mViewHolder); 
		}else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		
		
		SlideData mSlideData = SlideDataS.get(position);
		mViewHolder.txtName.setText(mSlideData.getName());
		mViewHolder.txtName.setTextColor((mSlideData.getState()==0)?Color.GRAY:Color.parseColor("#FF5722"));
		mViewHolder.imgLogo.setImageResource(mSlideData.getIcon());
		mViewHolder.imgLogo.setSelected((mSlideData.getState()==0)?false:true);
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (SlideDataS.get(position).state==0) {
					for (SlideData slideData : SlideDataS) {
						slideData.state=0;
					}
					SlideDataS.get(position).state=1;
				}
				if (slidemenuadapterinterface!=null) {
					slidemenuadapterinterface.slideRowClickEvent(position);
				}
				notifyDataSetChanged();
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
