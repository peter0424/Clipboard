package com.example.idea.clipboard.Activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.idea.clipboard.Objects.SelectedApp;
import com.example.idea.clipboard.R;
import com.example.idea.clipboard.Adapters.RecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareTo extends AppCompatActivity
{
	@BindView(R.id.rvAppList)
	RecyclerView rvAppList;
	@BindView(R.id.lbl_noApp)
	TextView lbl_noApp;
	@BindView(R.id.btn_editApp)
	Button btn_editApp;

	private RecyclerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_to);
		ButterKnife.bind(this);

		setTitle("Share to...");
		rvAppList.setHasFixedSize(true);
		rvAppList.setLayoutManager(new LinearLayoutManager(this));
		rvAppList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

		final List<SelectedApp> selectedApps = new ArrayList<>();
		final PackageManager pm = getPackageManager();
		final List<String> selectedAppList = getIntent().getStringArrayListExtra("selectedAppList");
		//List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		//List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_META_DATA);

		String packName;
		for (int i = 0; i < selectedAppList.size(); ++i)
		{
			packName = selectedAppList.get(i);
			try
			{
				selectedApps.add(new SelectedApp(packName, (String) pm.getApplicationLabel(pm.getApplicationInfo(packName, 0)), pm.getApplicationIcon(packName)));
			} catch (PackageManager.NameNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		Collections.sort(selectedApps, new Comparator<SelectedApp>()
		{
			public int compare(SelectedApp o1, SelectedApp o2)
			{
				return o1.getAppLabel().compareTo(o2.getAppLabel());
			}
		});

		if (selectedApps.size() != 0)
		{
			adapter = new RecyclerAdapter(this, selectedApps);
			rvAppList.setAdapter(adapter);

			adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener()
			{
				@Override
				public void onItemClick(View view, int position)
				{
					Intent intent = getPackageManager().getLaunchIntentForPackage(selectedApps.get(position).getPackageName());
					if (intent != null) startActivity(intent);
					ShareTo.this.finish();
				}
			});
			adapter.notifyDataSetChanged();
		} else
		{
			lbl_noApp.setVisibility(View.VISIBLE);
			btn_editApp.setVisibility(View.VISIBLE);
		}
	}

	@OnClick(R.id.btn_editApp)
	public void editApp(View v)
	{
		startActivity(new Intent(ShareTo.this, EditAppList.class));
		this.finish();
	}

	public static boolean isSystemPackage(ApplicationInfo applicationInfo)
	{
		return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
	}
}
