package Domain;

import java.util.List;
import java.util.ArrayList;

import ADD.ADD;

public class Domain
{
	private List<Acao> actions;
	private ADD initialState;
	private List<String> terminalStates;
	private int horizon;
	
	public Domain()
	{
		actions = new ArrayList<Acao>();
		terminalStates = new ArrayList<String>();
	}
	
	public void setHorizonte(int h)
	{
		horizon = h;
	}
	
	public int getHorizon()
	{
		return horizon;
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
	
	public void addTerminalState(String var)
	{
		terminalStates.add(var);
	}
	
	public List<String> getTerminalStates()
	{
		return terminalStates;
	}
}