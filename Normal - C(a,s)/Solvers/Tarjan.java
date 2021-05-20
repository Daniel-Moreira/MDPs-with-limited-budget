package Solvers;

import Grafo.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class Tarjan
{
	public List<Node> initialNodes;
	public Map<Integer, Node> visited;
	public Map<Integer, Integer> index;
	public Map<Integer, Integer> lowIndex;
	public Map<Integer, Boolean> onStack;
	public Stack<Node> stack;
	public List<List<Node>> scc;
	private int indexN;
	
	public Tarjan(List<Node> nodes)
	{
		initialNodes = nodes;
		visited = new HashMap<Integer, Node>();
		index = new HashMap<Integer, Integer>();
		lowIndex = new HashMap<Integer, Integer>();
		onStack = new HashMap<Integer, Boolean>();
		stack = new Stack<Node>();
		scc = new ArrayList<List<Node>>();
		indexN = 0;
	}
	
	public List<List<Node>> findSCC()
	{	
		for(Node s : initialNodes) if(!visited.containsKey(s.hashCode())) tarjanVisit((AugmentedNode)s);
		
		return scc;
	}
	
	public void tarjanVisit(AugmentedNode s)
	{
		index.put(s.hashCode(), indexN);
		lowIndex.put(s.hashCode(), indexN++);				
		stack.push(s);
		visited.put(s.hashCode(), s);
		onStack.put(s.hashCode(), true);
		
		List<AugmentedNode> list = s.getNexts();
		for(AugmentedNode e : list)
		{
			if(!visited.containsKey(e.hashCode()))
			{
				tarjanVisit(e);	
				lowIndex.put(s.hashCode(), Math.min(lowIndex.get(s.hashCode()), lowIndex.get(e.hashCode())));
			}
			else if(onStack.containsKey(e.hashCode())) lowIndex.put(s.hashCode(), Math.min(lowIndex.get(s.hashCode()), index.get(e.hashCode())));
		}
		
		if(lowIndex.get(s.hashCode()) == index.get(s.hashCode()))
		{
			List<Node> aux = new ArrayList<Node>();
			Node n = null;
			do
			{
				n = stack.pop();
				onStack.remove(n.hashCode());
				aux.add(n);
			} while(n != s);
			scc.add(aux);
		}
	}
	
	// private void tarjanVisit(AugmentedNode s)
	// {
		// Stack<AugmentedNode> stackProfundidade = new Stack<AugmentedNode>();
		// stackProfundidade.push(s);
		// Map<Integer, Boolean> onStack = new HashMap<Integer, Boolean>();
		// onStack.put(s.hashCode(), true);
		
		// Busca em profundidade
		// while(!stackProfundidade.empty())
		// {
			// s = stackProfundidade.peek();

			// if(!visited.containsKey(s.hashCode()))
			// {
				// index.put(s.hashCode(), indexN);
				// lowIndex.put(s.hashCode(), indexN++);				
				// stack.push(s);
				// visited.put(s.hashCode(), s);
			// }	
			
			// boolean noChild = true;
			// List<AugmentedNode> list = s.getNexts();
			// for(AugmentedNode e : list)
			// {
				// if(!visited.containsKey(e.hashCode()))
				// {
					// if(onStack.containsKey(e.hashCode()) || !onStack.get(e.hashCode())) stackProfundidade.push(e);
					// onStack.put(e.hashCode(), true);
					// noChild = false;
				// }
				// else if(onStack.get(e.hashCode())) lowIndex.put(s.hashCode(), Math.min(lowIndex.get(s.hashCode()), index.get(e.hashCode())));
			// }
			// if(noChild)
			// {
				// s = stackProfundidade.pop();
				// if(lowIndex.get(s.hashCode()) == index.get(s.hashCode()))
				// {
					// List<Node> aux = new ArrayList<Node>();
					// Node n = null;
					// s.print();
					// System.out.println();
					// do
					// {
						// n = stack.pop();
						// onStack.put(n.hashCode(), false);
						// n.print();
						// aux.add(n);
					// } while(n != s);
					// System.out.println();
					// scc.add(aux);
				// }
			// }	
		// }
	// }
}
