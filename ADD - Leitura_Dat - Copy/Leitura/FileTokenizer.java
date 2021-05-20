package Leitura;

import java.io.FileNotFoundException;
import java.io.StreamTokenizer;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;

public class FileTokenizer
{
	private File file;
	private FileReader fileReader;
	private double number;
	private StreamTokenizer streamTokenizer;
	
	public FileTokenizer(String fileName)
	{
		file = new File(fileName);
		openFile();
	}
	
	private void openFile()
	{
		try
		{
			fileReader = new FileReader(file);
			streamTokenizer = new StreamTokenizer(fileReader);
		} catch (FileNotFoundException e)
		{
			System.out.println("Error: file not found\n");
		}
		defineWordChars();
	}
	
	private void defineWordChars()
	{
		streamTokenizer.wordChars('\'','\'');
		streamTokenizer.wordChars('_','_');
	}
	
	public void closeFile()
	{
	    try
	    {
			if (fileReader != null) fileReader.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public String getName()
	{
		return file.getName();
	}
	
	public void delete()
	{
		file.delete();
	}
	
	// Read until next word or EOF is found
	public String nextToken()
	{
		try
		{
			while(true)
			{				
				streamTokenizer.nextToken();
				switch(streamTokenizer.ttype)
				{
					case '(':
						return "(";
					case ')':
						return ")";
					case StreamTokenizer.TT_WORD:
						return streamTokenizer.sval;
					case StreamTokenizer.TT_NUMBER:
						number = streamTokenizer.nval;
						return "";
					case StreamTokenizer.TT_EOF:
						return null;
				}
			}
		} catch(IOException e)
		{             
			System.out.println("Error: IOException\n");
		}
		
		return null;
	}
	
	public boolean isNumber()
	{
		return (streamTokenizer.ttype==StreamTokenizer.TT_NUMBER);	
	}
	
	public double getNumber()
	{
		return number;
	}
}