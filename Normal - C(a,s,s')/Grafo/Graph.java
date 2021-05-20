package Grafo;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class Graph
{
	private Map<Integer, Node> nodes;
	private Map<Integer, Node> augmentedNodes;
	private Map<Integer, Acao> acoes;
	private Node initialState;
	private Map<Integer, Node> terminalStates;
	
	public Graph()
	{
		nodes = new HashMap<Integer, Node>();
		augmentedNodes = new HashMap<Integer, Node>();
		acoes = new HashMap<Integer, Acao>();
		terminalStates = new HashMap<Integer, Node>();
	}
	
	public List<Node> getAugmentedTerminalStates(int threshold)
	{
		List<Node> list = new ArrayList<Node>();
		Set<Integer> keys = terminalStates.keySet();
		for(Integer key : keys) list.add(getAugmentedNodes(terminalStates.get(key).getName(), 0));
		
		return list;
	}
	
	public List<Node> getAugmentedInitialStates(int threshold)
	{
		List<Node> list = new ArrayList<Node>();
		list.add(getAugmentedNodes(initialState.getName(), threshold));
		
		return list;
	}
	
	public Map<Integer, Node> getAugmentedNodes()
	{
		return augmentedNodes;
	}
	
	public Node getAugmentedNodes(String name, int threshold)
	{
		return augmentedNodes.get((name+threshold).hashCode());
	}
	
	public Node getAugmentedNodes(int code)
	{
		return augmentedNodes.get(code);
	}
	
	public Map<Integer, Acao> getActions()
	{
		return acoes;
	}
	
	public Acao getAction(String name)
	{
		return acoes.get(name.hashCode());
	}
	
	public Acao getAction(int code)
	{
		return acoes.get(code);
	}
	
	public void addAction(Acao a)
	{
		acoes.put(a.hashCode(), a);
	}
	
	public void createAugmentedMDP(boolean reverse, int threshold)
	{
		List<AugmentedNode> queue = new ArrayList<AugmentedNode>();
		Map<Integer, Boolean> visited = new HashMap<Integer, Boolean>();
		
		Map<Integer, Acao> acoes = this.acoes;
		if(reverse)
		{
			acoes = new HashMap<Integer, Acao>();
			Set<Integer> keys = this.acoes.keySet();
			for(Integer key : keys)	acoes.put(key, this.acoes.get(key).getTransposed(nodes));
			
			keys = terminalStates.keySet();
			for(Integer key : keys)
			{
				AugmentedNode terminalNode = terminalStates.get(key).extendNode(0);
				augmentedNodes.put(terminalNode.hashCode(), terminalNode);
				queue.add(terminalNode);
			}
		}
		else
		{
			AugmentedNode initialNode = initialState.extendNode(threshold);
			augmentedNodes.put(initialNode.hashCode(), initialNode);
			queue.add(initialNode);
		}
		
		Set<Integer> keys = acoes.keySet();
		while(!queue.isEmpty())
		{
			AugmentedNode sLine = queue.remove(0);
			visited.put(sLine.hashCode(), true);
			Node s = getNode(sLine.getName());
			int remainThresh = sLine.getThreshold();
			
			for(Integer key : keys)
			{
				Acao a = acoes.get(key);

				List<Transition> list = a.getTransitions(s);
				if(list == null) continue;
				for(Transition t : list)
				{
					int newThresh = remainThresh-t.getCost();
					if(newThresh < 0 || newThresh > threshold) continue;
					
					AugmentedNode aux = t.getNode().extendNode(newThresh);
					AugmentedNode e = (AugmentedNode) getAugmentedNodes(aux.hashCode());
					if(e == null)
					{
						e = aux;
						augmentedNodes.put(e.hashCode(), e);
					}
					sLine.addNext(e);
					
					if(!visited.containsKey(e.hashCode())) queue.add(e);
				}
			}
		}
	}
	
	public void putNode(Node n)
	{
		nodes.put(n.hashCode(), n);
	}
	
	public Node getNode(String name)
	{
		return nodes.get(name.hashCode());
	}
	
	public Node getNode(int code)
	{
		return nodes.get(code);
	}
	
	public Map<Integer, Node> getNodes()
	{
		return nodes;
	}
	
	public int nodesAmount()
	{
		return nodes.size();
	}
	
	public void setInitialState(String name)
	{
		initialState = getNode(name);
	}
	
	public Node getInitialState()
	{
		return initialState;
	}
	
	public void setTerminalStates(String name)
	{
		Node n = getNode(name);
		terminalStates.put(n.hashCode(), n);
		n.setGoal();
	}
	
	public Map<Integer, Node> getTerminalStates()
	{
		return terminalStates;
	}
	
	public void print()
	{
		Set<Integer> keys = nodes.keySet();
		for(Integer key : keys)
		{
			Node n = nodes.get(key);
			
			n.print();
		}
	}
	
	public void print(int threshold)
	{
		double[] lastP = new double[nodes.size()];
		Acao[] bestAction = new Acao[nodes.size()];
		Set<Integer> keys = nodes.keySet();
	
		for(int i = 0; i <= threshold; i ++)
		{
			int j = 0;
			for(Integer key : keys)
			{
				Node n = augmentedNodes.get((nodes.get(key).getName()+i).hashCode());
				if(n == null)
				{
					if(bestAction[j] != null) System.out.println(nodes.get(key).getName() + " (Threhold: " + i + ") Prob: " + lastP[j] + " action: " + bestAction[j].getName());
					else System.out.println(nodes.get(key).getName() + " (Threhold: " + i + ") Prob: " + lastP[j]);
				}
				else
				{
					n.print();
					lastP[j] = n.getP();
					bestAction[j] = n.getBestAction();
				}				
				j++;
			}
		}
		
		
		System.out.println();
		keys = augmentedNodes.keySet();
	
		for(Integer key : keys)	augmentedNodes.get(key).print();
	}
}
