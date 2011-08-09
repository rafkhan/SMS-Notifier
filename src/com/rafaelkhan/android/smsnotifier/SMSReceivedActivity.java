package com.rafaelkhan.android.smsnotifier;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.widget.ImageView;
import android.widget.TextView;

public class SMSReceivedActivity extends Activity {

	private String sender; // senders phone number
	private String message; // SMS text body

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
			return true;
		} else {
			return false;
		}
	}

	public void setSMSData() {
		TextView contactNameView = (TextView) findViewById(R.id.contact_name_view);
		TextView phoneNumberView = (TextView) findViewById(R.id.phone_number_view);
		TextView messageBodyView = (TextView) findViewById(R.id.message_body_view);
		@SuppressWarnings("unused")
		ImageView contactImageView = (ImageView) findViewById(R.id.imageView1);

		contactNameView.setText(getContactName(this.sender));
		phoneNumberView.setText(this.sender);
		messageBodyView.setText(this.message);
	}

	private String getContactName(String number) {	
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number)); 
		Cursor c = getApplicationContext().getContentResolver().query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);  
		if(c.moveToNext()){   
			String name = c.getString(c.getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME));
			return name;
		} else {
			return "Unknown";
		}
	}
}