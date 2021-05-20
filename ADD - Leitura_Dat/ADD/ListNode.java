package ADD;

import java.util.List;
import java.util.ArrayList;

public class ListNode extends Node
{
	private ADD[] list;
	private int threshold;
	
	public ListNode(int threshold, ADD[] list)
	{
		childs = new Node[NUMBER_CHILDS];
		this.threshold = threshold;
		this.list = list;
	}
	
	public boolean isTerminal()
	{
		return true;
	}
	
	public Node setCost(Node n2)
	{
		if(!n2.isTerminal()) return null;
		
		NodeTerminal t2 = (NodeTerminal) n2;
		int cost = (int) t2.getValue();
			// With zero cost should put goal states (primed) 
		if(cost == 0)
		{
			ADD tree = list[cost];
			ADD copyTree = new ADD(tree.copyTree(tree.getRoot()));
			copyTree.prime();
			
			return copyTree.getRoot();
		}
		else if(cost > threshold) return new NodeTerminal(0);
		
		ADD tree = list[threshold-cost+1];
		ADD copyTree = new ADD(tree.copyTree(tree.getRoot()));
		copyTree.prime();
		
		return copyTree.getRoot();
	}
}
