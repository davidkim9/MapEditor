package stormgate.action;

import java.util.Stack;

public class ActionHistory {
	private int undoHistory;
	private Stack<Action> history;
	private int maxUndoHistory = 1000;
	
	private boolean changed = false;
	
	public ActionHistory(){
		history = new Stack<Action>();
	}
	
	public void performAction( Action action ){
		action.perform();

		changed = true;
		//Remove the difference
		int Diff = history.size() - undoHistory;
		
		if ( Diff > 0 )
		{
			for( int i = 0; i < Diff; i++ )
			{
				history.remove( history.size() - 1);
			}
		}
		
		history.add( action );
		
		if ( undoHistory > maxUndoHistory )
		{
			//Cull the first one
			history.remove(0);
		}else{
			undoHistory++;
		}
	}
	
	public boolean undoAction()
	{
		changed = true;
		if ( undoHistory > 0 )
		{
			undoHistory--;
			history.get(undoHistory).undo();
			return true;
		}
		return false;
	}
	
	/**
	 * Go forward a step in history.
	 */
	public boolean redoAction()
	{
		changed = true;
		if ( undoHistory < history.size() )
		{
			history.get(undoHistory).perform();
			undoHistory++;
			return true;
		}
		return false;
	}
	
	public boolean isChanged()
	{
		return changed;
	}

	public void clearChanged()
	{
		changed = false;
	}
	
}
