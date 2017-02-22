package com.example.idea.clipboard.Adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.idea.clipboard.Objects.SelectedApp;
import com.example.idea.clipboard.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Idea on 13/2/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
	private List<SelectedApp> mRecord;
	private Context mContext;

	public RecyclerAdapter(Context context, List<SelectedApp> records)
	{
		mRecord = records;
		mContext = context;
	}

	@Override
	public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
		ViewHolder vh = new ViewHolder(v);

		return vh;
	}

	@Override
	public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, int position)
	{
		SelectedApp selectedApp = mRecord.get(position);
		final PackageManager pm = mContext.getPackageManager();

		viewHolder.iv_icon.setImageDrawable(selectedApp.getAppIcon());
		viewHolder.lbl_name.setText(selectedApp.getAppLabel());
	}

	@Override
	public int getItemCount()
	{
		return mRecord.size();
	}

	private OnItemClickListener mListener;

	public void setOnItemClickListener(OnItemClickListener listener)
	{
		mListener = listener;
	}

	public interface OnItemClickListener
	{
		void onItemClick(View itemView, int position);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		@BindView(R.id.iv_icon)
		ImageView iv_icon;
		@BindView(R.id.lbl_name)
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
			if (mListener != null && position != RecyclerView.NO_POSITION)
			{
				mListener.onItemClick(itemView, position);
			}
		}
	}
}
