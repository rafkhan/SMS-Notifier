package com.rafaelkhan.android.smsnotifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("asd","ASD");
		Bundle bundle = intent.getExtras();

		if(bundle != null) {	
			Object[] pdus = (Object[]) bundle.get("pdus");
			SmsMessage[] msgs = new SmsMessage[pdus.length];
			
			for(int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				String sender = msgs[i].getOriginatingAddress();
				String message = msgs[i].getMessageBody();
				
				this.createNewPopup(sender,message,context);
			}
		}
	}
	
	public void createNewPopup(String sender, String message, Context context) { //brings up dialog
		Intent in = new Intent(context, SMSReceivedActivity.class);
		in.putExtra("sender", sender);
		in.putExtra("message", message);
		in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(in);
	}
}