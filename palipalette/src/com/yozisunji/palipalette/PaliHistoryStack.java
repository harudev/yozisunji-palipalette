package com.yozisunji.palipalette;

import java.util.ArrayList;

public class PaliHistoryStack extends ArrayList<ArrayList<PaliPoint>> {
	private int index = -1;
	private boolean UndoCommited = false;
	
	public PaliHistoryStack()
	{
		super();
	}
	
	public void Do(ArrayList<PaliPoint> obj)
	{
		index++;
		if(UndoCommited)
		{
			for(int i=index;i<this.size()-1;i++)
				this.remove(i);
		}
		this.add(index, obj);
	}
	
	public ArrayList<PaliPoint> UnDo()
	{
		if(index>0)
		{
			index--;
			UndoCommited = true;
			return this.get(index);
		}
		return null;
	}
	
	public ArrayList<PaliPoint> ReDo()
	{
		if(index<this.size()-1)
		{
			index++;
			if(index == this.size()-1)
				UndoCommited = false;
			return this.get(index);
		}
		return null;
	}

}
