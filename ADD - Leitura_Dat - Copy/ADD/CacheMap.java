package ADD;

import java.util.Map;
import java.util.LinkedHashMap;

public class CacheMap extends LinkedHashMap
{
	private int maxEntries;

    public CacheMap()
	{
		super(1000000);
		maxEntries = 1000000;
    }

    public CacheMap(int maxEntries)
	{
		super(1000000);
		this.maxEntries = maxEntries;
    }

    protected boolean removeEldestEntry(Map.Entry eldest)
	{
        return size() > maxEntries;
    }
}
