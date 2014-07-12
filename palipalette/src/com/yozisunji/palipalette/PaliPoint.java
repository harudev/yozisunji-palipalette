package com.yozisunji.palipalette;

public class PaliPoint {

	public int x, y;
	public float fx, fy;
	public PaliObject obj;
	
	public PaliPoint()
	{
		this.x = 0;
		this.y = 0;
		this.fx = 0f;
		this.fy = 0f;
	}
	public PaliPoint(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.fx = 0f;
		this.fy = 0f;
	}
	
	public PaliPoint(float x, float y)
	{
		this.fx = x;
		this.fy = y;
		this.x = 0;
		this.y = 0;
	}
}