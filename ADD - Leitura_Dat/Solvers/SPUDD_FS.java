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
		valueFunctions = new ADD[threshold+2];
		valueFunctions[0] = d.getGoalState();
	}
	
	public void executa()
	{
		// Creates dual actions diagrams
		for(Acao a : actions) a.createDualDiagram();	
		// for(Acao a : actions) a.print();
		for(Acao a : actions) a.orderDiagrams();
		// for(Acao a : actions) a.print();
		
		// Building V(i)
		constructCostDiagram(threshold);
	}
	
	public void constructCostDiagram(int thresh)
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
				if(valueFunctions[value+1] == null) constructCostDiagram(value);
			}
			
			ADD teste = a.getCost(thresh, valueFunctions);
			for(int i = 0; i < a.sizeDiagrams(); i++)
			{
				ADD actionDiagram = a.getDiagram(i);
				// actionDiagram.print();
				// valueTemp.print();
				teste = actionDiagram.op(teste, ADD.TIMES);
				// valueTemp.print();
				teste.sumOut(a.getVar(i));
				// valueTemp.print();
			}
			valueA.add(teste);
		}
		
		ADD vI = maxAll(valueA);
		solve(thresh, vI);
	}
		
	public void print()
	{
		valueFunctions[threshold].print();
	}
}
