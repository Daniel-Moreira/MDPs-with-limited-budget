package Domain;

import Domain.Variables;
import java.util.Arrays;

public class State
{
	private int[] values;
	
	public State()
	{
		values = new int[Variables.size()];
		// for(int i = 0; i < values.length; i++) this.values[i] = -1;
	}
	
	public State(int[] values)
	{
		this.values = new int[Variables.size()];
		for(int i = 0; i < values.length; i++) this.values[i] = values[i];
	}
	
	public State copy()
	{
		return new State(values);
	}
	
	public int getIndex(String var)
	{
		return values[Variables.getPosition(var)];
	}
	
	public void setVar(String var, int value)
	{
		if(var.charAt(var.length()-1) == '\'') var = var.substring(0, var.length()-1);
		int i = Variables.getPosition(var);
		
		values[i] = value;
	}
	
	public int hashCode()
	{
		String code = "";
		
		// for(int i = 0; i < values.length; i++)
		// {
			// if(values[i] == 1) return i;
		// }	
		// return values.length;
		
		for(int i = 0; i < values.length; i++) code += values[i];	
		
		return code.hashCode();
	}
	
	public void print()
	{
		for(int i = 0; i < values.length; i++) if(values[i] == 1) System.out.print(i + " ");
		
		System.out.println(hashCode());
		System.out.println();
	}
}