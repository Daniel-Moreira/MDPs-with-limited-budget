package Grafo;

public class Transition
{
	private Node proxEstado;
	private double prob;
	private int custo;
		
	public Transition(Node node, double prob)
	{
		proxEstado = node;
		this.prob = prob;
	}
	
	public Transition(Node node, double prob, int cost)
	{
		proxEstado = node;
		this.prob = prob;
		custo = cost;
	}
	
	public Node getNode()
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
}