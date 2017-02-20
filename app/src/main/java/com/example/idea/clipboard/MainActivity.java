package com.example.idea.clipboard;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{
	boolean run;
	@BindView(R.id.btn)
	Button btn;
	@BindView(R.id.btn2)
	Button btn2;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		run = isMyServiceRunning(Clipboard.class);
		if (!run) btn.setText("Tap to run");
		else btn.setText("Tap to stop");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.edit_app_list:
			{
				Intent editApp = new Intent(this, EditAppList.class);
				startActivity(editApp);
				break;
			}
			default:
				break;
		}

		return true;
	}

	@OnClick(R.id.btn)
	public void runService()
	{
		run = isMyServiceRunning(Clipboard.class);
		if (!run)
		{
			startService(new Intent(this, Clipboard.class));
			btn.setText("Tap to stop");
		} else
		{
			getBaseContext().stopService(new Intent(this, Clipboard.class));
			btn.setText("Tap to run");
			//android.os.Process.killProcess(android.os.Process.myPid());
			//finish();
		}
	}

	@OnClick(R.id.btn2)
	public void check()
	{
		run = isMyServiceRunning(Clipboard.class);
		if (run) Toast.makeText(this, "Running", Toast.LENGTH_SHORT).show();
		else Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
	}

	private boolean isMyServiceRunning(Class<?> serviceClass)
	{
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
		{
			if (serviceClass.getName().equals(service.service.getClassName()))
			{
				return true;
			}
		}
		return false;
	}
}
