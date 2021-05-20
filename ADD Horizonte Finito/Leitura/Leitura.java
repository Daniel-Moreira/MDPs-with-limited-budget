package Leitura;

import Domain.*;
import ADD.*;

import java.util.List;
import java.util.ArrayList;

public class Leitura
{
	private Arquivo a;
	private Domain d;
	
	public Leitura(Arquivo a)
	{
		this.a = a;
	}
	
	public Domain executa()
	{
		d = new Domain();
		
		leVariaveis();
		leEstadoInicial();
		leAcoes();
		leHorizonte();
		
		return d;
	}
	
	public void leHorizonte()
	{
		String[] linha;
		for(linha = a.leLinha().trim().split(" "); !linha[0].equals("horizon"); linha = a.leLinha().trim().split(" "));
		
		d.setHorizonte(Integer.parseInt(linha[1]));
	}
	
	public void leVariaveis()
	{
		for(String linha = a.leLinha().trim(); !linha.equals("(variables"); linha = a.leLinha().trim());
		
		String[] linha = a.leLinha().trim().split(" ");
		List<Node> list = new ArrayList<Node>();
		List<Node> listLine = new ArrayList<Node>();
		do
		{
			String variable = linha[0].substring(1, linha[0].length());
			// System.out.println(variable + ".");
			list.add(new Node(variable));
			listLine.add(new Node(variable+"'"));
			linha = a.leLinha().trim().split(" ");
		} while(!linha[0].trim().equals(")"));
		
		list.addAll(listLine);
		Order.setOrder(list);
	}

	public void leEstadoInicial()
	{
		for(String linha = a.leLinha().trim(); !linha.equals("init [*"); linha = a.leLinha().trim()) ;
		
		String[] linha = a.leLinha().trim().split(" ");

		Node parent = new Node(linha[0].substring(1, linha[0].length()));
		int side = 1-Character.getNumericValue(linha[2].charAt(1));
		ADD initialState = new ADD(parent);
		
		Node zero = new NodeTerminal(0.0);
		Node one = new NodeTerminal(1.0);
		for(linha = a.leLinha().trim().split(" "); !linha[0].trim().equals("]"); linha = a.leLinha().trim().split(" "))
		{
			String variable = linha[0].substring(1, linha[0].length());
			Node n = new Node(variable);
			
			initialState.putNode(parent, n, side);
			initialState.putNode(parent, zero, 1-side);
			
			parent = n;
			side = 1-Character.getNumericValue(linha[2].charAt(1));
		}
		
		initialState.putNode(parent, one, side);
		initialState.putNode(parent, zero, 1-side);
		d.setInitialState(initialState);
		
		// initialState.print();
	}
	
	public void leAcoes()
	{
		a.leLinha();
		
		String[] linha = a.leLinha().trim().split(" ");
		while(linha[0].equals("action"))
		{			
			String nomeAcao = linha[1];
			
			Acao acao = new Acao(nomeAcao);
			
			for(linha = a.leLinha().trim().split(" "); !linha[0].equals("cost"); linha = a.leLinha().trim().split(" "))
			{			
				String parentName = linha[0];
				Node parent = new Node(parentName);
				ADD diagram = new ADD(parent);
				
				linha = a.leLinha().trim().split(" ");
				linha[0] = linha[0].substring(1, linha[0].length());
				Node n = new Node(linha[0]);
				// if(n.hashCode() == parent.hashCode())
				// {
					// for(int i = Node.NUMBER_CHILDS-1; i >= 0 ; i--) readRecur(parent.hashCode(), parent, i, i);
					// continue;
				// }
				for(int i = Node.NUMBER_CHILDS-1; i >= 0 ; i--) readRecur(parent.hashCode(), n, i, 1);
				
				parent.addChild(n, 1);

				acao.addDiagram(diagram, parentName.hashCode());
			}
			
			readCost(acao);
			a.leLinha();
			a.leLinha();
		
			linha = a.leLinha().trim().split(" ");
			
			d.addAction(acao);
		}
	}
	
	private void readCost(Acao acao)
	{	
		ADD cost = null;
		for(String[] linha = a.leLinha().trim().split(" "); !linha[0].equals("]"); linha = a.leLinha().trim().split(" "))
		{			
			String parentName = linha[0].substring(1, linha[0].length());
			Node parent = new Node(parentName);
			ADD diagram = new ADD(parent);
			
			for(int i = Node.NUMBER_CHILDS-1; i >= 0 ; i--) readRecur(parent.hashCode(), parent, i, 1);

			if(cost == null) cost = diagram;
			else cost = cost.op(diagram, ADD.SUM);
		}
		
		acao.addCostDiagram(cost);
		acao.setCostList(cost.getList());
	}	
	
	private void readRecur(int root, Node parent, int side, int put)
	{
		String l = a.leLinha();
		String[] linha = l.trim().split(" ");
		linha[1] = linha[1].substring(1, linha[1].length());
		
		Node n = new Node(linha[1]);
		if(Character.getNumericValue(linha[1].charAt(0)) == 0 || Character.getNumericValue(linha[1].charAt(0)) == 1)
			n = new NodeTerminal(Double.parseDouble(linha[1].replaceAll("[()]", "")));

		if(n.isTerminal())
		{	
			if(put == 1) parent.addChild(n, side);
			return;
		}	
		// if(n.hashCode() == root)
		// {
			// for(int i = Node.NUMBER_CHILDS-1; i >= 0 ; i--) readRecur(root, parent, i, i);
			// return;
		// }	
		if(put == 1) parent.addChild(n, side);
		
		for(int i = Node.NUMBER_CHILDS-1; i >= 0 ; i--) readRecur(root, n, i, put);
	}
}