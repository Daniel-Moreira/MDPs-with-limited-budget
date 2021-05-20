package Domain;

import java.util.List;
import java.util.ArrayList;

import ADD.ADD;

public class Domain
{
	private List<Acao> actions;
	private ADD initialState;
	private ADD terminalStates;
	
	public Domain()
	{
		actions = new ArrayList<Acao>();
	}
	
	public void addAction(Acao action)
	{
		actions.add(action);
	}
	
	public List<Acao> getActions()
	{
		return actions;
	}
	
	public void setInitialState(ADD tree)
	{
		initialState = tree;
	}
	
	public ADD getInitialState()
	{
		return initialState;
	}
	
	public void setGoalState(ADD tree)
	{
		terminalStates = tree;
	}
	
	public ADD getGoalState()
	{
		return terminalStates;
	}
}