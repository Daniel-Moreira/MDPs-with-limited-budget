package Solvers;

import ADD.*;
import Domain.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class SPUDD_FS extends Solver
{
	public SPUDD_FS(Domain d, int threshold)
	{
		domain = d;
		this.threshold = threshold;
		actions = domain.getActions();
		valueFunctions = new ADD[d.getHorizon()+1][threshold+1];
	}
	
	public void executa()
	{
		// Creates dual actions diagrams
		for(Acao a : actions) a.createDualDiagram();	
		// for(Acao a : actions) a.print();
		for(Acao a : actions) a.orderDiagrams();
		// for(Acao a : actions) a.print();
		
		// Building V(horizon, theta)
		constructCostDiagram(threshold, 0);
	}
	
	public void constructCostDiagram(int thresh, int horizon)
	{	
		List<ADD> valueA = new ArrayList<ADD>();
		// Value function is set following the cost diagram and previous valueFunctions
		for(Acao a : actions)
		{
			List<Integer> costs = a.getCostList();
			for(Integer cost : costs)
			{
				int value = thresh-cost;
				if(value < 0 || cost == 0) continue;
				if(valueFunctions[horizon][value] == null) constructCostDiagram(value, horizon+1);
			}
			
			ADD teste = a.getCost(thresh, valueFunctions[horizon+1]);
			for(int i = 0; i < a.sizeDiagrams(); i++)
			{
				ADD actionDiagram = a.getDiagram(i);
				// actionDiagram.print();
				// valueTemp.print();
				System.out.println("Multiplying... " + thresh + " " + horizon);
				teste = actionDiagram.op(teste, ADD.TIMES);
				System.out.println("Multiplyied " + thresh + " " + horizon);
				// valueTemp.print();
				teste = teste.sumOut(a.getVar(i));
				// valueTemp.print();
			}
			valueA.add(teste);
		}
		
		ADD vI = maxAll(valueA);
		System.out.println("Solving... " + thresh + " " + horizon);
		solve(thresh, horizon, vI);
		System.out.println("Solved " + thresh + " " + horizon);
	}
		
	public void print()
	{
		valueFunctions[0][threshold].print();
	}
}
