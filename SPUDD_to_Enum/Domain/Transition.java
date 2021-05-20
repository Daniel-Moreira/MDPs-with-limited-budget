package Domain;

import Domain.State;
import Leitura.Arquivo;

import java.util.Map;

public class Transition
{
	private State proxEstado;
	private double prob;
	private int custo;
		
	public Transition(State node, double prob)
	{
		proxEstado = node;
		this.prob = prob;
	}
	
	public Transition(State node, double prob, int cost)
	{
		proxEstado = node;
		this.prob = prob;
		custo = cost;
	}
	
	public State getNode()
	{
		return proxEstado;
	}
	
	public double getProb()
	{
		return prob;
	}
	
	public void setCost(int custo)
	{
		this.custo = custo;
	}
	
	public int getCost()
	{
		return custo;
	}
	
	public void print()
	{
		System.out.println("\t" + proxEstado.hashCode() + " " + prob);
	}
	
	public void printToFile(Arquivo arq, Map<Integer, String> map)
	{
		arq.escreveArquivo(map.get(proxEstado.hashCode()) + " " + prob);
	}
}