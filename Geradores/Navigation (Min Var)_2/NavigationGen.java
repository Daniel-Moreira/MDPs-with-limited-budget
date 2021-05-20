import java.io.*;
import java.util.*;
	
	// y1 y2 y3
	// 1  0  0   =	4

public class NavigationGen
{
	private static final String NUMBER_REGEX = "^[0-9]+$";
	private static final String ZERO = "[0.0]";
	private static final String ONE = "[1.0]";
	private static final String VARIABLE_MASK_X = "x%d"; //e.g. x1
	private static final String VARIABLE_MASK_Y = "y%d"; //e.g. y1
	private static final String VARIABLE_MASK_D = "d";
	private static final String TREE = "%1$s (%2$s) (%3$s)";
	private static final String TREE_2 = "%1$s (%2$s) (%2$s)";
	private static final String TRANSITION = "%1$s (%2$s)";
	
	private static final double minProbOfDisappear = 0.1;
	private static final double maxProbOfDisappear = 0.9;
	
	private static int numberOfColumns; 
	private static int numberOfLines;	
	
	public static void main(String[] args)
	{		
		String outputFilePath = args[0];
		String numberOfLinesAsString = args[1];
		String numberOfColumnsAsString = args[2];
		
		if (!numberOfColumnsAsString.matches(NUMBER_REGEX) || !numberOfLinesAsString.matches(NUMBER_REGEX))
		{
			System.out.println("The number of rows and columns must be numbers !");
			return;
		}
		
		numberOfColumns = Integer.parseInt(numberOfColumnsAsString);
		numberOfLines = Integer.parseInt(numberOfLinesAsString);
		
		File outputFile = new File(outputFilePath);
		
		if (outputFile.exists())
			System.out.println("The output file exists. The file will be overrided.");
		
		generateDomainFile(outputFile);
	}
	
	static void help()
	{
		System.out.println("To use this generator use the parameters:");
		System.out.println("output-file number-of-rows number-of-columns");
	}
	
	private static void generateDomainFile(File outputFile)
	{
		String variables = generateVariables();
		
		HashMap<String, List<String>> actions = generateActions();
		
		String cost = generateCost();
		
		String initialState = "(" + generateInitialState() + ")";
		
		String goalState = "(" + generateGoalState() + ")";
		
		generateFormattedDomainFile(outputFile, variables, actions, cost, initialState, goalState);
	}

	private static void generateFormattedDomainFile(File outputFile,
		String variables, HashMap<String, List<String>> actions, String cost, String initialState, String goalState)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
			
			writer.write("variables ");	
			writer.write(String.format("(%s)", variables));
			writer.newLine();
			
			for (String action : actions.keySet())
			{
				writer.write("action " + action);
				writer.newLine();
				
				List<String> adds = actions.get(action);
				
				for (String add : adds)
				{
					writer.write("\t" + add);
					writer.newLine();
				}
				
				writer.write("endaction");
				writer.newLine();
			}
			
			writer.write("reward");
			writer.newLine();
			
			writer.write("\t" + cost);
			writer.newLine();
			
			writer.write("initial");
			writer.newLine();
			writer.write("\t" + initialState);
			writer.newLine();
			writer.write("endinitial");
			
			writer.newLine();
			writer.write("goal");
			writer.newLine();
			writer.write("\t" + goalState);
			writer.newLine();
			writer.write("endgoal");
			
