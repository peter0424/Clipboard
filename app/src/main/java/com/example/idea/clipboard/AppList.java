package com.example.idea.clipboard;

import android.graphics.drawable.Drawable;

/**
 * Created by Idea on 14/2/2017.
 */

public class AppList extends SelectedApp
{
	private boolean selected;

	public AppList(String packageName, String appLabel, Drawable appIcon, boolean selected)
	{
		super(packageName, appLabel, appIcon);
		this.selected = selected;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
}
