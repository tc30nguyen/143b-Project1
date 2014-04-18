import java.util.HashMap;

public class PCB 
{	
	private HashMap<String, RCB> resources;
	private HashMap<String, PCB> children;
	private int priority;
	private Status status;
	private PCB parent;
	private final String pId;
	private RCB blockedBy;
	
	public enum Status
	{
		READY, BLOCKED, RUNNING;
	}
	
	public PCB()
	{
		resources = new HashMap<>();
		children = new HashMap<>();
		priority = 0;
		status = Status.READY;
		this.parent = null;
		this.pId = "init";
		blockedBy = null;
	}
	
	public PCB(String pId, PCB parent, int priority)
	{
		resources = new HashMap<>();
		children = new HashMap<>();
		this.priority = priority;
		status = Status.READY;
		this.parent = parent;
		this.pId = pId;
		blockedBy = null;
	}
	
	public PCB createChild(String pId, int priority)
	{
		PCB child = new PCB(pId, this, priority);
		children.put(pId, child);
		return child;
	}
	
	public boolean delete(String pId, ReadyList rl)
	{
		if(this.pId.equals(pId))
		{
			delete(rl);
			return true;
		}
		else
		{
			for(PCB child : children.values())
			{
				if(child.delete(pId, rl))
					return true;
			}
		}
		
		return false;
	}
	
	public void delete(ReadyList rl)
	{
		for(PCB child : children.values())
			child.delete(rl);
		
		for(RCB resource : resources.values())
		{
			PCB unBlockedProcess = resource.release();
			if(unBlockedProcess != null)
				rl.add(unBlockedProcess);
		}
		
		if(status == Status.BLOCKED)
			blockedBy.deleteFromBlock(this);
		
		rl.delete(pId);
	}
	
	public boolean request(RCB resource, HashMap<String, RCB> resources)
	{
		RCB requested = resources.get(resource.getRId());
		
		if(requested == null)
			throw new IllegalArgumentException("The RID does not exist");
		
		if(this.resources.containsKey(resource.getRId()))
			return true;
		
		if(requested.allocate(this))
		{
			this.resources.put(resource.getRId(), requested);
			return true;
		}
		
		else
		{
			status = Status.BLOCKED;
			blockedBy = resource;
			return false;
		}
	}
	
	public boolean release(String rId)
	{
		if(resources.containsKey(rId))
		{
			resources.remove(rId);
			return true;
		}
		
		return false;
	}
	
	//Status control-----------------------------------------------------
	public void timeout()
	{
		if(status == Status.RUNNING)
			status = Status.READY;
	}
	public boolean run()
	{
		if(status == Status.READY || status == Status.RUNNING)
		{
			status = Status.RUNNING;
			return true;
		}
		
		return false;
	}
	
	public void unBlock(RCB resource)
	{
		if(status == Status.BLOCKED)
		{
			resources.put(resource.getRId(), resource);
			status = Status.READY;
		}
	}
	
	//Accessors----------------------------------------------------------
	public HashMap<String, RCB> getResources()
	{
		return resources;
	}
	
	public HashMap<String, PCB> getChildren()
	{
		return children;
	}
	
	public int getPriority()
	{
		return priority;
	}
	
	public Status getStatus()
	{
		return status;
	}
	
	public PCB getParent()
	{
		return parent;
	}
	
	public String getPId()
	{
		return pId;
	}
}
