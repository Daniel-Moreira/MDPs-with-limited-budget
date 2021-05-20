package Leitura;

import Domain.*;
import ADD.*;

import java.util.List;
import java.util.ArrayList;

public class Leitura
{
	private Arquivo arquivo;
	private Domain d;
	
	public Leitura(Arquivo a)
	{
		arquivo = a;
	}
	
	public Domain executa()
	{
		d = new Domain();
		
		leAcoes();
		leCustos();
		leEstadoInicial();
		leEstadoObjetivo();
		
		return d;
	}
	
	// Adicionar variavel prime como root
	public void leAcoes()
	{
		List<Node> nodes = new ArrayList<Node>();
		
		Acao a = new Acao("a1");
		
		Node x = new Node("x");
		Node xLine = new Node("x'");
		Node y = new Node("y");
		Node one = new NodeTerminal(1.0);
		Node zeroThree = new NodeTerminal(0.3);
		
		ADD diagram = new ADD(xLine);
		diagram.putNode(xLine, x, 1);
		diagram.putNode(x, y, 1);
		diagram.putNode(y, one, 1);
		diagram.putNode(x, one, 0);
		diagram.putNode(y, zeroThree, 0);
		
		a.addDiagram(diagram, "x'".hashCode());
		
		x = new Node("x");
		y = new Node("y");
		Node yLine = new Node("y'");
		Node zero = new NodeTerminal(0.0);
		Node zeroSeven = new NodeTerminal(0.7);
		
		diagram = new ADD(yLine);
		diagram.putNode(yLine, y, 1);
		diagram.putNode(x, zeroSeven, 1);
		diagram.putNode(x, zero, 0);
		diagram.putNode(y, x, 0);
		diagram.putNode(y, one, 1);
		
		a.addDiagram(diagram, "y'".hashCode());
		
		d.addAction(a);

			nodes.add(x);
			nodes.add(y);
			nodes.add(xLine);
			nodes.add(yLine);
			Order.setOrder(nodes);
			
		a = new Acao("a2");
		
		x = new Node("x");
		xLine = new Node("x'");
		y = new Node("y");
		one = new NodeTerminal(1.0);
		zero = new NodeTerminal(0.0);
		Node zeroFive = new NodeTerminal(0.5);
		
		diagram = new ADD(xLine);
		diagram.putNode(xLine, x, 1);
		diagram.putNode(x, y, 1);
		diagram.putNode(y, one, 1);
		diagram.putNode(y, zero, 0);
		y = new Node("y");
		diagram.putNode(x, y, 0);
		diagram.putNode(y, one, 1);
		diagram.putNode(y, zeroFive, 0);
		
		a.addDiagram(diagram, "x'".hashCode());
		
		x = new Node("x");
		y = new Node("y");
		yLine = new Node("y'");
		
		diagram = new ADD(yLine);
		diagram.putNode(yLine, x, 1);
		diagram.putNode(x, one, 1);
		diagram.putNode(x, y, 0);
		diagram.putNode(y, one, 1);
		diagram.putNode(y, zero, 0);
		
		a.addDiagram(diagram, "y'".hashCode());
		
		d.addAction(a);
	}
	
	/*
			x
		1		y
			  2	  0
	*/
	public void leCustos()
	{
		for(Acao a : d.getActions())
		{
			if(a.getName().equals("a1"))
			{				
				Node x = new Node("x");
				Node y = new Node("y");
				Node zero = new NodeTerminal(0.0);
				Node one = new NodeTerminal(1.0);
				Node two = new NodeTerminal(2.0);
				
				ADD cost = new ADD(x);
				cost.putNode(x, one, 0);
				cost.putNode(x, y, 1);
				cost.putNode(y, two, 0);
				cost.putNode(y, zero, 1);
				
				a.addCostDiagram(cost);
				
				List<Integer> costs = new ArrayList<Integer>(); 
				costs.add(0);
				costs.add(1);
				costs.add(2);
				a.setCostList(costs);
			}
			else
			{
				Node x = new Node("x");
				Node y = new Node("y");
				Node zero = new NodeTerminal(0.0);
				Node one = new NodeTerminal(1.0);
				Node two = new NodeTerminal(2.0);
				
				ADD cost = new ADD(x);
				cost.putNode(x, y, 1);
				cost.putNode(y, one, 0);
				cost.putNode(y, zero, 1);
				y = new Node("y");
				cost.putNode(x, y, 0);
				cost.putNode(y, two, 0);
				cost.putNode(y, one, 1);
				
				a.addCostDiagram(cost);
				
				List<Integer> costs = new ArrayList<Integer>(); 
				costs.add(0);
				costs.add(1);
				costs.add(2);
				a.setCostList(costs);
			}
		}
	}
	
	public void leEstadoInicial()
	{
		d.setInitialState("~x~y");
	}
	
	public void leEstadoObjetivo()
	{
		d.addTerminalState("xy");
	}
}