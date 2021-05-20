package ADD;

public class NodeTerminal extends Node
{
	private double value;
	
	public NodeTerminal()
	{
		childs = new Node[NUMBER_CHILDS];
		value = 0;
		//min = Integer.MAX_VALUE;
		//max = 0;
	}
	
	public NodeTerminal(double value)
	{
		childs = new Node[NUMBER_CHILDS];
		this.value = value;
		//min = Integer.MAX_VALUE;
		//max = 0;
	}
	
	/*public NodeTerminal(String name, double value, int min, int max)
	{
		this.name = name;
		childs = new Node[NUMBER_CHILDS];
		this.value = value;
		this.min = min;
		this.max = max;
	}*/
	
	public void prime(){}
	
	public void setValue(int val)
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
		return Dictionary.getItem(new NodeTerminal(value * t2.getValue()));
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
		//NodeTerminal copy = new Node(name, this.value, this.min, this.max);
		Node copy = new NodeTerminal(this.value);
		
		return copy;
	}
	
	public String print()
	{
		return String.valueOf(value);
	}
	
	public int treeHashCode()
	{
		long v = Double.doubleToLongBits(value);
		// if(this.hashCode == null) hashCode = value.hashCode();
		if(this.hashCode == null) hashCode = (int)(v^(v>>>32));
		return hashCode;
	}
}
