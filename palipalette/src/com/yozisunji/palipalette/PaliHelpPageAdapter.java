package com.yozisunji.palipalette;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class PaliHelpPageAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private String[][] mContents;
	private String[] mTitles;

	public PaliHelpPageAdapter(Context context, String[] titles,
			String[][] contents) {
		super();
		if (titles.length != contents.length) {
			throw new IllegalArgumentException(
					"Titles and Contents must be the same size.");
		}

		mContext = context;
		mContents = contents;
		mTitles = titles;
	
	}

	@Override
	public String getChild(int groupPosition, int childPosition) {
		return mContents[groupPosition][childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		TextView row = (TextView) convertView;
		if (row == null) {
			row = new TextView(mContext);
		}
		row.setText(mContents[groupPosition][childPosition]);
		row.setBackgroundColor(Color.WHITE);
		row.setTextColor(Color.parseColor("#16BDE9"));
		row.setTextSize(20);
		row.setGravity(Gravity.CENTER);
		return row;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mContents[groupPosition].length;
	}

	@Override
	public String[] getGroup(int groupPosition) {
		return mContents[groupPosition];
	}

	@Override
	public int getGroupCount() {
		return mContents.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView row = (TextView) convertView;
		if (row == null) {
			row = new TextView(mContext);
		}
		row.setTypeface(Typeface.DEFAULT_BOLD);
		row.setText(mTitles[groupPosition]);
		row.setBackgroundColor(Color.parseColor("#16BDE9"));
		row.setTextColor(Color.WHITE);
		row.setTextSize(25);
		row.setGravity(Gravity.CENTER);
		return row;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		//Log.i("debug","g: "+groupPosition+" c: "+childPosition);
		return true;
	}

}
