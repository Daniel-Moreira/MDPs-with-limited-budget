package Leitura;

import Grafo.Acao;
import Grafo.Graph;
import Grafo.Node;
import Grafo.Transition;

import java.util.Map;
import java.util.HashMap;

public class Leitura
{
	private Arquivo arquivo;
	private Graph g;
	
	public Leitura(Arquivo a)
	{
		arquivo = a;
	}
	
	public Graph executa()
	{
		g = new Graph();
		
		leEstados();
		leAcoes();
		leCustos();
		leEstadoInicial();
		leEstadoObjetivo();
		
		return g;
	}
	
	public void leEstados()
	{
		// descarta "states"	
			arquivo.leLinha();
		String texto = arquivo.leLinha();
		String[] nomeEstados = texto.split(",");
	
		for(int i = 0; i < nomeEstados.length; i++)	g.putNode(new Node(nomeEstados[i].trim()));
		
		// descarta "endstates"	
		arquivo.leLinha();
		// descarta linha em branco	
		arquivo.leLinha();
	}
	
	public void leAcoes()
	{
		String[] palavras = arquivo.leLinha().split(" "); 
		
		do
		{
			String nomeAcao = palavras[1];
			Acao a = new Acao(nomeAcao);
			for(palavras = arquivo.leLinha().split(" "); !palavras[0].equals("endaction"); palavras = arquivo.leLinha().split(" "))
			{
				String nomeEstado = palavras[0].trim();
				Node parent = g.getNode(nomeEstado);
				a.addTransition(parent, g.getNode(palavras[1]), Double.parseDouble(palavras[2]));
			}
			g.addAction(a);
			// descarta linha em branco	
			arquivo.leLinha();
		} while((palavras = arquivo.leLinha().split(" "))[0].equals("action"));
	}
	
	public void leCustos()
	{
		String[] palavras;
		for(palavras = arquivo.leLinha().split(" "); !palavras[0].equals("endcost");  palavras = arquivo.leLinha().split(" "))
			g.getAction(palavras[0].trim()).getTransition(palavras[1].trim(), palavras[2].trim()).setCost(Integer.parseInt(palavras[3]));
		// descarta linha em branco	
		arquivo.leLinha();
	}
	
	public void leEstadoInicial()
	{
		// Descarta "inicialstate"
		arquivo.leLinha();
		g.setInitialState(arquivo.leLinha().trim());
		// Descarta "endinicialstate"
		arquivo.leLinha();
		// descarta linha em branco	
		arquivo.leLinha();
	}
	
	public void leEstadoObjetivo()
	{
		// Descarta "goalstate"
		arquivo.leLinha();
		for(String estado = arquivo.leLinha().trim(); !estado.equals("endgoalstate"); estado = arquivo.leLinha().trim()) g.setTerminalStates(estado);
	}
}