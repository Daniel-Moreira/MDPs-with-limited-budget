package Solvers;

import Grafo.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

// Kosaraju a partir de estados iniciais
public class Kosaraju
{
	public List<Node> initialNodes;
	public Map<Integer, Node> visited;
	public List<List<Node>> componentes = new ArrayList<List<Node>>();
	public Stack<Node> stack;
	
	public Kosaraju(List<Node> nodes)
	{
		initialNodes = nodes;
		visited = new HashMap<Integer, Node>();
		stack = new Stack<Node>();
	}
	
	public List<List<Node>> findSCC()
	{
		visited.clear();
		stack.clear();
		
		for(Node s : initialNodes) if(!visited.containsKey(s.hashCode())) kosarajuVisit((AugmentedNode)s);
		
		visited.clear();
		// List<List<Node>> componentes = new ArrayList<List<Node>>();
		// while(!stack.empty())
		// {
			// Node s = stack.pop();
			
			// List<Node> atual = new ArrayList<Node>();
			// kosarajuAssign((AugmentedNode)s, atual);
			// if(atual.size() > 0) componentes.add(0, atual);
		// }
		
		return componentes;
	}

	private void kosarajuVisit(AugmentedNode s)
	{
		List<AugmentedNode> l = new ArrayList<AugmentedNode>();
		l.add(s);
		visited.put(s.hashCode(), s);
		while(!l.isEmpty())
		{
			s = l.remove(0);
			List<AugmentedNode> list = s.getNexts();
			List<Node> atual = new ArrayList<Node>();
			atual.add(s);
			componentes.add(atual);
			for(AugmentedNode e : list)
			{
				if(!visited.containsKey(e.hashCode()))
				{					
					visited.put(e.hashCode(), e);
					l.add(e);
					e.addPrevious(s);
				}
			}
		}
	}
	
	// private void kosarajuVisit(AugmentedNode s)
	// {
		// visited.put(s.hashCode(), s);
		// List<AugmentedNode> list = s.getNexts();
		// for(AugmentedNode e : list)
		// {		
			// if(!visited.containsKey(e.hashCode())) kosarajuVisit(e);
			// e.addPrevious(s);
		// }
		// stack.add(s);	
	// }
	
	// private void kosarajuVisit(AugmentedNode s)
	// {
		// Stack<AugmentedNode> stackProfundidade = new Stack<AugmentedNode>();
		// stackProfundidade.push(s);
		
		// Busca em profundidade
		// while(!stackProfundidade.empty())
		// {
			// s = stackProfundidade.peek();

			// visited.put(s.hashCode(), s);
			// boolean noChild = true;
			// List<AugmentedNode> list = s.getNexts();
			// for(AugmentedNode e : list)
			// {		
				// if(!visited.containsKey(e.hashCode()))
				// {
					// stackProfundidade.push(e);
					// noChild = false;
				// }
				// e.addPrevious(s);
			// }
			// if(noChild)	stack.add(stackProfundidade.pop());
		// }
	// }
	
	private void kosarajuAssign(AugmentedNode s, List<Node> componentes)
	{
		if(!visited.containsKey(s.hashCode())) componentes.add(s);
		visited.put(s.hashCode(), s);

		for(AugmentedNode e : s.getPrevious()) if(!visited.containsKey(e.hashCode())) kosarajuAssign(e, componentes);
	}
	
	// private void kosarajuAssign(AugmentedNode s, List<Node> componentes)
	// {
		// Stack<AugmentedNode> stackProfundidade = new Stack<AugmentedNode>();
		// stackProfundidade.push(s);
		
		// while(!stackProfundidade.empty())
		// {
			// s = stackProfundidade.pop();
			// if(!visited.containsKey(s.hashCode()))
			// {
				// componentes.add(s);
				// s.print();
			// }
			// visited.put(s.hashCode(), s);

			// for(AugmentedNode e : s.getPrevious()) if(!visited.containsKey(e.hashCode())) stackProfundidade.push(e);
		// }
		
		// System.out.println();
	// }
}
