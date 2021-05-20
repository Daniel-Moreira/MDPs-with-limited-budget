package ADD;

import Domain.Order;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class ADD
{
	public static final int TIMES = 0;
	public static final int SUM = 1;
	public static final int MAX = 2;
	public static final int MINUS = 3;
	public static final int COST_SETTING = 4;
	public static final int PRIME = 5;
	
	// private Map<Integer, Node> nodes;
	private Node root;
	
	public ADD()
	{
		//nodes = new HashMap<Integer, Node>();
	}
	
	public ADD(Node root)
	{
		//nodes = new HashMap<Integer, Node>();
		this.root = root;
		//nodes.put(n.hashCode(), n);
	}
	
	public void setRoot(Node root)
	{
		this.root = root;
	}
	
	public Node getRoot()
	{
		return root;
	}
		
	public void putNode(Node parent, Node child, int side)
	{
		//nodes.put(child.hashCode(), child);
		
		parent.addChild(child, side);
	}

	public ADD op(double value, int op)
	{
		Node aux = getRoot();
		Node aux2 = new NodeTerminal(value);
		ADD resultTree = new ADD();
		
		Node root = recursivelyDoOp(resultTree, aux, aux2, op);
		resultTree.setRoot(root);
		
		return resultTree;
	}
	
	public ADD op(ADD tree2, int op)
	{
		Node aux = getRoot();
		Node aux2 = tree2.getRoot();
		ADD resultTree = new ADD();
		
		Node root = recursivelyDoOp(resultTree, aux, aux2, op);
		resultTree.setRoot(root);
		
		return resultTree;
	}
	
	public ADD setCostDiagram(int threshold, ADD[] diagrams)
	{
		ADD resultTree = new ADD();
		Node listNode = new ListNode(threshold, diagrams);

		Node root = recursivelyDoOp(resultTree, getRoot(), listNode, COST_SETTING);
		resultTree.setRoot(root);
		
		return resultTree;
	}
	
	// Fazer recursivo para maior eficiencia
	public ADD sumOut(int key)
	{
		List<Node> list = new ArrayList<Node>();
		List<Node> listP = new ArrayList<Node>();
		List<Integer> sides = new ArrayList<Integer>();
		list.add(getRoot());
		listP.add(null);
		sides.add(0);
		int indexVar = Order.getIndex(key);

		while(!list.isEmpty())
		{
			Node aux = list.remove(0);
			Node parent = listP.remove(0);
			Integer side = sides.remove(0);
			int indexAux = Order.getIndex(aux);
			if(indexAux > indexVar) continue;
			
			if(indexAux == indexVar)
			{			
				ADD resultTree = new ADD();
				Node root = recursivelyDoOp(resultTree, aux.getChild(0), aux.getChild(1), SUM);
				if(parent != null) parent.addChild(root, side);
				else setRoot(root);
			}
			else
			{
				for(int i = 0; i < Node.NUMBER_CHILDS; i++)
				{
					sides.add(i);
					list.add(aux.getChild(i));
					listP.add(aux);
				}	
			}
		}
		
		return this;
	}
	
	private Node lookUp(Node aux, Node aux2, int op)
	{
		Node result = null;
		// if(!aux.isTerminal() || aux.isTerminal() && aux2.isTerminal()) result = doOp(aux, aux2, op);
		if(!aux2.isTerminal()) result = doOp(aux2, aux, op);
		else result = doOp(aux, aux2, op);
		
		return result;
	}
	
	private Node doOp(Node aux, Node aux2, int op)
	{
		// Returns the node aux times aux2
		if(op == TIMES) return aux.times(aux2);
		else if(op == SUM) return aux.sum(aux2);
		else if(op == MAX) return aux.max(aux2);
		else if(op == MINUS) return aux.minus(aux2);
		
		return null;
	}
	
	public Node recursivelyDoOp(ADD resultTree, Node aux, Node aux2, int op)
	{
		// if one of the sides is zero
		if(op == TIMES)
		{
			if(aux.isTerminal() && ((NodeTerminal)aux).getValue() == 0) return aux;
			else if(aux2.isTerminal() && ((NodeTerminal)aux2).getValue() == 0) return aux2;
		}
		
		Node result = lookUp(aux, aux2, op);
		if(result != null) return result;
		
		if(aux.isTerminal() && aux2.isTerminal() && op == COST_SETTING) return ((ListNode)aux2).setCost(aux);
		Node parent = null;
		// Aux2 preceds aux
		if(Order.getIndex(aux2) < Order.getIndex(aux))
		{
			Node trade = aux;
			aux = aux2;
			aux2 = trade;
		}
		parent = aux.copy();
		// If both variables are equal then goes to child nodes of both trees
		if(aux.equals(aux2))
		{
			for(int i = 0; i < Node.NUMBER_CHILDS; i++)
			{
				Node child = recursivelyDoOp(resultTree, aux.getChild(i), aux2.getChild(i), op);
				resultTree.putNode(parent, child, i);
			}
		}
		else
		{
			for(int i = 0; i < Node.NUMBER_CHILDS; i++)
			{
				Node child = recursivelyDoOp(resultTree, aux.getChild(i), aux2, op);
				resultTree.putNode(parent, child, i);
			}	
		}
		Dictionary.putItem(aux, aux2, parent, op);
		
		return parent;
	}
	
	public double getMaxDifference(ADD tree2)
	{
		return recursivelyDoMaxDifference(getRoot(), tree2.getRoot());
	}
	
	// Add a dictonary to speed up operations
	public double recursivelyDoMaxDifference(Node aux, Node aux2)
	{
		double maxValue = -1;
		if(aux.isTerminal() && aux2.isTerminal()) maxValue = Math.max(maxValue, aux.absMinus(aux2));
		else
		{
			// Aux2 preceds aux
			if(Order.getIndex(aux2) < Order.getIndex(aux))
			{
				Node trade = aux;
				aux = aux2;
				aux2 = trade;
			}
			// If both variables are equal then goes to child nodes of both trees
			if(aux.equals(aux2))
			{
				for(int i = 0; i < Node.NUMBER_CHILDS; i++)
					maxValue = Math.max(maxValue, recursivelyDoMaxDifference(aux.getChild(i), aux2.getChild(i)));
			}
			else
			{
				for(int i = 0; i < Node.NUMBER_CHILDS; i++)
					maxValue = Math.max(maxValue, recursivelyDoMaxDifference(aux.getChild(i), aux2));
			}	
		}
		
		return maxValue;
	}
	
	public void recursivelyDoUnaryOp(Node aux, int op)
	{
		if(aux.isTerminal()) return;
		
		if(op == PRIME) aux.prime();
		for(int i = 0; i < Node.NUMBER_CHILDS; i++)
			recursivelyDoUnaryOp(aux.getChild(i), op);
	}
	
	public void prime()
	{
		recursivelyDoUnaryOp(getRoot(), PRIME);
	}
	
	public ADD order()
	{
		ADD resultTree = new ADD();
		Node[] list = new Node[Order.size()+1];
		int[] listB = new int[Order.size()+1];
		
		recursivelyDoOrdering(resultTree, list, listB, getRoot());
		
		return resultTree;
	}
	
	public void recursivelyDoOrdering(ADD resultTree, Node[] list, int[] listB, Node aux)
	{
		list[Order.getIndex(aux)] = aux;
		if(aux.isTerminal())
		{
			int count = 0;
			while(list[count] == null) count++;
			putNodes(resultTree, list, listB, count, null, 0, resultTree.getRoot());
			return;
		}
		
		for(int i = 0; i < Node.NUMBER_CHILDS; i++)
		{
			listB[Order.getIndex(aux.hashCode())] = i;
			recursivelyDoOrdering(resultTree, list, listB, aux.getChild(i));
		}
		
		list[Order.getIndex(aux)] = null;
		listB[Order.getIndex(aux)] = 0;
	}
	
	public void putNodes(ADD resultTree, Node[] list, int[] listB, int listIt, Node parent, int parentSide, Node aux)
	{
		if(listIt >= list.length) return;
		
		if(aux == null)
		{
			if(!list[listIt].isTerminal()) aux = list[listIt].copy();
			else aux = list[listIt];
			
			if(parent != null) parent.addChild(aux, parentSide);
			else resultTree.setRoot(aux);
		}
		
		if(Order.getIndex(aux) < listIt)
		{
			for(int i = 0; i < Node.NUMBER_CHILDS; i++) putNodes(resultTree, list, listB, listIt, aux, i, aux.getChild(i));
		}
		else if(Order.getIndex(aux) == listIt)
		{
			// Get next node
				listIt++;
				while(listIt < list.length && list[listIt] == null) listIt++;
			putNodes(resultTree, list, listB, listIt, aux, listB[Order.getIndex(aux)], aux.getChild(listB[Order.getIndex(aux)]));
		}
		else if(Order.getIndex(aux) > listIt)
		{
			Node copy = list[listIt].copy();
			if(parent != null) parent.addChild(copy, parentSide);
			else resultTree.setRoot(copy);
			copy.addChild(aux, 1-listB[listIt]);
			aux = copyTree(aux);
			copy.addChild(aux, listB[listIt]);
			
			// Get next node
				listIt++;
				while(listIt < list.length && list[listIt] == null) listIt++;
			for(int i = 0; i < Node.NUMBER_CHILDS; i++) putNodes(resultTree, list, listB, listIt, copy, i, copy.getChild(i));
		}
	}
	
	public Node copyTree(Node root)
	{
		Node newNode = root.copy();
		List<Node> list = new ArrayList<Node>();
		List<Node> listP = new ArrayList<Node>();
		listP.add(newNode);
		list.add(root);
		
		while(!list.isEmpty())
		{
			Node aux = list.remove(0);
			Node parent = listP.remove(0);
			
			for(int i = 0; i < Node.NUMBER_CHILDS; i++)
			{
				Node child = aux.getChild(i);
				if(child != null)
				{
					if(!child.isTerminal())
					{
						Node childCopy = child.copy();  
						parent.addChild(childCopy, i);						
						list.add(child);
						listP.add(childCopy);
					}	
					else parent.addChild(child, i);
				}
			}	
		}
		
		return newNode;
	}
	
	public void print()
	{
		recursivelyDoPrint(getRoot(), "", 1);
	}
	
	public void recursivelyDoPrint(Node aux, String prefix, int side)
	{
		
		System.out.println(prefix + (side==0 ? "|-- " : "|++ ") + aux.print());
		if(aux.isTerminal()) return;
        for (int i = 0; i < Node.NUMBER_CHILDS; i++)
            recursivelyDoPrint(aux.getChild(i), prefix + (side==1 ? "    " : "|   "), i);
	}
	
	public String printDistance(int distance)
	{
		String space = "";
		for(int i = 0; i < distance; i++) space += " ";
		
		return space;
	}

	/*public Node getNode(String name)
	{
		return nodes.get(name.hashCode());
	}
	
	public Node getNode(int code)
	{
		return nodes.get(code);
	}
	
	public Map<Integer, Node> getNodes()
	{
		return nodes;
	}
	
	public int nodesAmount()
	{
		return nodes.size();
	}*/
	
	/*public void setMinMax(ADD costTree)
	{	
		Node aux = getRoot();
		Node aux2 = costTree.getRoot();
		
		recursivelySetMinMax(aux, aux2);
	}
	
	// Aux2 must be into the same order of aux
	private void recursivelySetMinMax(Node aux, Node aux2)
	{
		if(aux.isTerminal())
		{
			// Returns the terminal node aux times aux2
			if(aux2.isTerminal())
			{
				aux.setMin(aux2.getValue());
				aux.setMax(aux2.getValue());
			}
			else recursivelySetMinMax(aux2, aux);
		}
		else
		{
			// If both variables are equal then goes to child nodes of both trees
			if(aux.equals(aux2))
			{
				for(int i = 0; i < Node.NUMBER_CHILDS; i++)
				{
					Node childAux = aux.getChild(i);
					recursivelySetMinMax(childAux, aux2.getChild(i));
					aux.setMin(childAux.getValue());
					aux.setMax(childAux.getValue());
				}
			}
			else
			{
				for(int i = 0; i < Node.NUMBER_CHILDS; i++)
				{
					Node childAux = aux.getChild(i);
					recursivelySetMinMax(childAux, aux2);
					aux.setMin(childAux.getValue());
					aux.setMax(childAux.getValue());
				}	
			}	
		}
	}*/
}
