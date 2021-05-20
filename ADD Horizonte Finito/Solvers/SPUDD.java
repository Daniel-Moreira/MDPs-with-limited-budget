package Solvers;

import ADD.*;
import Domain.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class SPUDD extends Solver
{
	public SPUDD(Domain d, int threshold)
	{
		domain = d;
		this.threshold = threshold;
		actions = domain.getActions();
		valueFunctions = new ADD[threshold+1][d.getHorizon()];
	}
	
	public void executa()
	{
		double error = 0;
		
		// Creates dual actions diagrams
		for(Acao a : actions) a.createDualDiagram();	
		// for(Acao a : actions) a.print();
		for(Acao a : actions) a.orderDiagrams();
		// for(Acao a : actions) a.print();
		
		for(int iThresh = 0; iThresh <= threshold; iThresh++)
		{
			for(int horizon = d.getHorizon-1; horizon >= 0; i--)
			{			
				// Building V(i, 0)
				List<ADD> valueA = new ArrayList<ADD>();
				// Value function is set following the cost diagram and previous valueFunctions
				for(Acao a : actions)
				{
					ADD teste = a.getCost(iThresh, valueFunctions);
					for(int i = 0; i < a.sizeDiagrams(); i++)
					{
						ADD actionDiagram = a.getDiagram(i);
						// actionDiagram.print();
						// valueTemp.print();
						teste = actionDiagram.op(teste, ADD.TIMES);
						// valueTemp.print();
						teste = teste.sumOut(a.getVar(i));
						// valueTemp.print();
					}
					valueA.add(teste);
				}	
				
				ADD vI = maxAll(valueA);
				// Value Iteration
				// vI.print();
				solve(iThresh, vI);
			}
		}
	}
	
	public void print()
	{
		for(int i = 0; i < valueFunctions.length; i++) valueFunctions[i].print();
	}
}
