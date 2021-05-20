package Domain;

import ADD.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Acao
{
	private String nome;
	private List<Integer> vars;
	private List<ADD> diagrams;
	private List<Integer> costList;
	private ADD cost;
	
	public Acao(String nome)
	{
		this.nome = nome;
		diagrams = new ArrayList<ADD>();
		vars = new ArrayList<Integer>();
		costList = new ArrayList<Integer>();
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
		for(ADD add : diagrams)
		{
			add.setRoot(add.order().getRoot());
			add.reduceAll(null, add.getRoot());
			// add.print();
		}
		cost.setRoot(cost.order().getRoot());
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
	
	public int hashCode()
	{
		return nome.hashCode();
	}
}