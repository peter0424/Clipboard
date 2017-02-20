package com.example.idea.clipboard;

import android.graphics.drawable.Drawable;

/**
 * Created by Idea on 13/2/2017.
 */

public class SelectedApp
{
	private String packageName;
	private String appLabel;
	private Drawable appIcon;

	public SelectedApp(String packageName, String appLabel, Drawable appIcon)
	{
		this.packageName = packageName;
		this.appLabel = appLabel;
		this.appIcon = appIcon;
	}

	public String getPackageName()
	{
		return packageName;
	}

	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}

	public String getAppLabel()
	{
		return appLabel;
	}

	public void setAppLabel(String appLabel)
	{
		this.appLabel = appLabel;
	}

	public Drawable getAppIcon()
	{
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon)
	{
		this.appIcon = appIcon;
	}
}
