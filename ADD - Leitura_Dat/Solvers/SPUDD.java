package Solvers;

import ADD.*;
import Domain.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import java.util.Scanner;

public class SPUDD extends Solver
{
	public SPUDD(Domain d, int threshold)
	{
		domain = d;
		this.threshold = threshold;
		actions = domain.getActions();
		valueFunctions = new ADD[threshold+2];
		valueFunctions[0] = d.getGoalState();
		valueFunctions[0].setRoot(valueFunctions[0].order().getRoot());
		// valueFunctions[0].print();
		for(Acao a : actions) a.createDualDiagram();	
		for(Acao a : actions) a.orderDiagrams();
	}
	
	double cost = 0;
	double sumOut = 0;
	double times = 0;
	int amount = 0;
	int convergency = 0;
	public void executa()
	{
		double error = 0;
		
		// ADD r = defineRestriction();
		// Creates dual actions diagrams
		// for(Acao a : actions) a.print();
		// Scanner sc = new Scanner(System.in);

		int maximumCost = 1;
		// for(Acao a : actions) a.print();
		Cronometro2 cT = new Cronometro2();
		for(int iThresh = 0; iThresh <= threshold; iThresh++)
		{
			// Check for convergency
				// If didn't have any difference in n+1 diagrams where n is the maximumCost of the problem
				if(iThresh > maximumCost && valueFunctions[iThresh-maximumCost].getRoot().treeHashCode() == valueFunctions[iThresh].getRoot().treeHashCode())
				{
					if(convergency == 0) convergency = iThresh-1;
					System.out.println(convergency);
					/*for(; iThresh <= threshold; iThresh++)
					{
						valueFunctions[iThresh+1] = valueFunctions[iThresh];
						amount++;
					}*/
					break;
				}
			// Building V(i, 0)
			List<ADD> valueA = new ArrayList<ADD>();
			// Value function is set following the cost diagram and previous valueFunctions
			for(Acao a : actions)
			{
				// System.out.println(a.getName());
				cT.start();
				ADD teste = a.getCost(iThresh, valueFunctions);
				cT.stop();
				
				cost += cT.getTempo();
				// Scanner sc = new Scanner(System.in);
				// teste.print();
				// sc.next();
				// teste.setRoot(teste.order().getRoot());
				// if(iThresh==2) teste.print();
				// sc.next();
				// teste.print();
				for(int i = 0; i < a.sizeDiagrams(); i++)
				{
					ADD actionDiagram = a.getDiagram(i);
					// if(iThresh==2) actionDiagram.print();
					// actionDiagram.print();
					// teste.print();
					cT.start();
					// if(iThresh == 10) teste = actionDiagram.op2(teste, ADD.TIMES);
					// else teste = actionDiagram.op(teste, ADD.TIMES);
					teste = actionDiagram.op(teste, ADD.TIMES);
					cT.stop();
				
					times += cT.getTempo();
					// if(iThresh==2) teste.print();
					// teste.print();
					cT.start();
					teste.sumOut(a.getVar(i));
					cT.stop();
				
					sumOut += cT.getTempo();
					// teste.print();
					// sc.next();
					// if(iThresh==2) teste.print();
					// if(iThresh==2) sc.next();
					
					// System.out.println();
					// System.out.println();
					// System.out.println();
				}
				
				// teste.print();
				valueA.add(teste);
				// teste.print();
			}
			
			// if(iThresh==2) System.out.println("STOP");
			// if(iThresh==2) for(ADD lala : valueA) lala.print(); 
			// if(iThresh==2) sc.next();
			ADD vI = maxAll(valueA);
			// vI = r.op(vI, ADD.TIMES);
			// Value Iteration
			// vI.print();
			solve(iThresh, vI);
		}
	}
	
	public void print()
	{
		for(int i = 1; i < valueFunctions.length; i++) valueFunctions[i].print();
		// for(int i = 1; i < valueFunctions.length; i++) valueFunctions[i].print2();
		System.out.println("Convergency point: " + convergency);
		System.out.println("Amount not calculated: " + amount);
		System.out.println("Amount multiplication not calculated: " + Dictionary.amount);
		System.out.println("Cache size of multiplication: " + Dictionary.times.size());
		System.out.println("Time expended setting cost: " + cost + " ms");
		System.out.println("Time expended summing out: " + sumOut + " ms");
		System.out.println("Time expended multiplying: " + times + " ms");
	}
}
