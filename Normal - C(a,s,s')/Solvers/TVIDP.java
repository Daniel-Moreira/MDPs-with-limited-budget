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
		
		// Kosaraju k = new Kosaraju(initialNodes);
		Tarjan k = new Tarjan(initialNodes);
		List<List<Node>> componentes = k.findSCC();
		
		System.out.println(componentes.size());
		putNodes();
		insertionSort(componentes);
		while(!componentes.isEmpty())
		{
			List<Node> componenteI = componentes.remove(0);
			// List<Node> componenteI = componentes.remove(componentes.size()-1);
			updateSCC(componenteI);
		}
		
		graph.print(threshold);
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
		double error = 0.001;
		double residual = 0;
		do
		{
			residual = 0;
			for(Node n : nodes)
			{
				double p = n.getP();
				update((AugmentedNode)n);
				if(residual < Math.abs(p-n.getP())) residual = Math.abs(p-n.getP());
			}
		} while(residual > error);
	}
	
	public void update(AugmentedNode n)
	{
		if(n.isGoal()) return;
		
		double pStar = 0;
		Acao aStar = null;
		
		Set<Integer> keys = graph.getActions().keySet();
		int thresh = n.getThreshold();
		boolean teste = false;
		if(n.getName().equals("s1") && thresh == 4) teste = true;
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
			list.add(((AugmentedNode)n).getThreshold());
		}
		for(Integer key : keys) Collections.sort(augmentedNodes.get(aN.get(key).getName().hashCode()));		
	}
	
	private Node getClosestAugmentedNodes(String name, int threshold, boolean teste)
	{
		if(threshold < 0) return null;
		
		List<Integer> nodes = augmentedNodes.get(name.hashCode());
		int lastThresh = 0;
		for(Integer thresh : nodes)
		{
			if(teste) System.out.println(name + " " + thresh + " " + threshold);
			if(thresh == threshold) return graph.getAugmentedNodes(name, threshold);
			if(thresh > threshold) return graph.getAugmentedNodes(name, lastThresh);
			
			lastThresh = thresh;
		}
		
		if(teste) System.out.println(name + " " + lastThresh + " " + threshold + " " + graph.getAugmentedNodes(name, lastThresh).getP());
		return graph.getAugmentedNodes(name, lastThresh);
	}
	
	public void print(List<Node> nodes)
	{
		for(Node n : nodes) n.print();
	}
}
