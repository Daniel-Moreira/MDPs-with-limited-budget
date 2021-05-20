package Domain;

import ADD.Node;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Order
{
	private static Map<Integer, Integer> order;
	
	public static void setOrder(List<Node> nodes)
	{
		order = new HashMap<Integer, Integer>();
		int i = 0;
		for(Node n : nodes) order.put(n.hashCode(), i++);
	}
	
	public static int getIndex(int key)
	{
		return order.get(key);
	}
	
	public static int getIndex(Node n)
	{
		if(n.isTerminal()) return order.size();
		
		return order.get(n.hashCode());
	}
	
	public static int size()
	{
		return order.size();
	}
}