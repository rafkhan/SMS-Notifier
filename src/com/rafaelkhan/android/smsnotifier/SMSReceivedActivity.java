package com.rafaelkhan.android.smsnotifier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SMSReceivedActivity extends Activity {

	private String sender; // senders phone number
	private String name; // senders name
	private String message; // SMS text body
	private long id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receiver_dialog_layout);

		if (this.getIntentExtras()) { // bundle is not null
			this.setSMSData();
		}
	}

	public boolean getIntentExtras() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			this.sender = bundle.getString("sender");
			this.message = bundle.getString("message");
			this.name = getContactName(this.sender);
			return true;
		} else {
			return false;
		}
	}

	public void setSMSData() {
		TextView contactNameView = (TextView) findViewById(R.id.contact_name_view);
		TextView phoneNumberView = (TextView) findViewById(R.id.phone_number_view);
		TextView messageBodyView = (TextView) findViewById(R.id.message_body_view);
		ImageView contactImageView = (ImageView) findViewById(R.id.imageView1);

		contactNameView.setText(this.name);
		phoneNumberView.setText(this.sender);
		messageBodyView.setText(this.message);

		InputStream is = this.openPhoto(this.id);
		if (is != null) {
			Bitmap bm = BitmapFactory.decodeStream(is);
			contactImageView.setImageBitmap(bm);
		}
	}

	private String getContactName(String number) { // this method also sets the
													// iD
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri
				.encode(number));
		Cursor c = getApplicationContext().getContentResolver().query(uri,
				new String[] { PhoneLookup.DISPLAY_NAME, PhoneLookup._ID },
				null, null, null);
		if (c.moveToNext()) {
			String name = c.getString(c
					.getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME));
			this.id = c.getColumnIndexOrThrow(PhoneLookup._ID);
			Log.d("asd", Long.toString(this.id));
			return name;
		} else {
			return "Unknown";
		}
	}

	public InputStream openPhoto(long contactId) {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
				contactId);
		Uri photoUri = Uri.withAppendedPath(contactUri,
				Contacts.Photo.CONTENT_DIRECTORY);
		Cursor cursor = getContentResolver().query(photoUri,
				new String[] { Photo.PHOTO }, null, null, null);
		if (cursor == null) {
			return null;
		}
		try {
			if (cursor.moveToFirst()) {
				byte[] data = cursor.getBlob(0);
				if (data != null) {
					return new ByteArrayInputStream(data);
				}
			}
		} finally {
			cursor.close();
		}
		return null;
	}
	
	public void replyButton(View v) {
		EditText et = (EditText) findViewById(R.id.text_input);
		Button bt = (Button) findViewById(R.id.send_button);
		et.setVisibility(View.VISIBLE);
		bt.setVisibility(View.VISIBLE);
	}
	
	public void dismissButton(View v) {
		moveTaskToBack(true);
	}
}