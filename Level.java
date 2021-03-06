//queue for each priority level in the ready list
public class Level 
{	
	public class Node
	{
		final PCB data;
		Node next;
		
		Node(PCB data)
		{
			this.data = data;
			next = null;
		}
	}
	
	private Node head;
	private Node tail;
	
	Level()
	{
		head = null;
		tail = null;
	}
	
	//add to tail
	public void add(PCB data)
	{
		Node newNode = new Node(data);
		
		if(head == null)
		{
			head = newNode;
			tail = newNode;
		}
		else
		{
			tail.next = newNode;
			tail = newNode;
		}
	}
	
	public boolean delete(String pId)
	{
		if(head == null)
			return false;
		
		if(head.data.getPId().equals(pId))
		{
			head = head.next;
			return true;
		}
		
		Node prev = null;
		Node current = head;
		
		while(current.next != null)
		{
			prev = current;
			current = current.next;
			
			if(current.data.getPId().equals(pId))
			{
				prev.next = current.next;
				return true;
			}
		}
		
		return false;
	}
	
	public void timeout()
	{
		if(head == null)
			return;
		
		Node oldHead = head;
		head = head.next;
		add(oldHead.data);
	}
	
	public Node getHead()
	{
		return head;
	}
	
	public void removeHead()
	{
		if(head != null)
			head = head.next;
	}
}
