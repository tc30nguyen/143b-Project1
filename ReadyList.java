public class ReadyList 
{
	private final int NUM_OF_PRIORITIES = 3;
	private Level[] list;
	private Level.Node currentNode;
	
	public ReadyList()
	{
		list = new Level[NUM_OF_PRIORITIES];
		for(int i = 0; i < NUM_OF_PRIORITIES; i++)
			list[i] = new Level();
		
		PCB init = new PCB();
		list[0].add(init);
		currentNode = list[0].getHead();
	}
	
	public void add(String pId, int priority)
	{
		PCB newProcess = currentNode.data.createChild(pId, priority);
		list[priority].add(newProcess);
		getCurrentProcess(false);
	}
	
	public PCB getCurrentProcess(boolean isTimeout)
	{
		for(int i = NUM_OF_PRIORITIES - 1; i >= 0; i--)
		{
			//if on same priority level as current process, 
			if(i == currentNode.data.getPriority())
			{
				if(iterateLevel(isTimeout))
					return currentNode.data;
			}
			else
			{
				if(iterateLevel(list[i].getHead()))
					return currentNode.data;
			}
		}
		
		throw new IllegalStateException();
	}
	
	//iterates level starting and ending from the current running process
	private boolean iterateLevel(boolean isTimeout)
	{
		if(isTimeout)
		{
			currentNode.data.timeout();
			if(currentNode.next == null)
				currentNode = getHead(currentNode);
			else
				currentNode = currentNode.next;
		}
		
		Level.Node previousCurrent = currentNode;
		Level.Node current = currentNode;

		do
		{
			//if current is at end of level, loop back to head
			if(current == null)
				current = getHead(currentNode);
			
			if(currentNode.data.run())
			{
				currentNode = current;
				return true;
			}
			
			current = current.next;
		}
		//loop ends after iterating around back to the previous currentNode
		while(current != previousCurrent);
		
		return false;
	}
	
	//iterates level from start to end of a priority level 
	private boolean iterateLevel(Level.Node n)
	{
		while(n != null)
		{
			if(n.data.run())
			{
				currentNode.data.timeout();
				currentNode = n;
				return true;
			}
			n = n.next;
		}
		
		return false;
	}
	
	public PCB getCurrentProcess()
	{
		return currentNode.data;
	}
	
	private Level.Node getHead(Level.Node n)
	{
		return list[n.data.getPriority()].getHead();
	}
}
