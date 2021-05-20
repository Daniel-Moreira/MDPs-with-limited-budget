package Grafo;

import java.util.List;
import java.util.ArrayList;

public class AugmentedNode extends Node
{
	private int threshold;
	private List<AugmentedNode> nexts;
	private List<AugmentedNode> previous;
	
	public AugmentedNode(String name, int threshold, boolean terminal)
	{
		super(name, terminal);
		
		this.threshold = threshold;
		previous = new ArrayList<AugmentedNode>();
		nexts = new ArrayList<AugmentedNode>();
	}
	
	public void addNext(AugmentedNode n)
	{
		nexts.add(n);
	}
	
	public List<AugmentedNode> getNexts()
	{
		return nexts;
	}
	
	public void addPrevious(AugmentedNode n)
	{
		previous.add(n);
	}
	
	public List<AugmentedNode> getPrevious()
	{
		return previous;
	}
	
	public int getThreshold()
	{
		return threshold;
	}
	
	public void print()
	{
		if(bestAction != null) System.out.println(name + " (Threhold: " + threshold + ") Prob: " + p + " action: " + bestAction.getName());
		else System.out.println(name + " (Threhold: " + threshold + ") Prob: " + p);
	}
	
	public int hashCode()
	{
		return (name+(int)threshold).hashCode();
	}
}
