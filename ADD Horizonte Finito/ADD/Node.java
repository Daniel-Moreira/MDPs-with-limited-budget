package ADD;

import java.util.Objects;

public class Node
{
	public static final int NUMBER_CHILDS = 2;
	
	protected String name;
	protected Node[] childs;
	protected Integer hashCode;
	//private int min;
	//private int max;
	
	public Node()
	{
		childs = new Node[NUMBER_CHILDS];
		//min = Integer.MAX_VALUE;
		//max = 0;
	}
	
	public Node(String name)
	{
		this.name = name;
		childs = new Node[NUMBER_CHILDS];
		//min = Integer.MAX_VALUE;
		//max = 0;
	}
	
	/*public Node(String name, int min, int max)
	{
		this.name = name;
		childs = new Node[NUMBER_CHILDS];
		this.min = min;
		this.max = max;
	}*/
	
	public void prime()
	{
		name += "'";
	}
	
	public void addChild(Node child, int side)
	{
		childs[side] = child;
	}
	
	public Node getChild(int i)
	{
		return childs[i];
	}
	
	public boolean isTerminal()
	{
		return false;
	}
	
	public boolean equals(Node aux)
	{
		if(aux.isTerminal()) return false;
		
		if(hashCode() == aux.hashCode()) return true;
		
		return false;
	}
	
	public String print()
	{
		return name;
	}
	
	// Check on the dictionary
	public Node times(Node n2)
	{
		return Dictionary.getItem(this, n2, Dictionary.TIMES);
	}
	
	// Check on the dictionary
	public Node sum(Node n2)
	{
		return Dictionary.getItem(this, n2, Dictionary.SUM);
	}
	
	// Check on the dictionary
	public Node max(Node n2)
	{
		return Dictionary.getItem(this, n2, Dictionary.MAX);
	}
	
	// Check on the dictionary
	public Node minus(Node n2)
	{
		return Dictionary.getItem(this, n2, Dictionary.MINUS);
	}
	
	public Node setCost(Node n2)
	{
		return null;
	}
	
	// Check on the dictionary
	public double absMinus(Node n2)
	{
		return 0;
	}
	
	/*public void setMin(int value)
	{
		min = Math.min(value, min);
	}
	
	public int getMin()
	{
		return min;
	}
		
	public void setMax(int value)
	{
		max = Math.max(value, max);
	}

	public int getMax()
	{
		return max;
	}*/
	
	public Node copy()
	{
		//Node copy = new Node(name, min, max);
		Node copy = new Node(name);
		
		return copy;
	}
	
	public int hashCode()
	{
		return name.hashCode();
	}
	
	// Combine the var code and the children code
	public int treeHashCode()
	{
		if(this.hashCode != null) return this.hashCode;
		
		this.hashCode = Objects.hash(hashCode(), childs[0].treeHashCode(), childs[1].treeHashCode());
		// this.hashCode = ((hashCode() + childs[0].treeHashCode())*31 + childs[1].treeHashCode())*31;
		
		return hashCode;
	}
}
