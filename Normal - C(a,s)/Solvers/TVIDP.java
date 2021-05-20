package Solvers;

import Grafo.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class TVIDP extends Solver
{
	private int threshold;
	
	public TVIDP(Graph g, int threshold)
	{
		this.threshold = threshold;
		graph = g;
		graph.createAugmentedMDP(true, threshold);
	}
	
	public String getName()
	{
		return "TVIDP";
	}
	
	public void executa()
	{
		List<Node> initialNodes = graph.getAugmentedTerminalStates(threshold);
		
		Kosaraju k = new Kosaraju(initialNodes);
		// Tarjan k = new Tarjan(initialNodes);
		List<List<Node>> componentes = k.findSCC();
		
		putNodes();
		// System.out.println(componentes.size());
		// insertionSort(componentes);
		int lThresh = 1;
		while(!componentes.isEmpty())
		{
			List<Node> componenteI = componentes.remove(0);
			// List<Node> componenteI = componentes.remove(componentes.size()-1);
			AugmentedNode n = (AugmentedNode)componenteI.get(0);
			int thresh = n.getThreshold();
			
			if(thresh > lThresh)
			{
				lThresh = thresh;
				if(check(thresh-1))
				{
					System.out.println(thresh-1);				
					break;
				}
			}
			updateSCC(componenteI);
		}
	}
	
	public boolean check(int t)
	{
		for(Node n : graph.getListNodes())
		{
			Node teste = getClosestAugmentedNodes(n.getName(), t, false);
			Node teste2 = getClosestAugmentedNodes(n.getName(), t-1, false);
			double compare = 0;
			double compare2 = 0;
			if(teste != null) compare = teste.getP();
			if(teste2 != null) compare2 = teste2.getP();
			// System.out.println(n.getP() + " " + compare);
			// System.out.println(n.getP() != compare);
			// System.out.println();
			if(compare2 != compare)	return false;
		}
		
		return true;
	}
	
	public void insertionSort(List<List<Node>> array)
	{
		int i, j;

		for (i = 1; i < array.size(); i++)
		{
			List<Node> key = array.get(i);
			j = i;
			while((j > 0) && (compareTo(array.get(j-1), key) > 0))
			{
				array.set(j, array.get(j-1));
				j--;
			}
			array.set(j, key);
		}
	}
	
	public int compareTo(List<Node> listA, List<Node> listB)
	{
		AugmentedNode a = (AugmentedNode) listA.get(0);
		AugmentedNode b = (AugmentedNode) listB.get(0);
		
		if(a.getThreshold() >= b.getThreshold()) return 1;
		
		return -1;
	}
	
	public void updateSCC(List<Node> nodes)
	{
		// double error = 0.000000000001;
		// double residual = 0;
		// do
		// {
			// residual = 0;
			for(Node n : nodes)
			{
				double p = n.getP();
				update((AugmentedNode)n);
				// if(residual < Math.abs(p-n.getP())) residual = Math.abs(p-n.getP());
			}
		// } while(residual > error);
	}
	
	public void update(AugmentedNode n)
	{
		if(n.isGoal()) return;
		
		double pStar = 0;
		Acao aStar = null;
		
		// System.out.println(n.getName());
		Set<Integer> keys = graph.getActions().keySet();
		int thresh = n.getThreshold();
		boolean teste = false;
		// if(n.getName().equals("s1") && thresh == 4) teste = true;
		for(Integer key : keys)
		{
			Acao a = graph.getAction(key);
			double pA = 0;
			List<Transition> list = a.getTransitions(n.getName());
			if(list == null) continue;
			for(Transition t : list)
			{
				int cost = t.getCost();
				Node e = getClosestAugmentedNodes(t.getNode().getName(), thresh-cost, teste);
				if(e == null) continue;
				
				if(e.isGoal()) pA += t.getProb();
				else pA += t.getProb()*e.getP();
			}
			
			if(pA > pStar)
			{
				pStar = pA;
				aStar = a;
			}
		}
		
		// System.out.println();
		n.setP(pStar);
		n.setBestAction(aStar);
	}
	
	private Map<Integer, List<Integer>> augmentedNodes = new HashMap<Integer, List<Integer>>();
	
	private void putNodes()
	{
		Map<Integer, Node> aN = graph.getAugmentedNodes();
		Set<Integer> keys = aN.keySet();
		for(Integer key : keys)
		{
			Node n = aN.get(key);
			int hash = n.getName().hashCode();
			List<Integer> list = augmentedNodes.get(hash);
			if(list == null) augmentedNodes.put(hash, new ArrayList<Integer>());
			list = augmentedNodes.get(hash);
			// System.out.println(n.getName() + " " + ((AugmentedNode)n).getThreshold());
			int i = 0;
			int thresh = ((AugmentedNode)n).getThreshold();
			while(i < list.size())
			{
				if(thresh > list.get(i)) break;
				i++;
			}
			list.add(i, thresh);
		}
		// for(Integer key : keys) Collections.sort(augmentedNodes.get(aN.get(key).getName().hashCode()));		
	}
	
	private Node getClosestAugmentedNodes(String name, int threshold, boolean teste)
	{
		if(threshold < 0) return null;
		
		List<Integer> nodes = augmentedNodes.get(name.hashCode());
		// Don't reach the goal
		if(nodes == null) return null;
		if(teste) System.out.println(name + " " + threshold);
		// for(Integer thresh : nodes) System.out.println(thresh);
		for(Integer thresh : nodes)
			if(thresh <= threshold) return graph.getAugmentedNodes(name, thresh);
		
		return null;
	}
	
	public void print(){};
	
	public void print(List<Node> nodes)
	{
		for(Node n : nodes) n.print();
	}
}
