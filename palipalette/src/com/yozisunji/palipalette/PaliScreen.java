package com.yozisunji.palipalette;

import java.util.ArrayList;

import com.samsung.android.example.helloaccessoryprovider.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.widget.GridLayout;
import android.view.View;
import android.view.View.OnClickListener;

public class PaliScreen extends GridLayout implements OnClickListener{
	PaliItemList itemList;
	Context mContext;
	Bitmap bground;
	public PaliScreen(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		itemList = new PaliItemList();
		this.setRowCount(3);
		this.setColumnCount(3);
		
		this.layout(0, 0, 100, 100);
	}
	public void copy(PaliScreen p)
	{
		itemList = p.itemList;
	}
	
	public void putItem(int func, int item, int posX, int posY)
	{
		this.itemList.items.add(new PaliItem(func, item, posX, posY,mContext));
		//
		//
	}
	
	public PaliScreen getScreen(int scaler)
	{
		this.removeAllViews();
		BitmapFactory.Options option  = new BitmapFactory.Options();
		option.inSampleSize = scaler;
		bground = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.screen_background, option);
		this.setBackground(new BitmapDrawable(mContext.getResources(),bground));
		for(int i = 0; i<itemList.items.size() ; i++)
			this.addView(itemList.items.get(i).getImageView(scaler) , itemList.items.get(i).gl);
		this.invalidate();
		
		return this;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
