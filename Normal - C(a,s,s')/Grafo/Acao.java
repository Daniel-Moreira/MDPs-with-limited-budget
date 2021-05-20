package Grafo;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class Acao
{
	private String nome;
	private Map<Integer, List<Transition>> transicoes;
	
	public Acao(String nome)
	{
		this.nome = nome;
		transicoes = new HashMap<Integer, List<Transition>>();
	}
	
	public String getName()
	{
		return nome;
	}
	
	public Acao getTransposed(Map<Integer, Node> nodes)
	{
		Acao newAction = new Acao(nome);
		Set<Integer> keys = transicoes.keySet();
		for(Integer key : keys)
			for(Transition t : transicoes.get(key))	newAction.addTransition(t.getNode(), nodes.get(key), t.getProb(), -t.getCost());
	
		return newAction;
	}
	
	public void addTransition(Node parent, Node proxEstado, double prob, int cost)
	{
		Transition t = new Transition(proxEstado, prob, cost);
		
		if(!transicoes.containsKey(parent.hashCode())) transicoes.put(parent.hashCode(), new ArrayList<Transition>());
		List<Transition> transitions = transicoes.get(parent.hashCode());
		transitions.add(t);
	}
	
	public void addTransition(Node parent, Node proxEstado, double prob)
	{
		Transition t = new Transition(proxEstado, prob);
		
		if(!transicoes.containsKey(parent.hashCode())) transicoes.put(parent.hashCode(), new ArrayList<Transition>());
		List<Transition> transitions = transicoes.get(parent.hashCode());
		transitions.add(t);
	}

	public Transition getTransition(String parent, String proxEstado)
	{
		for(Transition t : transicoes.get(parent.hashCode()))
			if(t.getNode().getName().equals(proxEstado)) return t;

		return null;
	}
	
	public List<Transition> getTransitions(Node n)
	{
		return transicoes.get(n.hashCode());
	}
	
	public List<Transition> getTransitions(String n)
	{
		return transicoes.get(n.hashCode());
	}
	
	public int hashCode()
	{
		return nome.hashCode();
	}
}