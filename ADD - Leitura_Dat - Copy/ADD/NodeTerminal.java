package ADD;

import java.math.BigDecimal;

public class NodeTerminal extends Node
{
	private double value;
	
	public NodeTerminal()
	{
		childs = new Node[NUMBER_CHILDS];
		value = 0;
	}
	
	public NodeTerminal(double value)
	{
		childs = new Node[NUMBER_CHILDS];
		this.value = value;
	}
	
	public void prime(){}
	
	public void setValue(double val)
	{
		value = val;
	}
	
	public double getValue()
	{
		return value;
	}
	
	public boolean isTerminal()
	{
		return true;
	}
	
	public Node times(Node n2)
	{
		NodeTerminal t2 = (NodeTerminal)n2;
		// return Dictionary.getItem(new NodeTerminal(value * t2.getValue()));
		return new NodeTerminal(value * t2.getValue());
	}
	
	public Node sum(Node n2)
	{
		NodeTerminal t2 = (NodeTerminal)n2;
		return new NodeTerminal(value + t2.getValue());
	}
	
	public Node max(Node n2)
	{
		NodeTerminal t2 = (NodeTerminal)n2;
		return new NodeTerminal(Math.max(value, t2.getValue()));
	}
	
	public Node minus(Node n2)
	{
		NodeTerminal t2 = (NodeTerminal)n2;
		return new NodeTerminal(t2.getValue() - value); 
	}
	
	public double absMinus(Node n2)
	{
		NodeTerminal t2 = (NodeTerminal)n2;
		return Math.abs(t2.getValue() - value);
	}
	
	public Node copy()
	{
		Node copy = new NodeTerminal(this.value);
		
		return copy;
	}
	
	public String print()
	{
		return String.valueOf(value);
	}
	
	public String getTesteString()
	{
		return treeHashCode()+"";
	}
	
	public int treeHashCode()
	{
		// long v = Double.doubleToLongBits(value);
		// BigDecimal b = new BigDecimal(value);
		// if(this.hashCode == null) hashCode = b.hashCode();
		// if(this.hashCode == null) hashCode = Double.toString(value).hashCode();
		if(this.hashCode == null) hashCode = Double.valueOf(value).hashCode();
		// if(this.hashCode == null) hashCode = (int)(v^(v>>>31));
		// if(this.hashCode == null) hashCode = String.format("%.3f", value).hashCode();
		
		return hashCode;
	}
}
