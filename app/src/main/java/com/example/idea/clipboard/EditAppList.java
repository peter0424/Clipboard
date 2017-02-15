package com.example.idea.clipboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditAppList extends AppCompatActivity implements AsyncTaskCompleteListener<List<AppList>>
{
	@BindView(R.id.rvAllAppList)
	RecyclerView recyclerView;

	AppListRecyclerAdapter adapter;
	List<AppList> packages;
	List<AppList> packagesCopy = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_app_list);
		ButterKnife.bind(this);

		new BackgroundLoadApp(this, this).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_edit_app, menu);
		MenuItem item = menu.findItem(R.id.menuSearch);
		SearchView searchView = (SearchView) item.getActionView();
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
		{
			@Override
			public boolean onQueryTextSubmit(String query)
			{
				filter(query);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText)
			{
				filter(newText);
				return true;
			}
		});
		return true;
	}

	public void filter(String text)
	{
		packages.clear();
		if (text.isEmpty())
		{
			packages.addAll(packagesCopy);
		}
		else
		{
			text = text.toLowerCase();
			for (AppList item : packagesCopy)
			{
				if (item.getAppLabel().toLowerCase().contains(text))
				{
					packages.add(item);
				}
			}
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.save:
			{
				List<String> selectedPackages = new ArrayList<>();
				for (int i = 0; i < packagesCopy.size(); ++i)
				{
					if (packagesCopy.get(i).isSelected())
					{
						selectedPackages.add(packagesCopy.get(i).getPackageName());
					}
				}

				SharedPreferences appList = getSharedPreferences("myPref", 0);
				SharedPreferences.Editor prefEditor = appList.edit();
				Set<String> set = new HashSet<>();
				set.addAll(selectedPackages);
				prefEditor.putStringSet("app", set);
				prefEditor.apply();
				Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
			default:
				break;
		}

		return true;
	}

	public void onTaskComplete(List<AppList> result)
	{
		packages = result;
		packagesCopy.addAll(packages);
		adapter = (AppListRecyclerAdapter)recyclerView.getAdapter();
		adapter.setOnItemClickListener(new AppListRecyclerAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick(View itemView, int position)
			{
				AppList appsList = packages.get(position);
				appsList.setSelected(!appsList.isSelected());
				packages.get(position).setSelected(appsList.isSelected());
				packagesCopy.get(position).setSelected(appsList.isSelected());
			}
		});
	}
}
