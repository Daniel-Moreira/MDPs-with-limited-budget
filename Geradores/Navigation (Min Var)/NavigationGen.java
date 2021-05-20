import java.io.*;
import java.util.*;

public class NavigationGen
{
	private static final String NUMBER_REGEX = "^[0-9]+$";
	private static final String ZERO = "[0.0]";
	private static final String ONE = "[1.0]";
	private static final String VARIABLE_MASK_X = "x%d"; //e.g. x1y1
	private static final String VARIABLE_MASK_Y = "y%d"; //e.g. x1y1
	private static final String VARIABLE_MASK_D = "d";
	private static final String TREE = "%1$s (%2$s) (%3$s)";
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
		
		if (numberOfColumns < 2)
		{
			System.out.println("The number of columns must be greater or equal to 2.");
			return;
		}
		
		if (numberOfLines < 3)
		{
			System.out.println("The number of lines must be greater or equal to 3.");
			return;
		}
		
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
		String variableX = String.format(VARIABLE_MASK_X, numberOfColumns);
		String variableY = String.format(VARIABLE_MASK_Y, numberOfLines);
		
		List<String> tree = Arrays.asList("d", "[0.0]", TREE, variableX, TREE, "[0.0]", variableY, "[1.0]", "[0.0]");
		String result = String.format(TREE, tree.get(0), tree.get(1), tree.get(2));
		
		for(int i = 1; i < numberOfColumns; i++)
		{
			String var = String.format(VARIABLE_MASK_X, i);
			result = String.format(result, var, ZERO, TREE);
		}
		
		for(int i = 1; i < numberOfLines; i++)
		{
			String var = String.format(VARIABLE_MASK_Y, i);
			result = String.format(result, var, ZERO, TREE);
		}
		
		for(int i = 3; i+2 < tree.size(); i+=3)
			result = String.format(result, tree.get(i), tree.get(i+1), tree.get(i+2));
		// String result = constructTree(tree);
		
