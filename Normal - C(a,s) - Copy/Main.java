import Grafo.*;
import Leitura.*;
import Solvers.*;

import java.util.*;

class Main
{	
	private static final int SIZE = 1;
	private static final int thresh = 230;
    
	public static void main(String args[])
    {
		if(args.length != 1)
		{
			System.out.println("Um parametro eh necessario.");
			System.out.println("Ex.: Main [arquivo de entrada]");
			return;
		}
		
		//Arquivo arquivoEntrada = new Arquivo(args[0], Arquivo.LEITURA);
		Arquivo arquivoEntrada = null;
		Arquivo arquivoSaida = new Arquivo(args[0].substring(0, args[0].length()-3) + "txt", Arquivo.ESCRITA);
		
		Cronometro cT = new Cronometro();	
		Graph g = null;
		
		System.out.println();
		
		Solver s = null;
		double averageTime = 0;
		double tempoGera = 0;
		for(int i = 0; i < SIZE; i++)
		{
			arquivoEntrada = new Arquivo(args[0], Arquivo.LEITURA);
			Leitura l = new Leitura(arquivoEntrada);
			
			System.out.println("Lendo Arquivo...");
			g = l.executa();
			System.out.println("Leitura Concluida!");
			
			System.out.println("Generating Augmented MDP");
			cT.start();
			s = new TVIDP(g, thresh);
			cT.stop();
			System.out.println("Augemented MDP Generated!");
			tempoGera = cT.getTempo();
			
			System.out.println("\nExecutando " + s.getName() + "...");
			cT.start();
			
			s.executa();
			
			cT.stop();
			
			averageTime += cT.getTempo();
			
			System.out.println("\nExecucao " + (i+1) + " de " + SIZE);
			System.out.println("Tempo medio " + averageTime/(i+1));
			arquivoEntrada.fechaArquivo();
		}
		averageTime /= SIZE;
		
		// s.print();
		// g.printAugmented();
		// System.out.println("Quantidade Total de Estados: " + g.nodesAmount());
		// arquivoSaida.escreveArquivo("Quantidade Total de Estados: " + g.nodesAmount());
		
		//System.out.println("\nTempo de execucao: " + cT.getTempo() + " ms");
		//arquivoSaida.escreveArquivo("Tempo de execucao: " + cT.getTempo() + " ms");
		System.out.println("\nTempo de execucao: " + averageTime + " ms");
		System.out.println("Number of states Generated!" + g.augmentedNodesSize());
		System.out.println("Tempo de geracao: " + tempoGera + " ms");
		arquivoSaida.escreveArquivo("Tempo de execucao: " + averageTime + " ms\n" + "Tempo de Geracao: " + tempoGera + " ms\n");
		
		arquivoEntrada.fechaArquivo();
		arquivoSaida.fechaArquivo();
    }
}
