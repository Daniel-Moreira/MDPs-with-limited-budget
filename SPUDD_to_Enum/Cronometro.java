public class Cronometro
{
	private long tempoInicio = 0;
	private long tempoFinal = 0;
	
	public void start()
	{
        tempoInicio = System.currentTimeMillis();
    }
	
	public void stop()
	{
        tempoFinal = System.currentTimeMillis();
    }
	
	public long getTempo()
	{
		return (tempoFinal-tempoInicio);   
	}

}