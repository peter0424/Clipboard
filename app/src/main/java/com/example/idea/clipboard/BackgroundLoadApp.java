package com.example.idea.clipboard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;

import com.example.idea.clipboard.Adapters.AppListRecyclerAdapter;
import com.example.idea.clipboard.Objects.AppList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Idea on 22/11/2016 4:34 PM.
 */

public class BackgroundLoadApp extends AsyncTask<Integer, Integer, List<AppList>>
{
	private Activity mActivity;
	private ProgressBar progressBar;
	private CheckBox chk_selectAll;
	private boolean allSelected = true;
	private AsyncTaskCompleteListener<List<AppList>> mCallback;

	public BackgroundLoadApp(Activity activity, AsyncTaskCompleteListener<List<AppList>> callback)
	{
		mActivity = activity;
		mCallback = callback;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();

		progressBar = (ProgressBar) mActivity.findViewById(R.id.progressBar);
		//progressBar.setVisibility(View.VISIBLE);
		chk_selectAll = (CheckBox) mActivity.findViewById(R.id.chk_selectAll);
	}

	@Override
	protected List<AppList> doInBackground(Integer... params)
	{
		SharedPreferences sharedPreferences = mActivity.getSharedPreferences("myPref", 0);
		List<String> selectedAppList = new ArrayList<>(sharedPreferences.getStringSet("app", new HashSet<String>()));

		PackageManager pm = mActivity.getPackageManager();
		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		List<ApplicationInfo> filteredPackages = new ArrayList<>();
		final List<AppList> appsList = new ArrayList<>();

		for(ApplicationInfo appInfo: packages)      //filter the app, only get those apps which are runnable
		{
			if(pm.getLaunchIntentForPackage(appInfo.packageName) != null)
				filteredPackages.add(appInfo);
		}

		String packName;
		boolean isSelected;
		for (int i = 0; i < filteredPackages.size(); ++i)
		{
			isSelected = false;
			packName = filteredPackages.get(i).packageName;
			for (int j = 0; j < selectedAppList.size(); ++j)
			{
				if (packName.equals(selectedAppList.get(j)))
				{
					isSelected = true;
					break;
				}
			}

			if(allSelected && !isSelected)
			{
				allSelected = false;
			}

			try
			{
				appsList.add(new AppList(packName, (String) pm.getApplicationLabel(pm.getApplicationInfo(packName, 0)), pm.getApplicationIcon(packName), isSelected));
			} catch (PackageManager.NameNotFoundException e)
			{
				e.printStackTrace();
			}
		}

		Collections.sort(appsList, new Comparator<AppList>()
		{
			public int compare(AppList o1, AppList o2)
			{
				return o1.getAppLabel().compareTo(o2.getAppLabel());
			}
		});

		return appsList;
	}

	@Override
	protected void onPostExecute(List<AppList> packagesName)
	{
		setList(packagesName);
		if (progressBar.getVisibility() != View.GONE)
			progressBar.setVisibility(View.GONE);
		if(chk_selectAll.getVisibility() != View.VISIBLE)
			chk_selectAll.setVisibility(View.VISIBLE);
		chk_selectAll.setChecked(allSelected);
		mCallback.onTaskComplete(packagesName);
	}

	private void setList(final List<AppList> packagesName)
	{
		RecyclerView recyclerView = (RecyclerView) mActivity.findViewById(R.id.rvAllAppList);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
		recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));

		AppListRecyclerAdapter adapter = new AppListRecyclerAdapter(mActivity, packagesName);
		recyclerView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	public static boolean isSystemPackage(ApplicationInfo applicationInfo)
	{
		return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
	}

	public interface AsyncTaskCompleteListener<T>
	{
		void onTaskComplete(T result);
	}
}