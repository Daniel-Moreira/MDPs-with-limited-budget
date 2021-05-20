import java.util.Map;
import java.util.HashMap;

public class Dictionary <E>
{
	private Map<Integer, E> dicionario;
	
	public Dictionary()
	{
		dicionario = new HashMap<Integer, E>();
	}
	
	public E getItem(E n)
	{
		if(dicionario.containsKey(n.hashCode())) return dicionario.get(n.hashCode());
		
		dicionario.put(n.hashCode(), n);
		return n;
	}
}