		return result;
	}
	
	private static String generateInitialState()
	{
		String variableX = String.format(VARIABLE_MASK_X, numberOfColumns);
		String variableY = String.format(VARIABLE_MASK_Y, 1);
		
		List<String> tree = Arrays.asList("d", "[0.0]", TREE, variableX, TREE, "[0.0]", variableY, "[1.0]", "[0.0]");
		
		String result = constructTree(tree);
		
		return result;
	}
	
	// List with {variable, true_condition, false_condition, variable, ...}
	private static String constructTree(List<String> tree)
	{
		String result = TREE;
		for(int i = 0; i+2 < tree.size(); i+=3)
			result = String.format(result, tree.get(i), tree.get(i+1), tree.get(i+2));
		
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
		List<String> adds = new ArrayList<String>();
		
		String transition = null;
		for (int x = 1; x <= numberOfColumns; x++)
		{
			String currentVariable = String.format(VARIABLE_MASK_X, x);			
			adds.add(getMovementTransition(currentVariable, currentVariable, false, null, true));
		}	
		
		for (int y = 1; y <= numberOfLines; y++)
		{	
			
			String currentVariable = String.format(VARIABLE_MASK_Y, y);
			String previousVariable = String.format(VARIABLE_MASK_Y, y-1);
			// String goalVariable = getGoalString();
			
			if (y == 1) //Impossible to transit
				transition = getNoTransitionString(currentVariable, null);
			else if (y == numberOfLines) //Safe Zone + Goal State  
				transition = getMovementTransition(currentVariable, previousVariable, true, null, false);
			else //Normal Movement
				transition = getMovementTransition(currentVariable, previousVariable, false, null, false);
			
			adds.add(transition);
		}
		
		adds.add(getDisapperanceString(3, 4));
		
		return adds;
	}

	// Goal State has a transition
	private static List<String> getMoveSouthADDs()
	{
		List<String> adds = new ArrayList<String>();
		
		String transition = null;
		for (int x = 1; x <= numberOfColumns; x++)
		{
			String currentVariable = String.format(VARIABLE_MASK_X, x);			
			adds.add(getMovementTransition(currentVariable, currentVariable, false, null, true));
		}
		
		for (int y = 1; y <= numberOfLines; y++)
		{	
			String currentVariable = String.format(VARIABLE_MASK_Y, y);
			String previousVariable = String.format(VARIABLE_MASK_Y, y + 1);
			
			if (y == numberOfLines) // last line
				transition = getNoTransitionString(currentVariable, String.format(VARIABLE_MASK_X, numberOfColumns));
			else if (y == 1) //first line
				transition = getMovementTransition(currentVariable, previousVariable, true, null, false);
			else if (y == numberOfLines-1) //cell above the goal
				transition = getMovementTransition(currentVariable, previousVariable, false, String.format(VARIABLE_MASK_X, numberOfColumns), false);
			else //middle lines
				transition = getMovementTransition(currentVariable, previousVariable, false, null, false);
			
			adds.add(transition);
		}

		adds.add(getDisapperanceString(1, 2));
		
		return adds;
	}
	
	private static List<String> getMoveEastADDs()
	{
		List<String> adds = new ArrayList<String>();
		
		String transition = null;
		
		for (int x = 1; x <= numberOfColumns; x++) 
		{
			String currentVariable = String.format(VARIABLE_MASK_X, x);
			String previousVariable = String.format(VARIABLE_MASK_X, x-1);
			
			if (x == 1) // first column
				transition = getNoTransitionString(currentVariable, null);
			else if (x == numberOfColumns) //last column
					transition = getMovementTransition(currentVariable, previousVariable, true, null, true);
			else if (x > 1 && x < numberOfColumns) //middle coluns
					transition = getMovementTransition(currentVariable, previousVariable, false, null, true);
			
			adds.add(transition);
		}
		
		for (int y = 1; y <= numberOfLines; y++)
		{
			String currentVariable = String.format(VARIABLE_MASK_Y, y);			
			adds.add(getMovementTransition(currentVariable, currentVariable, false, null, false));
		}

		adds.add(getDisapperanceString(1, numberOfLines));
		
		return adds;
	}
	
	// Goal State has a transition
	private static List<String> getMoveWestADDs()
	{
		List<String> adds = new ArrayList<String>();
		
		String transition = null;
		for (int x = 1; x <= numberOfColumns; x++)
		{
			String currentVariable = String.format(VARIABLE_MASK_X, x);
			String previousVariable = String.format(VARIABLE_MASK_X, x+1);
			
			if (x == numberOfColumns-1) //cell before goal
				transition = getMovementTransition(currentVariable, previousVariable, false, String.format(VARIABLE_MASK_Y, numberOfColumns), true);
			else if (x == 1) // first column
				transition = getMovementTransition(currentVariable, previousVariable, true, null, true);
			else if (x == numberOfColumns) //last column
				transition = getNoTransitionString(currentVariable, String.format(VARIABLE_MASK_Y, numberOfColumns));
			else if (x > 1 && x < numberOfColumns) //middle columns
				transition = getMovementTransition(currentVariable, previousVariable, false, null, true);

			adds.add(transition);
		}
			
		for (int y = 1; y <= numberOfLines; y++)
		{
			String currentVariable = String.format(VARIABLE_MASK_Y, y);			
			adds.add(getMovementTransition(currentVariable, currentVariable, false, null, false));
		}

		adds.add(getDisapperanceString(1, numberOfLines));
		
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
	
	private static String getDisapperanceString(int y1, int y2)
	{	
		String result = String.format(TRANSITION, "d", TREE);
		result = String.format(result, "d", ONE, TREE);
			// y == 1
			String currentVariable = String.format(VARIABLE_MASK_Y, y1);
			result = String.format(result, currentVariable, ZERO, TREE);
			// y == n
			currentVariable = String.format(VARIABLE_MASK_Y, y2);
			result = String.format(result, currentVariable, ZERO, TREE);
		
		// middle
		for(int y = 1; y <= numberOfLines; y++)
		{
			if(y == y1 || y == y2) continue;
			String var = String.format(VARIABLE_MASK_Y, y);

			String teste = TREE;
			boolean noNext = true;
			for(int yN = y+1; yN <= numberOfLines; yN++)
			{
				if(yN == y1 || yN == y2) continue;
				noNext = false;
				String varN = String.format(VARIABLE_MASK_Y, yN);
				teste = String.format(teste, varN, ZERO, TREE);
			}
			
			for(int x = 1; x <= numberOfColumns; x++) 
			{
				double existenceProbability = getExistenceProbability(x);
				
				if(existenceProbability == 0.5) existenceProbability = 0.55;
				
				currentVariable = String.format(VARIABLE_MASK_X, x);
				if(x != numberOfColumns) teste = String.format(teste, currentVariable, String.format("[%1$f]", existenceProbability), TREE);
				else if(y != numberOfLines) teste = String.format(teste, currentVariable, String.format("[%1$f]", existenceProbability), ZERO);
				else teste = String.format(teste, currentVariable, ONE, ZERO);
			}
			
			if(!noNext) result = String.format(result, var, teste, TREE);
			else result = String.format(result, var, ZERO, teste);
		}
		
		return result;	
	}

	private static double getExistenceProbability(int column)
	{
		double first_point = maxProbOfDisappear;
		double last_point = minProbOfDisappear;
		
		//linear formula to compute the probability of movement fail
		return 1 - (first_point + (column - 1) * ((last_point - first_point) / (numberOfColumns - 1)));
	}
}