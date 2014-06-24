package com.yozisunji.palipalette;

import java.util.ArrayList;

public class PaliLayer {
	
	ArrayList<PaliObject> objs;
	int visibility;					// From 1 to 100, percent

	public PaliLayer()
	{
		objs = new ArrayList<PaliObject>();
		visibility = 100;
	}
}
