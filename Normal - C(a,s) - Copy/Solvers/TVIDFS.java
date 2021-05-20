package Solvers;

import Grafo.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Set;

public class TVIDFS extends Solver
{
	private List<List<Node>> componentes;
	private int threshold;
	
	public TVIDFS(Graph g, int threshold)
	{
		this.threshold = threshold;
		g.createAugmentedMDP(false, threshold);
		graph = g;
	}
	
	public String getName()
	{
		return "TVIDFS";
	}
	
	public void executa()
	{
		List<Node> initialNodes = graph.getAugmentedInitialStates(threshold);
		
		Kosaraju k = new Kosaraju(initialNodes);
		// Tarjan k = new Tarjan(initialNodes);
		componentes = k.findSCC();

		// System.out.println(componentes.size());
		for(List<Node> componenteI :componentes)
			updateSCC(componenteI);
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
		
		// print(nodes);
	}
	
	public void update(AugmentedNode n)
	{
		if(n.isGoal()) return;
		
		double pStar = 0;
		Acao aStar = null;
		
		Set<Integer> keys = graph.getActions().keySet();
		int threshold = n.getThreshold();
		for(Integer key : keys)
		{
			Acao a = graph.getAction(key);
			double pA = 0;
			List<Transition> list = a.getTransitions(n.getName());
			if(list == null) continue;
			for(Transition t : list)
			{
				int cost = t.getCost();
				Node e = graph.getAugmentedNodes(t.getNode().getName(), threshold-cost);
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
	
	public void print()
	{
		for(List<Node> componenteI :componentes) print(componenteI);
	}
	
	public void print(List<Node> nodes)
	{
		for(Node n : nodes) n.print();
	}
}
