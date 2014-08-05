package com.yozisunji.palipalette;

import java.util.ArrayList;

public class PaliLayer {
	
	ArrayList<PaliObject> objs;
	boolean visibility;	

	public PaliLayer()
	{
		objs = new ArrayList<PaliObject>();
		visibility = true;
	}
	public PaliLayer(PaliLayer origin)
	{
		objs = new ArrayList<PaliObject>(origin.objs.size());
		for(PaliObject obj : origin.objs)
		{
			objs.add(obj.copy());
		}
		visibility = origin.visibility;
	}
	public boolean getVisible()
	{
		return this.visibility;
	}
	
	public void setVisible(boolean b)
	{
		if(b)
			this.visibility = true;
		else
			this.visibility = false;
		
	}
	
}
