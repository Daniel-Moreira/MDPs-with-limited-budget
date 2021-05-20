package Domain;

import ADD.Node;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class Variables
{
	private static Map<Integer, Integer> positions = new HashMap<Integer, Integer>();
	
	public static void setVars(List<Node> vars)
	{
		int i = 0;
		
		for(Node node : vars) positions.put(node.hashCode(), i++);
	}
	
	public static int size()
	{
		return positions.size();
	}
	
	public static int getPosition(String var)
	{
		return positions.get(var.hashCode());
	}
}