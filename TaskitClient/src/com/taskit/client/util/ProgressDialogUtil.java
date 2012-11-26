/**
 * ProgressDialogUtil.java
 * 2012-11-25 OckhamTheRazor
 */
package com.taskit.client.util;

import android.app.ProgressDialog;
import android.util.Log;

public class ProgressDialogUtil {
	
	private static final String ERROR_TAG = "ProgressDialogUtil.java";
	
	public ProgressDialog setUpProgressDialog(ProgressDialog dialog, String message) {
		
		dialog.setCancelable(false);
        dialog.setMessage(message);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        
        return dialog;
	}
	
	public void dismissProgressDialog(final ProgressDialog dialog, String message) {
		
		dialog.setMessage(message);
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
					dialog.dismiss();
				} catch (InterruptedException e) {
					Log.e(ERROR_TAG, "fail to dismiss progress dialog");
				}
			}
		}).start();
		
	}
	
}
