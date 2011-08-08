package com.rafaelkhan.android.smsnotifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();

		if(bundle != null) {	
			Object[] pdus = (Object[]) bundle.get("pdus");
			SmsMessage[] msgs = new SmsMessage[pdus.length];
			
			for(int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				String sender = msgs[i].getOriginatingAddress();
				String message = msgs[i].getMessageBody();
				
				this.startNewActivity(sender,message,context);
			}
		}
	}
	
	public void startNewActivity(String sender, String message, Context context) { //brings up dialog
		Intent in = new Intent(context, SMSReceivedActivity.class);
		in.putExtra("sender", sender);
		in.putExtra("message", message);
	}
}