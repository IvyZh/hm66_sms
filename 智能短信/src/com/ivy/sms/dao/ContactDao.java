package com.ivy.sms.dao;

import java.io.InputStream;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;

public class ContactDao {
	

	public static String getNameByAddress(ContentResolver cr,String address){
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, address);
		Cursor cursor = cr.query(uri, new String []{PhoneLookup.DISPLAY_NAME}, null, null, null);
		if(cursor.moveToFirst()){
			address = cursor.getString(0);
		}
		return address;
	}
	
	public static Bitmap getBitmapByAddress(ContentResolver cr,String address){
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, address);
		Cursor cursor = cr.query(uri, new String []{PhoneLookup._ID}, null, null, null);
		if(cursor.moveToFirst()){
			String _id = cursor.getString(0);
			
			InputStream is = Contacts.openContactPhotoInputStream(cr, Uri.withAppendedPath(Contacts.CONTENT_URI, _id));
			
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			return bitmap;
		}
		return null;
	}

}
