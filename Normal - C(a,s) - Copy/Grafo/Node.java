package Grafo;

public class Node
{
	protected String name;
	protected boolean terminal;
	protected double p;
	protected Acao bestAction;
	
	public Node(String name)
	{
		p = 0;
		this.name = name;
		terminal = false;
		bestAction = null;
	}
	
	public Node(String name, boolean terminal)
	{
		p = 0;
		if(terminal) p = 1;
		this.name = name;
		this.terminal = terminal;
		bestAction = null;
	}
	
	public String getName()
	{
		return name;
	}
	
	public AugmentedNode extendNode(int threshold)
	{
		return new AugmentedNode(name, threshold, terminal);
	}
		
	public void setGoal()
	{
		terminal = true;
		p = 1;
	}
	
	public boolean isGoal()
	{
		return terminal;
	}
		
	public void setP(double p)
	{
		this.p = p;
	}
	
	public double getP()
	{
		return p;
	}
	
	public void setBestAction(Acao a)
	{
		bestAction = a;
	}
	
	public Acao getBestAction()
	{
		return bestAction;
	}
	
	public int hashCode()
	{
		return name.hashCode();
	}
	
	public void print()
	{
		if(bestAction != null) System.out.println(name + " Prob: " + p + " action: " + bestAction.getName());
		else System.out.println(name + " Prob: " + p);
	}
}
