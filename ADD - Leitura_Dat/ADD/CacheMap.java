package ADD;

import java.util.Map;
import java.util.LinkedHashMap;

public class CacheMap extends LinkedHashMap
{
	private int maxEntries;

    public CacheMap()
	{
		super(11);
		maxEntries = 10;
    }

    public CacheMap(int maxEntries)
	{
		super(maxEntries);
		this.maxEntries = maxEntries;
    }

    protected boolean removeEldestEntry(Map.Entry eldest)
	{
        return size() > maxEntries;
    }
}