			writer.close();
		} 
		catch (IOException e)
		{
			System.err.println("Error when creating domain file...");
			e.printStackTrace();
		}
	}

	private static String generateGoalState()
	{
		String result = String.format(TREE, "d", ZERO, TREE);
		
		for(int i = 1; i <= numberOfColumns; i++)
		{
			String var = String.format(VARIABLE_MASK_X, i);
			result = String.format(result, var, TREE, ZERO);
		}
		
		for(int i = 1; i <= numberOfLines; i++)
		{
			String var = String.format(VARIABLE_MASK_Y, i);
			if(i != numberOfLines) result = String.format(result, var, TREE, ZERO);
			else result = String.format(result, var, ONE, ZERO);
		}
		
		return result;
	}
	
	private static String generateInitialState()
	{
		String result = String.format(TREE, "d", ZERO, TREE);
		
		for(int i = 1; i <= numberOfColumns; i++)
		{
			String var = String.format(VARIABLE_MASK_X, i);
			result = String.format(result, var, ZERO, TREE);
		}
		
		for(int i = 1; i <= numberOfLines; i++)
		{
			String var = String.format(VARIABLE_MASK_Y, i);
			if(i != numberOfLines) result = String.format(result, var, TREE, ZERO);
			else result = String.format(result, var, ONE, ZERO);
		}
		
		return result;
	}
	
	private static String generateCost()
	{
		String cost = "(" + generateGoalState() + ")";
		
		cost = cost.replace("1.0", "-2.0");
		cost = cost.replace("0.0", "-1.0");
		cost = cost.replace("-2.0", "0.0");
		
		return cost;
	}

	private static HashMap<String, List<String>> generateActions()
	{
		HashMap<String, List<String>> actions = new HashMap<String, List<String>>();
		
		List<String> moveNorthADDs = getMoveNorthADDs();
		actions.put("movenorth", moveNorthADDs);
		
		List<String> moveSouthADDs = getMoveSouthADDs();
		actions.put("movesouth", moveSouthADDs);
		
		List<String> moveEastADDs = getMoveEastADDs();
		actions.put("moveeast", moveEastADDs);
		
		List<String> moveWestADDs = getMoveWestADDs();
		actions.put("movewest", moveWestADDs);
	
		return actions;
	}

	private static List<String> getMoveNorthADDs()
	{
		String traillingZeros = "0000000000000000000000000000000000";
		traillingZeros = traillingZeros.substring(0, numberOfLines);
		List<String> adds = new ArrayList<String>();
		
		String transition = null;
		for (int x = 1; x <= numberOfColumns; x++)
		{
			String currentVariable = String.format(VARIABLE_MASK_X, x);			
			transition = String.format(TRANSITION, currentVariable, TREE);
			transition = String.format(transition, "d", ZERO, TREE);
			transition = String.format(transition, currentVariable, ONE, ZERO);
			
			adds.add(transition);
		}
		
		for (int y = 1; y <= numberOfLines; y++)
		{
			String currentVariable = String.format(VARIABLE_MASK_Y, y);
			
			transition = String.format(TRANSITION, currentVariable, TREE);
			transition = String.format(transition, "d", ZERO, TREE_2);
			
			for (int i = 1; i <= numberOfLines; i++) 
			{
				if(i != numberOfLines) transition = String.format(transition, String.format(VARIABLE_MASK_Y, i), TREE_2);
				else transition = String.format(transition, String.format(VARIABLE_MASK_Y, i), "DADO");
			}
			
			int count = 1;
			int successor = (int) Math.pow(2, numberOfLines)-2;
			do
			{
				if(count++ <= 2) transition = transition.replaceFirst("DADO", ONE);
				else
				{
					String binaryString = Integer.toBinaryString(successor);
					binaryString = traillingZeros.substring(0, traillingZeros.length()-binaryString.length()) + binaryString;
					char value = binaryString.charAt(y-1);
					if(value == '0') transition = transition.replaceFirst("DADO", ZERO);	
					else transition = transition.replaceFirst("DADO", ONE);	
					successor--;
				}
			} while(successor >= 0);
			
			adds.add(transition);
		}
		
		adds.add(getDisapperanceString((int)Math.pow(2, numberOfLines)-1, (int)Math.pow(2, numberOfLines), 0));
		
		return adds;
	}

	// Goal State has a transition
	private static List<String> getMoveSouthADDs()
	{
		String traillingZeros = "0000000000000000000000000000000000";
		traillingZeros = traillingZeros.substring(0, numberOfLines);
		List<String> adds = new ArrayList<String>();
		
		String transition = null;
		for (int x = 1; x <= numberOfColumns; x++)
		{
			String currentVariable = String.format(VARIABLE_MASK_X, x);			
			transition = String.format(TRANSITION, currentVariable, TREE);
			transition = String.format(transition, "d", ZERO, TREE);
			transition = String.format(transition, currentVariable, ONE, ZERO);
			
			adds.add(transition);
		}
		
		for (int y = 1; y <= numberOfLines; y++)
		{
			String currentVariable = String.format(VARIABLE_MASK_Y, y);
			
			transition = String.format(TRANSITION, currentVariable, TREE);
			transition = String.format(transition, "d", ZERO, TREE_2);
			
			for (int i = 1; i <= numberOfLines; i++) 
			{
				if(i != numberOfLines) transition = String.format(transition, String.format(VARIABLE_MASK_Y, i), TREE_2);
				else transition = String.format(transition, String.format(VARIABLE_MASK_Y, i), "DADO");
			}
			
			int count = 1;
			int successor = (int) Math.pow(2, numberOfLines)-2;
			do
			{
				count++;
				String binaryString = Integer.toBinaryString(successor);
				binaryString = traillingZeros.substring(0, traillingZeros.length()-binaryString.length()) + binaryString;
				char value = binaryString.charAt(y-1);
				if(value == '0')	transition = transition.replaceFirst("DADO", ZERO);	
				else transition = transition.replaceFirst("DADO", ONE);	
				successor--;
				if(successor < 0) successor = 0;
			} while(count <= Math.pow(2, numberOfLines));
			
			adds.add(transition);
		}

		adds.add(getDisapperanceString(1, 2, 0));
		
		return adds;
	}
	
	private static List<String> getMoveEastADDs()
	{
		String traillingZeros = "0000000000000000000000000000000000";
		traillingZeros = traillingZeros.substring(0, numberOfLines);
		List<String> adds = new ArrayList<String>();
		
		String transition = null;
		
		for (int x = 1; x <= numberOfColumns; x++)
		{
			String currentVariable = String.format(VARIABLE_MASK_X, x);
			
			transition = String.format(TRANSITION, currentVariable, TREE);
			transition = String.format(transition, "d", ZERO, TREE_2);
			
			for (int i = 1; i <= numberOfColumns; i++) 
			{
				if(i != numberOfColumns) transition = String.format(transition, String.format(VARIABLE_MASK_X, i), TREE_2);
				else transition = String.format(transition, String.format(VARIABLE_MASK_X, i), "DADO");
			}
			
			int count = 1;
			int successor = (int) Math.pow(2, numberOfColumns)-2;
			do
			{
				if(count++ <= 2) transition = transition.replaceFirst("DADO", ONE);
				else
				{
					String binaryString = Integer.toBinaryString(successor);
					binaryString = traillingZeros.substring(0, traillingZeros.length()-binaryString.length()) + binaryString;
					char value = binaryString.charAt(x-1);
					if(value == '0') transition = transition.replaceFirst("DADO", ZERO);	
					else transition = transition.replaceFirst("DADO", ONE);	
					successor--;
				}
			} while(successor >= 0);
			
			adds.add(transition);
		}
		for (int y = 1; y <= numberOfLines; y++)
		{
			String currentVariable = String.format(VARIABLE_MASK_Y, y);			
			transition = String.format(TRANSITION, currentVariable, TREE);
			transition = String.format(transition, "d", ZERO, TREE);
			transition = String.format(transition, currentVariable, ONE, ZERO);
			
			adds.add(transition);
		}

		adds.add(getDisapperanceString(1, (int) Math.pow(2, numberOfLines), 1));
		
		return adds;
	}
	
	// Goal State has a transition
	private static List<String> getMoveWestADDs()
	{
		String traillingZeros = "0000000000000000000000000000000000";
		traillingZeros = traillingZeros.substring(0, numberOfLines);
		List<String> adds = new ArrayList<String>();
		
		String transition = null;
		
		for (int x = 1; x <= numberOfColumns; x++)
		{
			String currentVariable = String.format(VARIABLE_MASK_X, x);
			
			transition = String.format(TRANSITION, currentVariable, TREE);
			transition = String.format(transition, "d", ZERO, TREE_2);
			
			for (int i = 1; i <= numberOfColumns; i++) 
			{
				if(i != numberOfColumns) transition = String.format(transition, String.format(VARIABLE_MASK_X, i), TREE_2);
				else transition = String.format(transition, String.format(VARIABLE_MASK_X, i), "DADO");
			}
			
			int count = 1;
			int successor = (int) Math.pow(2, numberOfColumns)-2;
			do
			{
				count++;
				String binaryString = Integer.toBinaryString(successor);
				binaryString = traillingZeros.substring(0, traillingZeros.length()-binaryString.length()) + binaryString;
				char value = binaryString.charAt(x-1);
				if(value == '0') transition = transition.replaceFirst("DADO", ZERO);	
				else transition = transition.replaceFirst("DADO", ONE);	
				successor--;
				if(successor < 0) successor = 0;
			} while(count <= Math.pow(2, numberOfColumns));
			
			adds.add(transition);
		}
		for (int y = 1; y <= numberOfLines; y++)
		{
			String currentVariable = String.format(VARIABLE_MASK_Y, y);			
			transition = String.format(TRANSITION, currentVariable, TREE);
			transition = String.format(transition, "d", ZERO, TREE);
			transition = String.format(transition, currentVariable, ONE, ZERO);
			
			adds.add(transition);
		}

		adds.add(getDisapperanceString(1, (int) Math.pow(2, numberOfLines), -1));
		
		return adds;
	}

	private static String generateVariables()
	{
		List<String> variables = new ArrayList<String>();
		
		for (int x = 1; x <= numberOfColumns; x++)
			variables.add(String.format(VARIABLE_MASK_X, x));
		
		for (int y = 1; y <= numberOfLines; y++)
			variables.add(String.format(VARIABLE_MASK_Y, y));
		
		variables.add(VARIABLE_MASK_D);
		
		String variablesList = variables.get(0);
		
		for (int i = 1; i < variables.size(); i++)
			variablesList += (" " + variables.get(i));
		
		return variablesList;
	}

	private static String getNoTransitionString(String currentVariable, String condition)
	{
		String result = TRANSITION;
		// Goal State
		if(condition != null) 
		{
			result = String.format(result, currentVariable, generateGoalState());

			return result;
		}
		
		return String.format(result, currentVariable, ZERO);
	}
	
	private static String getMovementTransition(String currentVariable, String previousVariable, boolean stayInPlace, String condition, boolean column)
	{	
		String result = String.format(TRANSITION, currentVariable, TREE);
		result = String.format(result, "d", ZERO, TREE);
		
		if(column)
		{				
			for(int i = 1; i <= numberOfColumns; i++)
			{
				String var = String.format(VARIABLE_MASK_X, i);
				
				if(var.equals(previousVariable)) continue;
				else if(stayInPlace && var.equals(currentVariable)) continue;
				

				result = String.format(result, var, ZERO, TREE);
			}
		}
		else
		{			
			for(int i = 1; i <= numberOfLines; i++)
			{
				String var = String.format(VARIABLE_MASK_Y, i);
				
				if(var.equals(previousVariable)) continue;
				else if(stayInPlace && var.equals(currentVariable)) continue;
				

				result = String.format(result, var, ZERO, TREE);
			}
		}
		
		if(condition != null)
			result = String.format(result, condition, ZERO, TREE);
		
		if(stayInPlace == true) result = String.format(result, currentVariable, previousVariable + " ([0.0]) ([1.0])", TREE);
		result = String.format(result, previousVariable, ONE, ZERO);
		
		return result;
	}
	
	private static String getDisapperanceString(int y1, int y2, int x1)
	{
		String result = String.format(TRANSITION, "d", TREE);
		result = String.format(result, "d", ONE, TREE_2);
			
		for (int i = 1; i <= numberOfLines; i++) 
		{
			if(i != numberOfLines) result = String.format(result, String.format(VARIABLE_MASK_Y, i), TREE_2);
			else result = String.format(result, String.format(VARIABLE_MASK_Y, i), "DADO");
		}
		
		for(int y = (int) Math.pow(2, numberOfLines); y > 0; y--)
		{
			if(y == y1 || y == y2) result = result.replaceFirst("DADO", ZERO);		
			else
			{	
				String teste = TREE_2;
				for (int i = 1; i <= numberOfColumns; i++) 
				{
					if(i != numberOfColumns) teste = String.format(teste, String.format(VARIABLE_MASK_X, i), TREE_2);
					else teste = String.format(teste, String.format(VARIABLE_MASK_X, i), "DADO");
				}
				for(int x = (int) Math.pow(2, numberOfColumns); x > 0; x--) 
				{
					int successorX = x+x1;
					if(successorX == 0) successorX = 1;
					if(successorX > (int) Math.pow(2, numberOfColumns)) successorX = (int) Math.pow(2, numberOfColumns);
					double existenceProbability = getExistenceProbability(successorX);
					
					if(existenceProbability == 0.5) existenceProbability = 0.55;
					
					if(successorX != (int) Math.pow(2, numberOfColumns)) teste = teste.replaceFirst("DADO", String.format("[%1$f]", existenceProbability));
					else if(y != (int) Math.pow(2, numberOfLines)) teste = teste.replaceFirst("DADO", String.format("[%1$f]", existenceProbability));
					else teste = teste.replaceFirst("DADO", ZERO);
				}
				
				result = result.replaceFirst("DADO", teste);
			}
		}
		
		return result;	
	}

	private static double getExistenceProbability(int column)
	{
		double first_point = maxProbOfDisappear;
		double last_point = minProbOfDisappear;
		
		//linear formula to compute the probability of movement fail
		return 1 - (first_point + (column - 1) * ((last_point - first_point) / ((int) Math.pow(2, numberOfColumns) - 1)));
	}
}