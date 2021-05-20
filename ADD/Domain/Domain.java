package Domain;

import java.util.List;
import java.util.ArrayList;

public class Domain
{
	private List<Acao> actions;
	private String initialState;
	private List<String> terminalStates;
	
	public Domain()
	{
		actions = new ArrayList<Acao>();
		terminalStates = new ArrayList<String>();
	}
	
	public void addAction(Acao action)
	{
		actions.add(action);
	}
	
	public List<Acao> getActions()
	{
		return actions;
	}
	
	public void setInitialState(String var)
	{
		initialState = var;
	}
	
	public String getInitialState()
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