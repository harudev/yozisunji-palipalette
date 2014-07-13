package com.yozisunji.palipalette;

import java.io.Serializable;
import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.GridLayout;
import android.widget.ImageView;

public class PaliItem implements Serializable
{
	int x, y;
	int width, height;
	int funcNum;
	int itemNum;
	int imageid;
	Bitmap image;
	ImageView imgview;
	GridLayout.LayoutParams gl;
	Context context;
	
	public PaliItem()
	{
	}
	
	public PaliItem(int itemNum, int width, int height, int imageid)
	{
		this.itemNum = itemNum;
		this.width = width;
		this.height = height;
		this.imageid = imageid;
	}
	
	public PaliItem(int f, int i, int x, int y, Context context)
	{
		PaliItem temp = CustomizingMainActivity.GearUIList.get(f).items.get(i);
		this.funcNum = f;
		this.itemNum = i;
		this.x = x;
		this.y = y;
		this.width = temp.width;
		this.height = temp.height;
		this.imageid = temp.imageid;
		this.context = context;
		
		imgview = new ImageView(context);
		gl = new GridLayout.LayoutParams(GridLayout.spec(x,width), GridLayout.spec(y,height));
	}
	
	public void createImageView(Context context)
	{
		this.imgview = new ImageView(context);
		gl = new GridLayout.LayoutParams(GridLayout.spec(x,width), GridLayout.spec(y,height));
		image = BitmapFactory.decodeResource(context.getResources(), imageid);
		imgview.setImageBitmap(image);
	}
	
	public ImageView getImageView(int scaler)
	{
		BitmapFactory.Options option  = new BitmapFactory.Options();
		option.inSampleSize = scaler;
		image = BitmapFactory.decodeResource(context.getResources(), imageid, option);
		imgview.setImageBitmap(image);
		return this.imgview;
	}
	
}