package ADD;

import java.util.Objects;
import java.util.Arrays;

public class Node
{
	protected static final int NUMBER_CHILDS = 2;
	
	protected String name;
	protected boolean primed;
	protected Node[] childs;
	protected Integer hashCode;
	protected Integer hashCodeVar;
	
	public Node()
	{
		childs = new Node[NUMBER_CHILDS];
		primed = false;
	}
	
	public Node(String name)
	{
		this.name = name;
		childs = new Node[NUMBER_CHILDS];
		primed = false;
	}
	
	public Node(String name, boolean primed)
	{
		this.name = name;
		this.primed = primed; 
		childs = new Node[NUMBER_CHILDS];
	}
	
	public Node(String name, boolean primed, int hashCodeVar)
	{
		this.name = name;
		this.primed = primed; 
		childs = new Node[NUMBER_CHILDS];
		this.hashCodeVar = hashCodeVar;
	}
	
	public void prime()
	{
		primed = true;
	}
	
	public Node unPrime()
	{
		Node n = new Node(name);
		
		n.addChild(childs[0], 0);
		n.addChild(childs[1], 1);
		
		return n;
	}
	
	public boolean isPrimed()
	{
		return primed;
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
		return name + (primed ? "'" : "");
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
	
	public Node copy()
	{
		// Node copy = null;
		// if(hashCodeVar != null) new Node(name, primed, hashCodeVar);
		// else copy = new Node(name, primed);
		Node copy = new Node(name, primed);
		
		return copy;
	}
	
	public int hashCode()
	{
		if(hashCodeVar == null) hashCodeVar = print().hashCode();
		// return print().hashCode(); 
		
		return hashCodeVar;
	}
	
	// Reset the hashCode before change the ADD
	public void resetCode()
	{
		hashCode = null;
	}
	
	String testeString = null;
	
	public String getTesteString()
	{
		if(testeString == null) testeString = print() + childs[0].getTesteString() + childs[1].getTesteString();
		return testeString;
	}
	
	// Combine the var code with the children code
	public int treeHashCode()
	{
		if(this.hashCode != null) return this.hashCode;
		
		this.hashCode = Objects.hash(hashCode(), childs[0].treeHashCode(), childs[1].treeHashCode());
		// this.hashCode = hashCode()*173 + childs[0].treeHashCode()*1129^1 + childs[1].treeHashCode()*263;
		// Integer[] keys = {hashCode(), childs[0].treeHashCode(), childs[1].treeHashCode()};
		// hashCode = Arrays.hashCode(keys);
		// this.hashCode = getTesteString().hashCode();
		// this.hashCode = super.hashCode();
		
		return hashCode;
	}
}
