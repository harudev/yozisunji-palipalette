package com.yozisunji.palipalette;

import java.util.ArrayList;

public class PaliLayer {
	
	ArrayList<PaliObject> objs;
	boolean visibility;					// From 1 to 100, percent

	public PaliLayer()
	{
		objs = new ArrayList<PaliObject>();
		visibility = true;
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
