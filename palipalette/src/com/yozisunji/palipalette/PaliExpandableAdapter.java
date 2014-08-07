package com.yozisunji.palipalette;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

public class PaliExpandableAdapter extends BaseExpandableListAdapter {
	Context context;
	ArrayList<PaliItemList> list;
	
	View.OnTouchListener touch = null;
	
	
	public PaliExpandableAdapter(Context context, ArrayList<PaliItemList> list, View.OnTouchListener t) {
        this.context = context;
        this.list = (ArrayList<PaliItemList>)list.clone(); 
        touch = t;
    }
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition).items.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition).items.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub			
		View view;
        if(convertView == null) {
            view = getParentGenericView();
        } else {
            view = convertView;
        }
               
        TextView text = (TextView)view.findViewById(R.id.categoryName);
        text.setText(list.get(groupPosition).FuncName);
        
        ExpandableListView eLV = (ExpandableListView)parent;
        eLV.expandGroup(groupPosition);
        return view;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View view;
        if(convertView == null) {
            view = getChildGenericView();
        } else {
            view = convertView;
        }
        
        TextView text1 = (TextView)view.findViewById(R.id.functionNameView);
        TextView text2 = (TextView)view.findViewById(R.id.itemTypeView);
        ImageView imgview = (ImageView)view.findViewById(R.id.itemImageView);
        
        if(list.get(groupPosition).items.get(childPosition).getVisible())
        {
	        text1.setText(list.get(groupPosition).items.get(childPosition).funcName);
	        text2.setText(" - " + list.get(groupPosition).items.get(childPosition).itemTypeName);
			imgview.setImageResource(list.get(groupPosition).items.get(childPosition).imageid);
			
			
			view.setOnTouchListener(touch);
			view.setTag(list.get(groupPosition).items.get(childPosition));
        }
        else
        {
        	text1.setVisibility(View.GONE);
        	text2.setVisibility(View.GONE);
        	imgview.setVisibility(View.GONE);
        }
        return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public View getChildGenericView() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listitemlayout, null);
        return view;
    }
     
    public View getParentGenericView() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listcategorylayout, null);
        return view;
    }

}
