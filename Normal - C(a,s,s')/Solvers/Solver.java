package Solvers;

import Grafo.Graph;
import Grafo.Node;
import java.util.List;

public abstract class Solver
{
	public Graph graph;
	
	public abstract String getName();
	public abstract void executa();
	public abstract void print(List<Node> nodes);
}
