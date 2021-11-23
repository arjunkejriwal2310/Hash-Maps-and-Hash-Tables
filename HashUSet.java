import java.util.Random;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashUSet<E> implements SimpleUSet<E>
{
    public static final int DEFAULT_LOG_CAPACITY = 4;
    
    protected int logCapacity = DEFAULT_LOG_CAPACITY; // value d from lecture
    protected int capacity = 1 << logCapacity;        // n = 2^d
    protected int size = 0;
    protected Object[] table;                         // array of heads of linked lists

    // final = can't be changed after initial assignment!
    protected final int z;

    public HashUSet()
	{
	    // set capacity to 2^logCapacity
	    int capacity = 1 << logCapacity;
	
	    table = new Object[capacity];

	    // add a sentinel node to each list in the table
	    for (int i = 0; i < capacity; ++i)
	    {
	        table[i] = new Node<E>(null);
	    }

	    // fix a random odd integer
	    Random r = new Random();
	    z = (r.nextInt() << 1) + 1;
    }
    
    @Override
    public int size() {
	return this.size;
    }

    @Override
    public boolean isEmpty() {
	return this.size == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E x)
	{
		//if x already exists in the hash table, then return false
		if (find(x) != null)
		{
			return false;
		}

		//Use the hash function to get an index for x
		int index = getIndex(x);

		//Find the head node associated with that index
		Node<E> headNode = (Node<E>)table[index];

		//Create new node variables
		Node<E> newNode = new Node<E>(x);
		Node<E> currentNode = headNode;

		//if headNode.next is not null, then set that next node as the current node
		if(headNode.getNext() != null)
		{
			currentNode = headNode.getNext();
		}
		//else add the new node as the head's next
		else
		{
			headNode.setNext(newNode);
			size++;
			return true;
		}

		//Traverse the linked list in the particular index of the array until we reach the end (when getNext returns null)
		while(currentNode.getNext() != null)
		{
			currentNode = currentNode.getNext();
		}

		//then set the next node's value to x
		currentNode.setNext(newNode);

		//increment the size of the HashUSet and return true
		size++;
		return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(E x)
	{
		//Return null is no node containing x is found in the hash table
		if (find(x) == null)
		{
			return null;
		}

		//Use the hash function to get an index for x
		int index = getIndex(x);

		//Find the head node associated with that index
		Node<E> headNode = (Node<E>)table[index];

		//Store the previous node as a pointer to the headNode
		Node<E> previousNode = headNode;
		//Initialize the current Node as currently pointing to headNode's next node
		// Current node cannot be null since findNode found the node (findNode isn't null)
		Node<E> currentNode = headNode.getNext();

		//Run this while loop until we find and remove the node to be removed
		while(true)
		{
			//if the node to be removed is found
			if(currentNode.getVal().equals(x))
			{
				//set the previous node's next to the current Node's next
				previousNode.setNext(currentNode.getNext());

				//The node was removed from the linked list (and thus the hash table)
				// Therefore, decrement the size and return x
				size--;
				return currentNode.getVal();
			}
			//else, go to the next node in linked list at index i
			else
			{
				previousNode = currentNode;
				currentNode = currentNode.getNext();
			}
		}
    }

    @Override
    @SuppressWarnings("unchecked")
    public E find(E x)
	{
		//Create current and head nodes that point to the same node in the set
		Node<E> current = (Node<E>)table[getIndex(x)];
		Node<E> head = (Node<E>)table[getIndex(x)];

		//If the linked list at the index does not have a node containing x, then return null
		if(head.next == null)
		{
			return null;
		}

		//Initialize each of the node variables to the next node
		current = head.next;
		head = head.next;

		//If the value of the head equals x, then return x (x is found)
		if(head.value.equals(x))
		{
			return head.value;
		}

		//While the linked list hasn't reached the end, find x (and if found, return x)
		while(current.next != null)
		{
			if(current.next.value.equals(x))
			{
				return current.next.value;
			}

			current = current.next;
		}

		//Check if the last node in the linked list has value x (and then return x if this is the case)
		if(current.next == null)
		{
			if(current.value.equals(x))
			{
				return current.value;
			}
		}
		return null;
    }

    protected int getIndex(E x) {
	// get the first logCapacity bits of z * x.hashCode()
	return ((z * x.hashCode()) >>> 32 - logCapacity);
    }

    // @SuppressWarnings("unchecked")
    // protected void increaseCapacity() {
	
    // 	logCapacity += 1;
    // 	capacity = capacity << 1;

    // 	// store the old hash table
    // 	Object[] oldTable = table;

    // 	// make a new new has table and initialize it
    // 	table = new Object[capacity];

    // 	// add old lists to new table
    // 	for (int i = 0; i < oldTable.length; ++i) {
    // 	    table[i] = oldTable[i];	    
    // 	}

    // 	// add sentinel nodes to new table
    // 	for (int i = oldTable.length; i < table.length; ++i) {
    // 	    table[i] = new Node<E>(null);
    // 	}
    // }

    @SuppressWarnings("unchecked")
    protected void increaseCapacity() {	
    	logCapacity += 1;
    	capacity = capacity << 1;

    	// store the old hash table
    	Object[] oldTable = table;

    	// make a new new has table and initialize it
    	table = new Object[capacity];
	    
    	for (int i = 0; i < table.length; ++i) {
    	    table[i] = new Node<E>(null);
    	}

    	// reset the size to 0 since it will get incremented when we
    	// add elements to the new table
    	size = 0;

    	// iterate over lists in oldTable and add elements to new table
    	for (int i = 0; i < oldTable.length; ++i) {
    	    Node<E> nd = ((Node<E>)oldTable[i]).next;
    	    while (nd != null) {
    		this.add(nd.value);
    		nd = nd.next;
    	    }
    	}
    }
    
    protected class Node<E>
	{
	protected Node<E> next = null;
	protected E value;

	public Node(E value) {
	    this.value = value;
	}

		public E getVal(){
			return value;
		}

		public Node<E> getNext(){
			return next;
		}

		public void setVal(E val){
			this.value = val;
		}

		public void setNext(Node<E> node){
			this.next = node;
		}

    }
}
