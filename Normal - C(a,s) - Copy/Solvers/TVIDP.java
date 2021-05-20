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
		//Tarjan k = new Tarjan(initialNodes);
		List<List<Node>> componentes = k.findSCC();
		
		// System.out.println(componentes.size());
		// insertionSort(componentes);
		int i = 0;
		List<Node> allNodes = graph.getLNodes();
		while(!componentes.isEmpty())
		{
			List<Node> componenteI = componentes.remove(0);
			// List<Node> componenteI = componentes.remove(componentes.size()-1);
			AugmentedNode n = (AugmentedNode)componenteI.get(0);
			int thresh = n.getThreshold();
			for(; i <= thresh; i++) 
			{
				for(Node node : allNodes) 
				{
					AugmentedNode aN = (AugmentedNode)graph.getAugmentedNodes(node.getName(), i);
					if(i>0)
					{						
						AugmentedNode preN = (AugmentedNode)graph.getAugmentedNodes(node.getName(), i-1);
						aN.setP(preN.getP());
						aN.setBestAction(preN.getBestAction());
					}
					else
					{
						aN.setP(0);
						aN.setBestAction(null);
					}
				}
			}
			
			updateSCC(componenteI);
		}
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
		//for(Node n : nodes) update2((AugmentedNode)n);
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
				if(thresh-cost < 0) continue;
				Node e = graph.getAugmentedNodes(t.getNode().getName(), thresh-cost);
				
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
	
	public void update2(AugmentedNode n)
	{
		if(n.isGoal()) return;
		
		double pStar = n.getP();
		Acao aStar = null;
		
		int thresh = n.getThreshold();
		System.out.println(n.getName() + " " + thresh);
		Set<Integer> keys = graph.getActions().keySet();
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
				Node e = graph.getAugmentedNodes(t.getNode().getName(), thresh-cost);
				if(e == null) continue;
				
				if(e.isGoal()) pA += t.getProb();
				else pA += t.getProb()*e.getP();
			}
			
			if(pA == pStar)
				System.out.print(a.getName() + " ");
		}
		System.out.println();
	}
	
	public void print(){};
	
	public void print(List<Node> nodes)
	{
		for(Node n : nodes) n.print();
	}
}
