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
	
	public void add(PCB p)
	{
		list[p.getPriority()].add(p);
	}
	
	public void add(String pId, int priority)
	{
		PCB newProcess = currentNode.data.createChild(pId, priority);
		list[priority].add(newProcess);
	}
	
	public void delete(String pId)
	{
		for(Level level : list)
		{
			if(level.delete(pId))
				return;
		}
	}
	
	public PCB getCurrentProcess()
	{
		for(int i = NUM_OF_PRIORITIES - 1; i >= 0; i--)
		{
			Level.Node current = list[i].getHead();
			if(current != null)
			{
				currentNode = current;
				currentNode.data.run();
				return currentNode.data;
			}
		}
		
		throw new IllegalStateException();
	}
	
	public void block()
	{
		list[currentNode.data.getPriority()].removeHead();
	}
	
	public void timeout()
	{
		currentNode.data.timeout();
		list[currentNode.data.getPriority()].timeout();
	}
	
	private Level.Node getHead(Level.Node n)
	{
		return list[n.data.getPriority()].getHead();
	}
}
