package com.yozisunji.palipalette;

import java.util.ArrayList;

public class PaliHistoryStack extends ArrayList<ArrayList<PaliLayer>> {
	private int index = 0;
	private boolean UndoCommited = false;

	public PaliHistoryStack() {
		super();
	}

	public void Do(ArrayList<PaliLayer> obj) {
		if (UndoCommited) {
			for (int i = index; i < this.size() - 1; i++)
				this.remove(i);
		}
		if (index >= 10) {
			this.remove(0);
			index--;
		}

		ArrayList<PaliLayer> temp = new ArrayList<PaliLayer>(obj.size());
		for (PaliLayer layer : obj) {
			temp.add(new PaliLayer(layer));
		}
		this.add(index, temp);
		index++;
	}

	public ArrayList<PaliLayer> UnDo() {
		if (index > 0) {
			index--;
			UndoCommited = true;
			return this.get(index);
		}
		return null;
	}

	public ArrayList<PaliLayer> ReDo() {
		if (index < this.size() - 1) {
			index++;
			if (index == this.size() - 1)
				UndoCommited = false;
			return this.get(index);
		}
		return null;
	}

	public boolean isUnDoable() {
		if (index > 0)
			return true;
		else
			return false;
	}

	public boolean isReDoable() {
		if (index < this.size() - 1)
			return true;
		else
			return false;
	}
}