package ADD;

import java.util.Objects;
import java.util.Arrays;

public class Dictionary
{
	public static final int TIMES = 0;
	public static final int SUM = 1;
	public static final int MAX = 2;
	public static final int MINUS = 3;
	public static final int COST_SETTING = 4;
	public static final int PRIME = 5;
	public static final int SUMOUT = 6;
	public static final int TOP = 7;
	
	private static CacheMap nodes = new CacheMap();
	private static CacheMap sum = new CacheMap();
	public static CacheMap times = new CacheMap();
	public static CacheMap topTimes = new CacheMap();
	private static CacheMap minus = new CacheMap();
	private static CacheMap max = new CacheMap();
	private static CacheMap sumOut = new CacheMap();
	public static int amount = 0;
	
	public static Node getItem(Node n, int varIndex, int op)
	{
		int hashCode = Objects.hash(n.treeHashCode(), varIndex);
		Node resultNode = null;
		
		if(op == SUMOUT) resultNode = (Node)sumOut.get(hashCode);
		
		return resultNode;
	}
	
	public static void putItem(Node n, int varIndex, Node result, int op)
	{
		if(op == SUMOUT) sumOut.put(Objects.hash(n.treeHashCode(), varIndex), result);
	}
	
	public static Node getItem(Node n1, Node n2, int op)
	{
		int hashCode = hash(n1, n2);
		Node resultNode = null;
		
		// for(int i = 0; i < 1; i ++)
		// {
			if(op == TIMES)
			{
				resultNode = (Node)times.get(hashCode);
				amount++;
			}
			else if(op == SUM) resultNode = (Node)sum.get(hashCode);
			else if(op == MAX) resultNode = (Node)max.get(hashCode);
			else if(op == MINUS) resultNode = (Node)minus.get(hashCode);
			else if(op == TOP)
			{
				resultNode = (Node)topTimes.get(hashCode);
				amount++;
			}
		
			// if(resultNode == null) break;
			// else hashCode = hash(n2, n1);
		// }
	
		return resultNode;
	}
	
	public static void putItem(Node n1, Node n2, Node result, int op)
	{
		if(op == TIMES)
		{
			// if(times.containsKey(hash(n1, n2))) System.out.println("collision");
			times.put(hash(n1, n2), result);
		}	
		else if(op == SUM) sum.put(hash(n1, n2), result);
		else if(op == MAX) max.put(hash(n1, n2), result);
		else if(op == MINUS) minus.put(hash(n1, n2), result);
		else if(op == TOP) topTimes.put(hash(n1, n2), result);
	}
	
	// Combine both hashCodes
	private static int hash(Node n1, Node n2)
	{
		// int hashCode = Objects.hash(n1.treeHashCode(), n2.treeHashCode());
		int hashCode = n1.treeHashCode()*1201^1 + n2.treeHashCode()*397;
		// hashCode = (n1.getTesteString() + " op " + n2.getTesteString()).hashCode();
		// Integer[] keys = {n1.treeHashCode(), n2.treeHashCode()};
		// int hashCode = Arrays.hashCode(keys);
		
		return hashCode;
	}
	
	public static Node getItem(Node n)
	{
		if(nodes.containsKey(n.treeHashCode())) return (Node)nodes.get(n.treeHashCode());
		
		nodes.put(n.treeHashCode(), n);
		return n;
	}
}
