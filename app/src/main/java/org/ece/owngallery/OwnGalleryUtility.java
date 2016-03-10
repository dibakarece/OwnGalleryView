package org.ece.owngallery;


public class OwnGalleryUtility {
	public static void runOnUIThread(Runnable runnable) {
		runOnUIThread(runnable, 0);
	}

	public static void runOnUIThread(Runnable runnable, long delay) {
		if (delay == 0) {
			ApplicationOwnGallery.applicationHandler.post(runnable);
		} else {
			ApplicationOwnGallery.applicationHandler.postDelayed(runnable, delay);
		}
	}
}
