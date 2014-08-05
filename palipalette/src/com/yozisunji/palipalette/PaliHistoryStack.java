package com.yozisunji.palipalette;

import java.util.ArrayList;

public class PaliHistoryStack extends ArrayList<ArrayList<PaliLayer>> {
	private int index = -1;
	private boolean UndoCommited = false;
	
	public PaliHistoryStack()
	{
		super();
	}
	public void Do(ArrayList<PaliLayer> obj)
	{
		index++;
		if(UndoCommited)
		{
			for(int i=index;i<this.size()-1;i++)
				this.remove(i);
		}
		if(index>=10)
		{
			this.remove(0);
			index--;
		}
		this.add(index, (ArrayList<PaliLayer>)obj.clone());
	}
	public ArrayList<PaliLayer> UnDo()
	{
		if(index>0)
		{
			index--;
			UndoCommited = true;
			return this.get(index);
		}
		return null;
	}
	public ArrayList<PaliLayer> ReDo()
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
