package Solvers;

import Domain.Domain;
import Domain.Acao;
import ADD.ADD;
import java.util.List;
import java.util.ArrayList;

public abstract class Solver
{
	protected final double EPSILON = Math.pow(10, -6);
	
	protected Domain domain;
	protected ADD[] valueFunctions;
	protected List<Acao> actions;
	protected int threshold;
	
	public abstract void executa();
	public abstract void print();
	
	// Value Iteration
	protected void solve(int thresh, ADD vI)
	{
		double error = 0;
		
		do
		{
			List<ADD> valueA = new ArrayList<ADD>();
			for(Acao a : actions)
			{
				ADD valueTemp = vI;
				for(int i = 0; i < a.sizeDiagrams(); i++)
				{
					ADD actionDiagram = a.getDiagram(i);
					// actionDiagram.print();
					// valueTemp.print();
					valueTemp = actionDiagram.op(valueTemp, ADD.TIMES);
					// valueTemp.print();
					valueTemp = valueTemp.sumOut(a.getVar(i));
					// valueTemp.print();
				}
				valueA.add(valueTemp);
			}
			ADD vIPlusOne = maxAll(valueA);
			error = vIPlusOne.getMaxDifference(vI);
			vI = vIPlusOne;
		} while(error > EPSILON);
		valueFunctions[thresh] = vI;
	}
	
	protected ADD maxAll(List<ADD> diagrams)
	{
		ADD aux = diagrams.remove(0);
		for(ADD d : diagrams) aux = aux.op(d, ADD.MAX);
		
		return aux;
	}
}
