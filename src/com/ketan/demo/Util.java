/**
 * Copyright (c) IndiaNIC InfoTech Ltd.
 * Created By : Nomesh Gaur
 * Created Date : 02 Feb 2011
 * Modified By : 
 * Modified Date : 
 */
package com.ketan.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

/*
 * This class contents all general purpose methods.
 */
public class Util {

	public static int mYear;
	public static int mMonth;
	public static int mDay;
	public static String photoPath;
	public static String framePath = Environment.getExternalStorageDirectory()+"/frame.png";
	public static String message;
	public static String name;
	public static String address;
	public static String city;
	public static String postal;
	public static String country;
	public static String sendDate;
	public static Matrix photoMatrix ;
	public static Bitmap bitmapFrame;
	public static int photoX;
	public static int photoY;
	public static String IMEI;
	/**
	 * To check network connection is availability.
	 * 
	 * @param Context Context of the current activity.
	 * @return boolean
	 */
	public static boolean checkConnection(Context context) {

		final ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else
			return false;
	}
	
	
	/**
	 * Use to show AlertDialogBox with specific messsage.
	 * 
	 * @param Context Context of the current activity.
	 * @param String  title to display in the alert dialog.
	 * @param String  message to display in the alert dialog. 
	 */
	public static void alertDialogBox(Context context,String title,String msg){
		new AlertDialog.Builder(context)
		.setTitle(title)
		.setMessage(msg)
		.setNeutralButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						dialog.dismiss();
						
						
					}
				}).setCancelable(true).create().show();
	}

}
