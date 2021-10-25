import java.util.ArrayList;
import java.util.List;

class HashTable
{
 //   private int id=0;
    private int size;
    private int capacity;
    private int firstElement;
    private int firstFree;
    private List<String> entries;

    HashTable()
    {
        size = 0;
        firstFree = -1;
        firstElement = -1;
        capacity = 0;
        entries = new ArrayList<>();
    }

    private void resize()
    {
        if (capacity == 0)
        {
            capacity = 1;
        }

        capacity = capacity * 2;

        int index = entries.size();
        for (; index < capacity; ++index)
        {
            entries.add(null);
        }
    }

    void add(String element)
    {

        int hashValue = getHash(element);

        while (hashValue >= capacity)
        {
            resize();
        }

        if (null == entries.get(hashValue))
        {
            entries.set(hashValue, element);
        }
        else
        {
            if (!find(element))
            {
                int freePosition = getFreePosition();
                entries.set(freePosition, element);
            }
        }
    }

    private boolean find(String element)
    {
        for (String entry : entries)
        {
            if (null != entry  && entry.equals(element))
            {
                return true;
            }
        }
        return false;
    }

    int findElement(String element)
    {
        for (int index = 0; index < entries.size(); ++index)
        {
            if (null != entries.get(index) && entries.get(index).equals(element))
            {
                return index;
            }
        }
        return -1;
    }

    private int getFreePosition()
    {
        for (int index = 0; index < entries.size(); ++index)
        {
            if (null == entries.get(index))
            {
                return index;
            }
        }
        return -1;
    }

    private int getHash(String element)
    {
        int hashValue = 0;
        for (char c : element.toCharArray())
        {
            hashValue = hashValue + (int)c;
        }
        return hashValue / 11;
    }

    List<String> getEntries()
    {
        return entries;
    }
}