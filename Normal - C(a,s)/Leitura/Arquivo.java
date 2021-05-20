package Leitura;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;

public class Arquivo
{
	public static final int LEITURA = 0;  
	public static final int ESCRITA = 1;  
	
	private File file;
	private FileInputStream fIS;
	private InputStreamReader iSR;
    private BufferedReader bR;
	private FileWriter fW;
	private PrintWriter pW;
	
	
	public Arquivo(String nome, int tipo)
	{
		file = new File(nome);
		abreArquivo(tipo);
	}
	
	public void appendFile(Arquivo arquivo)
	{
		String linha = null;
		while((linha = arquivo.leLinha()) != null) escreveArquivo(linha);
		arquivo.fechaArquivo();
		arquivo.delete();
	}
	
	public String getName()
	{
		return file.getName();
	}
	
	public void delete()
	{
		file.delete();
	}
	
	public String leLinha()
	{
		String linha = null;
		try
		{
			linha = bR.readLine();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return linha;
	}
	
	public String leArquivo()
	{
		String arquivo = "";
		String linha = null;
		
		while((linha = leLinha()) != null) arquivo += linha;
		
		return arquivo;
	}
	
	public void escreveArquivo(String texto)
	{
		pW.println(texto);
	}
	
	public void print(String texto)
	{
		pW.print(texto);
	}
	
	public void abreArquivo(int tipo)
	{
		try 
		{
			if (tipo == LEITURA)
			{
				fIS = new FileInputStream(file);
				iSR = new InputStreamReader(fIS);
				bR = new BufferedReader(iSR);
			}
			else
			{
				fW = new FileWriter(file);
				pW = new PrintWriter(fW);
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
			fechaArquivo();
		}
	}
	
	public void fechaArquivo()
	{
	    try
	    {
			if (fIS != null) fIS.close();
			if (iSR != null) iSR.close();
			if (bR != null) bR.close();
			if (fW != null) fW.close();
			if (pW != null) pW.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}