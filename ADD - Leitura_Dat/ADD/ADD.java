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

	// Op with a scalar
	public ADD op(double value, int op)
	{
		Node aux = getRoot();
		Node aux2 = new NodeTerminal(value);
		
		Node root = recursivelyDoOp(aux, aux2, op);
		ADD resultTree = new ADD(root);
		
		return resultTree;
	}
	
	public ADD op(ADD tree2, int op)
	{
		Node aux = getRoot();
		Node aux2 = tree2.getRoot();
		
		Node result = Dictionary.getItem(aux, aux2, Dictionary.TOP);
		if(result != null) return new ADD(result);
		
		Node root = recursivelyDoOp(aux, aux2, op);
		ADD resultTree = new ADD(root);
		
		Dictionary.putItem(aux, aux2, root, Dictionary.TOP);
		
		return resultTree;
	}
	
	public ADD setCostDiagram(int threshold, ADD[] diagrams)
	{
		Node listNode = new ListNode(threshold, diagrams);

		Node root = recursivelyDoOp(getRoot(), listNode, COST_SETTING);
		ADD resultTree = new ADD(root);
		
		return resultTree;
	}
	
	public void sumOut(int key)
	{
		sumOutR(null, getRoot(), Order.getIndex(key));
	}
	
	public void sumOutR(Node parent, Node aux, int indexVar)
	{
		int indexAux = Order.getIndex(aux);
		if(indexAux > indexVar) return;
		
		Node result = Dictionary.getItem(aux, indexVar, Dictionary.SUMOUT);
		if(result != null)
		{
			if(parent != null)
			{
				int side = 0;
				if(parent.getChild(1) == aux) side = 1;
				parent.addChild(result, side);
			}
			else setRoot(result); 
			return;
		}
		
		if(indexAux == indexVar)
		{
			Node root = recursivelyDoOp(aux.getChild(0), aux.getChild(1), SUM);
			
			Dictionary.putItem(aux, indexVar, root, Dictionary.SUMOUT);
			if(parent != null)
			{
				int side = 0;
				if(parent.getChild(1) == aux) side = 1;
				parent.addChild(root, side);
			}	
			else setRoot(root);
		}
		else
		{
			aux.resetCode();
			for(int i = 0; i < Node.NUMBER_CHILDS; i++)	sumOutR(aux, aux.getChild(i), indexVar);
			
			Node aux2 = reduce(aux);
			
			if(aux2 != aux)
			{
				if(parent == null) setRoot(aux2);
				else
				{					
					int side = 0;
					if(parent.getChild(1) == aux) side = 1;
					parent.addChild(aux2, side);
				}
			}
			
			Dictionary.putItem(aux, indexVar, aux2, Dictionary.SUMOUT);
		}
	}
	
	private Node lookUp(Node aux, Node aux2, int op)
	{
		Node result = null;
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
	
	private Node recursivelyDoOp(Node aux, Node aux2, int op)
	{
		Node result = lookUp(aux, aux2, op);
		if(result != null) return result;
	
		// Put this in a method
		if(op == TIMES)
		{
			// if one of the sides is zero
			if(aux.isTerminal() && ((NodeTerminal)aux).getValue() == 0) return aux;
			else if(aux2.isTerminal() && ((NodeTerminal)aux2).getValue() == 0) return aux2;

			// Transform the double values into bigDecimals??
			// if one of the sides is one
			else if(aux.isTerminal() && Double.compare(((NodeTerminal)aux).getValue(), 1.0) == 0) return aux2;
			else if(aux2.isTerminal() &&  Double.compare(((NodeTerminal)aux2).getValue(), 1.0) == 0) return aux;
		}
		else if(op == SUM)
		{
			// if one of the sides is zero
			if(aux.isTerminal() && ((NodeTerminal)aux).getValue() == 0) return aux2;
			else if(aux2.isTerminal() && ((NodeTerminal)aux2).getValue() == 0) return aux;
		}
		
		if(aux.isTerminal() && aux2.isTerminal() && op == COST_SETTING) return ((ListNode)aux2).setCost(aux);
		Node parent = null;
		// Aux2 preceds aux
		
		// If both variables are equal then goes to child nodes of both trees
		if(aux.equals(aux2))
		{
			parent = aux.copy();
			for(int i = 0; i < Node.NUMBER_CHILDS; i++)
			{
				Node child = recursivelyDoOp(aux.getChild(i), aux2.getChild(i), op);
				parent.addChild(child, i);
			}
		}
		else if(Order.getIndex(aux2) > Order.getIndex(aux))
		{
			parent = aux.copy();
			for(int i = 0; i < Node.NUMBER_CHILDS; i++)
			{
				Node child = recursivelyDoOp(aux.getChild(i), aux2, op);
				parent.addChild(child, i);
			}
		}
		else if(Order.getIndex(aux2) < Order.getIndex(aux))
		{
			parent = aux2.copy();
			for(int i = 0; i < Node.NUMBER_CHILDS; i++)
			{
				Node child = recursivelyDoOp(aux, aux2.getChild(i), op);
				parent.addChild(child, i);
			}
		}
		parent = reduce(parent);
		Dictionary.putItem(aux, aux2, parent, op);
		
		return parent;
	}
	
	public void reduceAll(Node parent, Node aux)
	{
		if(aux.isTerminal()) return;
		
		aux.resetCode();
		for(int i = 0; i < Node.NUMBER_CHILDS; i++) reduceAll(aux, aux.getChild(i));
		
		Node n = reduce(aux);
		if(n != aux)
		{
			int side = 0;
			if(parent.getChild(1) == aux) side = 1;
			parent.addChild(n, side);
		}
	}
	
	// Return just one child if both subTrees are equal
	public Node reduce(Node node)
	{
		// System.out.println(node.getChild(0).print() + " " + node.getChild(0).treeHashCode() + " " + node.getChild(1).print() + " " + node.getChild(1).treeHashCode());
		if(node.getChild(0).treeHashCode() == node.getChild(1).treeHashCode()) return node.getChild(0);
		
		return node;
	}
	
	// Max(abs(ADD_1-ADD_2))
	public double getMaxDifference(ADD tree2)
	{
		return recursivelyDoMaxDifference(getRoot(), tree2.getRoot());
	}
	
	// Add a dictonary to speed up operations
	private double recursivelyDoMaxDifference(Node aux, Node aux2)
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
	
	public List<Integer> getList()
	{
		Node root = getRoot();
		List<Integer> list = new ArrayList<Integer>();
		
		recursivelyGetList(root, list);
		
		return list;
	}
	
	private void recursivelyGetList(Node aux, List<Integer> list)
	{
		if(aux.isTerminal())
			list.add((int)Math.round(((NodeTerminal)aux).getValue()));
		else 
			for(int i = 0; i < Node.NUMBER_CHILDS; i++)	recursivelyGetList(aux.getChild(i), list);
	}
	
	public void prime()
	{
		recursivelyDoUnaryOp(getRoot(), PRIME);
	}
	
	private void recursivelyDoUnaryOp(Node aux, int op)
	{
		if(aux.isTerminal()) return;
		
		if(op == PRIME) aux.prime();
		for(int i = 0; i < Node.NUMBER_CHILDS; i++) recursivelyDoUnaryOp(aux.getChild(i), op);
	}

	// Ordering
	public ADD order()
	{	
		recursivelyDoOrdering(null, getRoot());
		
		return this;
	}
	
	private int recursivelyDoOrdering(Node parent, Node aux)
	{
		int lowIndex = Order.size();
		if(aux.isTerminal()) return lowIndex;
		
		for(int i = 0; i < Node.NUMBER_CHILDS; i++) lowIndex = (int) Math.min(lowIndex, recursivelyDoOrdering(aux, aux.getChild(i)));
	
		int auxIndex = Order.getIndex(aux);
		if(auxIndex < lowIndex) lowIndex = auxIndex;
		else
		{
			Node[] highLow = new Node[2];
			for(int i = 0; i < Node.NUMBER_CHILDS; i++)
			{
				// Build ADD with var and terminal nodes 0 and 1 or 1 and 0
					Node var = setHighLow(aux, i);
				// Make multiplication
				highLow[i] = recursivelyDoOp(var, aux.getChild(i), TIMES);
			}
			
			Node child = recursivelyDoOp(highLow[0], highLow[1], SUM);
			
			if(parent == null) setRoot(child);
			else
			{
				int side = 0;
				if(parent.getChild(1) == aux) side = 1;
				parent.addChild(child, side);
			}
		}
		
		return lowIndex;
	}
	
	public Node setHighLow(Node aux, int side)
	{
		Node newNode = aux.copy();
		Node one = new NodeTerminal(1.0);
		Node zero = new NodeTerminal(0.0);
		
		newNode.addChild(zero, 1-side);
		newNode.addChild(one, side);
		
		return newNode;
	}
	
	public ADD order2()
	{
		ADD resultTree = new ADD();
		Node[] list = new Node[Order.size()+1];
		int[] listB = new int[Order.size()+1];
		
		recursivelyDoOrdering2(resultTree, list, listB, getRoot());
		
		return resultTree;
	}
	
	private void recursivelyDoOrdering2(ADD resultTree, Node[] list, int[] listB, Node aux)
	{
		list[Order.getIndex(aux)] = aux;
		if(aux.isTerminal())
		{
			int count = 0;
			while(list[count] == null) count++;
			putNodes(resultTree, list, listB, count, null, 0, resultTree.getRoot());
		}
		else
		{
			for(int i = 0; i < Node.NUMBER_CHILDS; i++)
			{
				listB[Order.getIndex(aux)] = i;
				recursivelyDoOrdering2(resultTree, list, listB, aux.getChild(i));
			}
		}
		
		list[Order.getIndex(aux)] = null;
		listB[Order.getIndex(aux)] = 0;
	}
	
	private void putNodes(ADD resultTree, Node[] list, int[] listB, int listIt, Node parent, int parentSide, Node aux)
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
			copy.addChild(aux, listB[listIt]);
			aux = copyTree(aux);
			copy.addChild(aux, 1-listB[listIt]);
			
			// Get next node
				listIt++;
				while(listIt < list.length && list[listIt] == null) listIt++;
			putNodes(resultTree, list, listB, listIt, copy, listB[Order.getIndex(copy)], copy.getChild(listB[Order.getIndex(copy)]));
			// else for(int i = 0; i < Node.NUMBER_CHILDS; i++) putNodes(resultTree, list, listB, listIt, copy, i, copy.getChild(i));
		}
	}
	
	// Copy a tree
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
	
	// Print
	public void print()
	{
		recursivelyDoPrint(getRoot(), "", 1);
	}
	
	public void print2()
	{
		recursivelyDoPrint2(getRoot(), "", 1, 2);
	}
	
	public void recursivelyDoPrint(Node aux, String prefix, int side)
	{
		System.out.println(prefix + (side==0 ? "|-- " : "|++ ") + aux.print());
		if(aux.isTerminal()) return;
        for (int i = 0; i < Node.NUMBER_CHILDS; i++)
            recursivelyDoPrint(aux.getChild(i), prefix + (side==1 ? "    " : "|   "), i);
	}
	
	public void recursivelyDoPrint2(Node aux, String prefix, int side, int size)
	{
		if(size == 1)
		{
			if(aux.isTerminal()) System.out.println(prefix + "|++ " + aux.print());
			else recursivelyDoPrint2(aux.getChild(0), prefix, 0, size);
			
			return;
		}
		System.out.println(prefix + (side==0 ? "|-- " : "|++ ") + aux.print());
		if(aux.isTerminal()) return;
        for (int i = 0; i < size; i++)
            recursivelyDoPrint2(aux.getChild(i), prefix + (side==1 ? "    " : "|   "), i, size-i);
	}
	
	public String printDistance(int distance)
	{
		String space = "";
		for(int i = 0; i < distance; i++) space += " ";
		
		return space;
	}
}
