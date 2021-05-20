package Solvers;

import Leitura.Arquivo;
import ADD.*;
import Domain.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import java.util.Scanner;

public class SPUDD_to_Enum
{
	private Domain domain;
	private Map<Integer, State> states;
	private Map<Integer, State> visited;
	private List<State> estados;
	private List<Acao> actions;
	private ADD initialState;
	private ADD goalState;
	private int globalCount;
	
	public SPUDD_to_Enum(Domain d)
	{
		domain = d;
		globalCount = 0;
		initialState = domain.getInitialState();
		goalState = domain.getGoalState();
		actions = domain.getActions();
		states = new HashMap<Integer, State>();
		visited = new HashMap<Integer, State>();
		estados = new ArrayList<State>();
	}
	
	public void enumera()
	{
		Scanner sc = new Scanner(System.in);
		
		for(Acao a : actions) a.createDualDiagram();
		for(Acao a : actions) a.orderDiagrams();
	
		getState(new State(), null, initialState.getRoot(), null);
		
		int count = 0;
		int fileCount = 0;
		while(!estados.isEmpty())
		{
			State s = estados.remove(0);
			visited.put(s.hashCode(), s);
			
			// s.print();
			for(Acao a : actions)
			{
				// System.out.println(a.getName());
				
				ADD aux = new ADD(apply(a.getDiagram(0).getRoot(), s));
				for(int i = 1; i < a.sizeDiagrams(); i++)
				{
					ADD actionDiagram = a.getDiagram(i);

					// Get the Sucessors
					// actionDiagram.print();
					Node node = apply(actionDiagram.getRoot(), s);
					ADD teste = new ADD(node);
					
					aux = aux.op(teste, ADD.TIMES);
					// if(a.getName().equals("movewest")) teste.print();
					// Put new States and transitions
					// if(a.getName().equals("movewest")) sc.next();
				}
				getState(new State(), s, aux.getRoot(), a);				
			}
			// System.out.println();
			System.out.println("AQUI " + ++count);
			// if(count == 100)
			// {	
				// printToFile2(new Arquivo("arq_aux" + fileCount++ +  ".net", Arquivo.ESCRITA));
				// count = 0;
			// }	
		}
	}
	
	public void printToFile2(Arquivo arq)
	{
		int i = 0;
		String s = "state_";
		Map<Integer, String> m = new HashMap<Integer, String>();
		
		arq.escreveArquivo("states");
		Set<Integer> keys = states.keySet();
		for(Integer key : keys)
		{
			m.put(key, s+i);
			if(i+1 != states.size()) arq.print(s+i + ", ");
			else arq.print(s+i);
			i++;
		}
		arq.escreveArquivo("\nendstates\n");
		
		for(Acao a : actions) a.printToFileE(arq, m);
		m.clear();
	}
	
	public Node apply(Node aux, State s)
	{
		if(aux.isPrimed()) return aux.unPrime();
		// Should create a tree with primed variable and 0.5 as terminals
			// Problem arrive from 0.5 definition
		if(aux.isTerminal()) return aux;
		
		Node node = null;
		// System.out.println(aux.isPrimed());
		// System.out.println(aux.print());
		int i = s.getIndex(aux.print());
		node = apply(aux.getChild(i), s);
		
		return node;
	}
	
	public void print()
	{
		System.out.println(states.size());
		Set<Integer> keys = states.keySet();
		System.out.println();
		for(Integer key : keys) states.get(key).print();
		for(Acao a : actions) a.printE();
	}

	public void printToFile(Arquivo arq)
	{
		int i = 0;
		String s = "state_";
		Map<Integer, String> m = new HashMap<Integer, String>();
		
		arq.escreveArquivo("states");
		Set<Integer> keys = states.keySet();
		for(Integer key : keys)
		{
			m.put(key, s+i);
			if(i+1 != states.size()) arq.print(s+i + ", ");
			else arq.print(s+i);
			i++;
		}
		arq.escreveArquivo("\nendstates\n");
		
		for(Acao a : actions) a.printToFileE(arq, m);
		
		// Print costs
		arq.escreveArquivo("cost");
		keys = states.keySet();
		for(Integer key : keys)
		{
			State sta = states.get(key);
			for(Acao a : actions)
			{
				ADD cost = a.getCost();
				Node node = apply(cost.getRoot(), sta);
				
				arq.escreveArquivo("\t" + a.getName() + " " + m.get(key) + " " + (int)((NodeTerminal)node).getValue());
			}
		}
		arq.escreveArquivo("endcost\n");
		
		states.clear();
		getState(new State(), null, initialState.getRoot(), null);
		arq.escreveArquivo("initialstate");
		keys = states.keySet();
		for(Integer key : keys)	arq.escreveArquivo(m.get(key));
		arq.escreveArquivo("endinitialstate\n");
		states.clear();
		getState(new State(), null, goalState.getRoot(), null);
		arq.escreveArquivo("goalstate");
		keys = states.keySet();
		for(Integer key : keys)	arq.escreveArquivo(m.get(key));
		arq.escreveArquivo("endgoalstate");
	}
	
	public void getState(State s, State antS, Node aux, Acao action)
	{
		if(aux.isTerminal())
		{
			aux.print();
			if(((NodeTerminal)aux).getValue() > 0)
			{
				if(!states.containsKey(s.hashCode()))
				{
					// s.print();
					states.put(s.hashCode(), s);
					if(!visited.containsKey(s.hashCode())) estados.add(s);					
				}
				if(action != null)
				{
					action.addTransition(antS, s, ((NodeTerminal)aux).getValue());	
					// System.out.println("Transicao");
					// antS.print();
					// s.print();
					// System.out.println("Fim Trans");						
				}
			}
			return;
		}
		
		State copy = s.copy();
		copy.setVar(aux.print(), 0);
		getState(copy, antS, aux.getChild(0), action);
		copy = s.copy();
		copy.setVar(aux.print(), 1);
		getState(copy, antS, aux.getChild(1), action);
	}
}
