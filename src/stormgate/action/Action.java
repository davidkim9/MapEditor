package stormgate.action;

public interface Action
{

	public void undo();

	//Perform is called automatically when it is submited to action history
	public void perform();
}
