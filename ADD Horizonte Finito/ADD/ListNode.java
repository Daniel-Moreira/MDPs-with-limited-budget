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
		if(cost == 0) return new NodeTerminal(1);
		else if(cost > threshold) return new NodeTerminal(0);
		
		ADD tree = list[threshold-cost];
		// ADD copyTree = new ADD(tree.copyTree(tree.getRoot()));
		// copyTree.prime();
		tree.prime();
		
		return tree.getRoot();
	}
}
