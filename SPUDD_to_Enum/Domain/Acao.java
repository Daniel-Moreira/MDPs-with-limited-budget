package Domain;

import ADD.*;
import Leitura.Arquivo;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Acao
{
	private String nome;
	private List<Integer> vars;
	private List<ADD> diagrams;
	private List<Integer> costList;
	private ADD cost;
	private Map<Integer, List<Transition>> transition;
	
	public Acao(String nome)
	{
		this.nome = nome;
		diagrams = new ArrayList<ADD>();
		vars = new ArrayList<Integer>();
		costList = new ArrayList<Integer>();
		transition = new HashMap<Integer, List<Transition>>();
	}
	
	public void addTransition(State s, State sucessor, double value)
	{
		Transition t = new Transition(sucessor, value);
		
		if(!transition.containsKey(s.hashCode())) transition.put(s.hashCode(), new ArrayList<Transition>());
		List<Transition> transitions = transition.get(s.hashCode());
		transitions.add(t);
	}
	
	public String getName()
	{
		return nome;
	}
	
	public void addDiagram(ADD tree, Integer var)
	{
		diagrams.add(tree);
		vars.add(var);
	}
	
	public void orderDiagrams()
	{
		for(ADD add : diagrams) add.setRoot(add.order().getRoot());
	}
	
	public List<ADD> getDiagrams()
	{
		return diagrams;
	}
	
	public int sizeDiagrams()
	{
		return diagrams.size();
	}
	
	public ADD getDiagram(int i)
	{
		return diagrams.get(i);
	}
	
	public void addCostDiagram(ADD cost)
	{
		this.cost = cost;
	}
	
	public int getVar(int i)
	{
		return vars.get(i);
	}
	
	public ADD getCost()
	{
		return cost;
	}
	
	public ADD getCost(int threshold, ADD[] diagrams)
	{
		return cost.setCostDiagram(threshold, diagrams);
	}
	
	public void setCostList(List<Integer> costs)
	{
		this.costList = costs;
	}
	
	public List<Integer> getCostList()
	{
		return costList;
	}
	
	// 1-A
	public void createDualDiagram()
	{
		for(ADD d : diagrams)
		{
			ADD aux = new ADD(d.getRoot().getChild(1));
			aux = aux.op(1.0, ADD.MINUS);
			d.putNode(d.getRoot(), aux.getRoot(), 0);
			// d.print();
		}
	}
	
	public void print()
	{
		for(ADD d : diagrams) d.print();
	}
	
	public void printE()
	{
		System.out.println(getName());
		Set<Integer> keys = transition.keySet();
		for(Integer key : keys)
		{
			System.out.println(key + " " + Math.log10(key)/Math.log10(2));
			for(Transition t : transition.get(key)) t.print();
			System.out.println();
		}
	}
	
	public void printToFileE(Arquivo arq, Map<Integer, String> map)
	{
		arq.escreveArquivo("action " + getName());
		Set<Integer> keys = transition.keySet();
		for(Integer key : keys)
		{
			for(Transition t : transition.get(key))
			{
				arq.print("\t" + map.get(key) + " ");				
				t.printToFile(arq, map);
			}	
		}
		arq.escreveArquivo("endaction\n");
		transition.clear();
	}
	
	public int hashCode()
	{
		return nome.hashCode();
	}
}