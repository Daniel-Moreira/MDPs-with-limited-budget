package ADD;

import java.util.Objects;

public class Dictionary
{
	public static final int TIMES = 0;
	public static final int SUM = 1;
	public static final int MAX = 2;
	public static final int MINUS = 3;
	
	private static CacheMap nodes = new CacheMap();
	private static CacheMap sum = new CacheMap();
	private static CacheMap times = new CacheMap();
	private static CacheMap minus = new CacheMap();
	private static CacheMap max = new CacheMap();
	
	public static Node getItem(Node n1, Node n2, int op)
	{
		int hashCode = hash(n1, n2);
		Node resultNode = null;
		
		if(op == TIMES) resultNode = (Node)times.get(hashCode);
		else if(op == SUM) resultNode = (Node)sum.get(hashCode);
		else if(op == MAX) resultNode = (Node)max.get(hashCode);
		else if(op == MINUS) resultNode = (Node)minus.get(hashCode);
	
		return resultNode;
	}
	
	public static void putItem(Node n1, Node n2, Node result, int op)
	{
		if(op == TIMES) times.put(hash(n1, n2), result);
		else if(op == SUM) sum.put(hash(n1, n2), result);
		else if(op == MAX) max.put(hash(n1, n2), result);
		else if(op == MINUS) minus.put(hash(n1, n2), result);
	}
	
	// Combine both hashCodes
	private static int hash(Node n1, Node n2)
	{
		int hashCode = Objects.hash(n1.treeHashCode(), n2.treeHashCode());
		// int hashCode = n1..treeHashCode()*31 + n2..treeHashCode()*41;
		
		return hashCode;
	}
	
	public static Node getItem(Node n)
	{
		if(nodes.containsKey(n.treeHashCode())) return (Node)nodes.get(n.treeHashCode());
		
		nodes.put(n.treeHashCode(), n);
		return n;
	}
}
