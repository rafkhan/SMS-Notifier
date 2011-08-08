package com.rafaelkhan.android.smsnotifier;

import android.app.Activity;
import android.os.Bundle;

public class SMSReceivedActivity extends Activity {

	private String sender;
	private String message;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		this.getIntentExtras();
	}
	
	public void getIntentExtras() {
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			this.sender = bundle.getString("sender");
			this.message = bundle.getString("message");
		}
	}
}