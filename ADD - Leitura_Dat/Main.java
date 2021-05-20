import ADD.*;
import Domain.*;
import Leitura.*;
import Solvers.*;

import java.util.*;

class Main
{	
	private static final int SIZE = 1;
    
	public static void main(String args[])
    {
		if(args.length != 1)
		{
			System.out.println("Um parametro eh necessario.");
			System.out.println("Ex.: Main [arquivo de entrada]");
			return;
		}
		
		//Arquivo arquivoEntrada = new Arquivo(args[0], Arquivo.LEITURA);
		FileTokenizer arquivoEntrada = null;
		Arquivo arquivoSaida = new Arquivo(args[0].substring(0, args[0].length()-3) + "txt", Arquivo.ESCRITA);
		
		Cronometro cT = new Cronometro();	
		Domain d = null;
		
		System.out.println();
		
		Solver s = null;
		double averageTime = 0;
		for(int i = 0; i < SIZE; i++)
		{
			arquivoEntrada = new FileTokenizer(args[0]);
			Leitura l = new Leitura(arquivoEntrada);
			
			System.out.println("Lendo Arquivo...");
			d = l.createDomain();
			arquivoEntrada.closeFile();
			System.out.println("Leitura Concluida!");
			
			s = new SPUDD(d, 500);
			
			System.out.println("\nExecutando SPUDD...");
			cT.start();
			
			s.executa();
			
			cT.stop();
			
			averageTime += cT.getTempo();
			
			System.out.println("\nExecucao " + (i+1) + " de " + SIZE);
			System.out.println("Tempo medio " + averageTime/(i+1));
		}
		averageTime /= SIZE;
		
		System.out.println("\nPolitica:");
		// s.print();
		//System.out.println("Quantidade Total de Estados: " + g.nodesAmount());
		//arquivoSaida.escreveArquivo("Quantidade Total de Estados: " + g.nodesAmount());
		
		System.out.println("\nTempo de execucao: " + cT.getTempo() + " ms");
		arquivoSaida.escreveArquivo("Tempo de execucao: " + cT.getTempo() + " ms");
		//System.out.println("\nTempo de execucao: " + averageTime + " ms");
		//arquivoSaida.escreveArquivo("Tempo de execucao: " + averageTime + " ms");
		
		arquivoSaida.fechaArquivo();
    }
}
