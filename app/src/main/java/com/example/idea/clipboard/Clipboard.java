package com.example.idea.clipboard;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.idea.clipboard.Activities.ShareTo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Clipboard extends Service
{
	private ClipboardManager mCM;
	int mStartMode;
	ClipboardManager.OnPrimaryClipChangedListener mPrimaryClipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener()
	{
		public void onPrimaryClipChanged()
		{
			//String newClip = mCM.getPrimaryClip().toString();
			SharedPreferences shared = getSharedPreferences("myPref", 0);
			List<String> selectedAppList = new ArrayList<>(shared.getStringSet("app", new HashSet<String>()));
			Intent shareTo = new Intent(Clipboard.this, ShareTo.class);
			shareTo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			shareTo.putExtra("selectedAppList", (ArrayList<String>) selectedAppList);
			startActivity(shareTo);
		}
	};
	boolean hasListener = false;

	@Override
	public void onCreate()
	{
		super.onCreate();
		mCM = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		regPrimaryClipChange();
		//Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
		Log.d("Service", "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
		Log.d("Service", "Start");
		//return Service.START_NOT_STICKY;
		return mStartMode;
	}

	@Override
	public void onDestroy()
	{
		unRegPrimaryClipChange();
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private void regPrimaryClipChange()
	{
		if (!hasListener)
		{
			mCM.addPrimaryClipChangedListener(mPrimaryClipChangedListener);
			hasListener = true;
		}
	}

	private void unRegPrimaryClipChange()
	{
		if (hasListener)
		{
			mCM.removePrimaryClipChangedListener(mPrimaryClipChangedListener);
			hasListener = false;
		}
	}
}
