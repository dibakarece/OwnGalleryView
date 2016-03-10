package org.ece.owngallery;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ApplicationOwnGallery extends Application{

	public static Context applicationContext=null;
	public static volatile Handler applicationHandler = null;
	public static Point displaySize = new Point();
	public static float density = 1;

	
	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = getApplicationContext();
		applicationHandler = new Handler(applicationContext.getMainLooper());
		checkDisplaySize();
		density = ApplicationOwnGallery.applicationContext.getResources().getDisplayMetrics().density;
		
		
		DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder() //
				.considerExifParams(true) 
				.resetViewBeforeLoading(true) 
				.showImageOnLoading(R.drawable.nophotos) 
				.showImageOnFail(R.drawable.nophotos) 
				.delayBeforeLoading(0) 
				.build(); //
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.defaultDisplayImageOptions(defaultDisplayImageOptions)
				.memoryCacheExtraOptions(480, 800).threadPoolSize(5).build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	 public static int dp(float value) {
	      return (int)Math.ceil(density * value);
	  }
	
	 public static void checkDisplaySize() {
	        try {
	            WindowManager manager = (WindowManager)ApplicationOwnGallery.applicationContext.getSystemService(Context.WINDOW_SERVICE);
	            if (manager != null) {
	                Display display = manager.getDefaultDisplay();
	                if (display != null) {
	                    if(android.os.Build.VERSION.SDK_INT < 13) {
	                        displaySize.set(display.getWidth(), display.getHeight());
	                    } else {
	                        display.getSize(displaySize);
	                    }
	                }
	            }
	        } catch (Exception e) {
	        }
	}
	 
	 
}
