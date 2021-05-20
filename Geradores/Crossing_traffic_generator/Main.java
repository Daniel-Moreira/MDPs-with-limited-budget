import java.util.*;

class Main
{
	private final static int EMPTY = 0;
	private final static int FINAL_STATE_QUANTITY = 1;
	private final static int OBSTACLE = 1;
	private final static int INITIAL_STATE = 2;
	private final static int FINAL_STATE = 3;
	private final static int INVALID_STATE = 4;
	private final static int ROBOT = 5;
	private final static String[] ACOES = {"Move_South", "Move_North", "Move_West", "Move_East", "Noop"};
	private final static int[] linhaI = {-1, 1, 0, 0, 0};
	private final static int[] colunaI = {0, 0, 1, -1, 0};
	
	private static double obstacleFactor;
	private static int[][] grid;
	private static Arquivo file;
	
    public static void main(String args[])
    {
		int fileNumber = Integer.parseInt(args[0]);
		// Size of grid
			int x = Integer.parseInt(args[1]);
			int y = Integer.parseInt(args[2]);

		// Input rate of obstacles
			obstacleFactor = Double.parseDouble(args[3]);
		
		// Set Initial State
		int[] initialState = {1, 1};
		
		file = new Arquivo("crossing_traffic" + fileNumber + ".dat", Arquivo.ESCRITA);
		grid = new int[x+2][y+2];
		clear();
		
		put(initialState[0], initialState[1], INITIAL_STATE);
				
		// Put Invalid States
			putInvalid();
			
		// Set final states
			// String[] estadosFinais = setFinalStates(rF);
			
		// Print
			printVariables();
			printInitialState(initialState[0], initialState[1]);
			printActions();
			// printCosts();
			
			file.fechaArquivo();
    }
	
	public static void printVariables()
	{
		file.escreveArquivo("variables");
		for(int i = 1; i < grid.length-1; i++)
			for(int j = 1; j < grid[i].length-1; j++)
				file.escreveArquivo("\t" + getString(OBSTACLE, i, j));
			
		for(int i = 1; i < grid.length-1; i++)
			for(int j = 1; j < grid[i].length-1; j++)
				file.escreveArquivo("\t" + getString(ROBOT, i, j));

		file.escreveArquivo("end variables\n");
	}
	
	public static void printInitialState(int x, int y)
	{
		getString(ROBOT, x, y);
	}
	
	public static void put(int x, int y, int object)
	{
		grid[x][grid.length-y-1] = object;
	}
	
	public static int getPosition(int x, int y)
	{
		return grid[x][grid.length-y-1];
	}
	
	public static String getString(int type, int x, int y)
	{
		String result = "";
		
		if(type == ROBOT) result = "robot_at__"; 
		else if(type == OBSTACLE) result = "obstacle_at__";
		
		result += "x" + x + "_y" + y;
		
		return result;
	}
	
	// Put invalid states
	public static void putInvalid()
	{
		for(int i = 0; i < grid[0].length; i++) put(0, i, INVALID_STATE);
		for(int i = 0; i < grid[0].length; i++) put(grid.length-1, i, INVALID_STATE);
		for(int i = 0; i < grid.length; i++) put(i, 0, INVALID_STATE);
		for(int i = 0; i < grid.length; i++) put(i, grid[0].length-1, INVALID_STATE);
	}
	
	// Build and print actions
	public static void printActions()
	{
		for(int a = 0; a < ACOES.length; a++)
		{
			file.escreveArquivo("action " + ACOES[a]);
			String text = "";
			int x = colunaI[a];
			int y = linhaI[a];
			
			printObstacles();
			for(int i = 1; i < grid.length-1; i++)
			{
				for(int j = 1; j < grid[i].length-1; j++)
				{
					text = "\t" + getString(ROBOT, i, j) + "' ";
					// Trying to get out of the grid
					if(getPosition(i+x, j+y) == INVALID_STATE) continue; //text += getString(ROBOT, i, j) + " (true " + getProb(i, j) + ") (false " + (1-getProb(i, j)) + ")";
					else
					{
						text += getString(ROBOT, i+x, j+y);
						// Robot goes underneath the car
						if(a == 3) text += " (true 1.0) (false 0.0)";
						// Check if there's a obstacle in the way
						else if(j > 1 && j < grid[0].length-2 && i < grid.length-2)
						{
							text += " (true " + getString(OBSTACLE, i, j) + " (true 0.0) (false 1.0))";
							text += " (false 0.0)";
						}
						else text += " (true " + String.format("%.3f", getProb(i, j)) + ") (false 0.0)";
					}
					file.escreveArquivo(text);
					
					// Robot at the edges
					if(getPosition(i-x, j-y) == INVALID_STATE)
					{
						text = "\t" + getString(ROBOT, i, j) + "' " + getString(ROBOT, i, j) + " (true " + String.format("%.3f", getProb(i, j)) + ") (false 0.0)";								
						file.escreveArquivo(text);
					}	
				}
			}
			file.escreveArquivo("endaction\n");
		}
	}
	
	public static double getProb(int x, int y)
	{
		if(y > 1 && y < grid[0].length-2 && x == grid.length-2) return 1-obstacleFactor;
		
		return 1.0;
	}
	
	public static void printObstacles()
	{
		String text = "";
		
		for(int i = 2; i < grid.length-2; i++)
		{
			for(int j = 2; j < grid[i].length-2; j++)
			{
				text = "\t" + getString(OBSTACLE, i-1, j) + "' " + getString(OBSTACLE, i, j);
				text += " (true 1.0)";
				text += " (false 0.0)";
				file.escreveArquivo(text);
			}
		}
		
		for(int j = 2; j < grid[0].length-2; j++)
		{
			text = "\t" + getString(OBSTACLE, grid[0].length-2, j) + "' ";
			text += obstacleFactor;
			file.escreveArquivo(text);
		}
	}
	
	private static void clear()
	{
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[i].length; j++) grid[i][j] = EMPTY;
	}
}
