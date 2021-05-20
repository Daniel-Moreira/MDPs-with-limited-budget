package Leitura;

import Domain.*;
import ADD.*;

import java.util.List;
import java.util.ArrayList;

public class Leitura
{
	private final int REWARD = 0;
	private final int PROB = 1;
	
	private FileTokenizer f;
	private Domain d;
	
	public Leitura(FileTokenizer f)
	{
		this.f = f;
		d = new Domain();
	}
	
	public Domain createDomain()
	{
		for(String token = f.nextToken(); token != null; token = f.nextToken())
		{			
			if(token.equals("variables")) readVariables();
			else if(token.equals("action")) readAction();
			else if(token.equals("reward")) readReward();
			// else if(token.equals("cost")) readCost();
			else if(token.equals("initial")) readInitialState();
			else if(token.equals("goal")) readGoal();
		}
		
		return d;
	}	
	
	private void readVariables()
	{
		// Should return a error
		if(!f.nextToken().equals("(")) System.out.println("Error: the symbol '(' was expected!");
		
		List<Node> list = new ArrayList<Node>();
		List<Node> listLine = new ArrayList<Node>();
		for(String variable = f.nextToken(); !variable.equals(")"); variable = f.nextToken())
		{
			// System.out.println(variable + ".");
			list.add(new Node(variable));
			listLine.add(new Node(variable, true));
		}
		
		Variables.setVars(list);
		list.addAll(listLine);
		Order.setOrder(list);
		// listLine.addAll(list);
		// Order.setOrder(listLine);
	}
	
	private void readAction()
	{	
		String actionName = f.nextToken();
		
		Acao acao = new Acao(actionName);
			
		for(String variable = f.nextToken(); !variable.equals("endaction"); variable = f.nextToken())
		{
			String parentName = variable;
			Node parent = new Node(parentName, true);
			ADD diagram = new ADD(parent);
			
			// System.out.println(parentName);
			// Should return a error
			if(!f.nextToken().equals("(")) System.out.println("Error: the symbol '(' was expected!");

			readChild(parent, 1, PROB);
			
			acao.addDiagram(diagram, parent.hashCode());
			
			// System.out.println();
		}
		
		d.addAction(acao);
	}
	
	private void readChild(Node parent, int side, int type)
	{		
		String childName = f.nextToken();
		if(childName.equals(")")) return;
		
		Node child;
		if(f.isNumber())
		{
			double number = f.getNumber();
			if(type == REWARD) number = -number;
			child = new NodeTerminal(number);		
		}
		else child = new Node(childName);
			
		parent.addChild(child, side);
		
		// System.out.println(child.print());
		int count = 1;
		for(String token = f.nextToken(); token.equals("("); token = f.nextToken()) readChild(child, count--, type);
	}

	
	private void readReward()
	{
		// Should return a error
		if(!f.nextToken().equals("(")) System.out.println("Error: the symbol '(' was expected!");
		
		Node parent = new Node("empty");
		
		readChild(parent, 1, REWARD);
		ADD cost = new ADD(parent.getChild(1));
		
		// cost.print();
		for(Acao acao : d.getActions())
		{
			acao.addCostDiagram(cost);
			acao.setCostList(cost.getList());
		}
	}
	
	private void readInitialState()
	{
		// Should return a error
		if(!f.nextToken().equals("(")) System.out.println("Error: the symbol '(' was expected!");
		
		Node parent = new Node("empty");
		
		readChild(parent, 1, PROB);
		ADD initialState = new ADD(parent.getChild(1));
		
		d.setInitialState(initialState);
		// Discard 'end initial'
		f.nextToken();
	}
	
	private void readGoal()
	{
		// Should return a error
		if(!f.nextToken().equals("(")) System.out.println("Error: the symbol '(' was expected!");
		
		Node parent = new Node("empty");
		// String parentName = f.nextToken();
		// Node parent = new Node(parentName);
		
		readChild(parent, 1, PROB);
		ADD goalState = new ADD(parent.getChild(1));

		d.setGoalState(goalState);
		
		// Discard 'end initial'
		f.nextToken();
	}
}