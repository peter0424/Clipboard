package com.example.idea.clipboard.Adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.idea.clipboard.Objects.AppList;
import com.example.idea.clipboard.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Idea on 13/2/2017.
 */

public class AppListRecyclerAdapter extends RecyclerView.Adapter<AppListRecyclerAdapter.ViewHolder>
{
	private List<AppList> appList;
	private List<AppList> appListCopy = new ArrayList<>();
	private Context mContext;
	private boolean selectAll = false;
	private boolean initial = true;

	public AppListRecyclerAdapter(Context context, List<AppList> records)
	{
		appList = records;
		mContext = context;
		appListCopy.addAll(appList);
	}

	@Override
	public AppListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);

		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(AppListRecyclerAdapter.ViewHolder viewHolder, int position)
	{
		AppList appList = this.appList.get(position);
		final PackageManager pm = mContext.getPackageManager();

		viewHolder.checkBox.setClickable(false);
		viewHolder.iv_icon.setImageDrawable(appList.getAppIcon());
		viewHolder.lbl_name.setText(appList.getAppLabel());

		if(initial)
			viewHolder.checkBox.setChecked(appList.isSelected());
		else
		{
			viewHolder.checkBox.setChecked(selectAll);
		}
	}

	public void selectAll(boolean isChecked)
	{
		initial = false;
		selectAll = isChecked;
		//mOnItemClickListener.onItemClick(null, -1);
		if (mOnSelectAllClickListener != null)
		{
			mOnSelectAllClickListener.onSelectAllClick(selectAll);
		}
		notifyDataSetChanged();
	}

	public void filter(String text)
	{
		appList.clear();
		if (text.isEmpty())
		{
			appList.addAll(appListCopy);
		} else
		{
			text = text.toLowerCase();
			for (AppList item : appListCopy)
			{
				if (item.getAppLabel().toLowerCase().contains(text))
				{
					appList.add(item);
				}
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount()
	{
		return appList.size();
	}

	private OnItemClickListener mOnItemClickListener;
	private OnSelectAllClickListener mOnSelectAllClickListener;

	public void setOnItemClickListener(OnItemClickListener listener)
	{
		mOnItemClickListener = listener;
	}
	public void setOnSelectAllClickListener(OnSelectAllClickListener listener)
	{
		mOnSelectAllClickListener = listener;
	}

	public interface OnItemClickListener
	{
		void onItemClick(View itemView, int position);
	}

	public interface OnSelectAllClickListener
	{
		void onSelectAllClick(boolean selected);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		@BindView(R.id.checkBox)
		CheckBox checkBox;
		@BindView(R.id.iv_list_icon)
		ImageView iv_icon;
		@BindView(R.id.lbl_list_name)
		TextView lbl_name;

		ViewHolder(View itemView)
		{
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view)
		{
			int position = getAdapterPosition();
			if (mOnItemClickListener != null && position != RecyclerView.NO_POSITION)
			{
				checkBox.setChecked(!checkBox.isChecked());
				mOnItemClickListener.onItemClick(itemView, position);
			}
		}
	}
}
