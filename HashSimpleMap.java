
public class HashSimpleMap<K, V> implements SimpleMap<K, V>
{
    SimpleUSet<Pair<K,V>> map = new HashUSet<Pair<K,V>>();

    private class Pair<K, V>
    {
        protected V value = null;
        protected K key = null;

        //We want the pair to be hashed to same location in the map
        @Override
        public int hashCode()
        {
            return key.hashCode();
        }

        //This override is to make sure that pairs with the same keys are considered equal
        @Override
        public boolean equals(Object other)
        {
            if(((Pair<K,V>)other).key.equals(key))
            {
                return true;
            }

            return false;
        }

        //Defining the constructor for the Pair class
        public Pair(K key, V value)
        {
            this.key = key;
            this.value = value;
        }

    }

    //This method returns the number of pairs present in the Hash Map
    public int size()
    {
        return map.size();
    }

    //This method checks whether the hash map is empty (has no pairs)
    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    //This method returns the value of the pair associated with 'key'
    public V get(K key)
    {
        //Creating a dummy pair with the same key and a null value
        Pair<K, V> pair = new Pair<K, V>(key, null);

        //If no pair with the associated key exists, return null
        if(map.find(pair) == null)
        {
            return null;
        }

        //If such a pair exists, return the value of the pair
        return map.find(pair).value;
    }

    //This method adds a pair with the given key and value to the Hash Map
    public V put(K key, V value)
    {
        //Creating a dummy pair with the same key and value
        Pair<K, V> pair = new Pair<K, V>(key, value);

        //If no pair with the given key exists, add the pair and return null
        if(map.find(pair) == null)
        {
            map.add(pair);
            return null;
        }

        //Obtain value of the existing pair with the given key
        V target = map.find(pair).value;

        //Remove the old pair and add the new pair with the given key
        map.remove(map.find(pair));
        map.add(pair);

        //Return the value of the old pair that was removed
        return target;
    }

    //This method removes a pair with the given key from the hash map
    public V remove(K key){

        //If no pair with the given key exists in the map, return null
        if(get(key) == null)
        {
            return null;
        }

        //Creating a dummy pair with the same key and a null value
        Pair<K, V> pair = new Pair<K, V>(key, null);

        //Store the value of the pair to be removed and then remove the pair
        V target = map.find(pair).value;
        map.remove(map.find(pair));

        //Return the value of the pair that was removed
        return target;
    }

    //This method checks whether a pair with the given key exists in the Hash Map
    public boolean contains(K key)
    {
        return get(key) != null;
    }
}