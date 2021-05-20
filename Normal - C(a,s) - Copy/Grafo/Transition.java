package Grafo;

public class Transition
{
	private Node proxEstado;
	private double prob;
	private int cost;
	
	public Transition(Node node, double prob)
	{
		proxEstado = node;
		this.prob = prob;
	}
	
	public Transition(Node node, double prob, int cost)
	{
		proxEstado = node;
		this.prob = prob;
		this.cost = cost;
	}
	
	public Node getNode()
	{
		return proxEstado;
	}
	
	public double getProb()
	{
		return prob;
	}
	
	public void setCost(int cost)
	{
		this.cost = cost;
	}
	
	public int getCost()
	{
		return cost;
	}
}