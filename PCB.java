import java.util.HashMap;

public class PCB 
{	
	private HashMap<String, RCB> resources;
	private HashMap<String, PCB> children;
	private int priority;
	private Status status;
	private PCB parent;
	private final String pId;
	
	public enum Status
	{
		READY, BLOCKED, RUNNING;
	}
	
	public PCB()
	{
		resources = null;
		children = new HashMap<>();
		priority = 0;
		status = Status.READY;
		this.parent = null;
		this.pId = "init";
	}
	
	public PCB(String pId, PCB parent, int priority)
	{
		resources = new HashMap<>();
		children = new HashMap<>();
		this.priority = priority;
		status = Status.READY;
		this.parent = parent;
		this.pId = pId;
	}
	
	public PCB createChild(String pId, int priority)
	{
		PCB child = new PCB(pId, this, priority);
		children.put(pId, child);
		return child;
	}
	
	public boolean request(String rId, HashMap<String, RCB> resources)
	{
		RCB requested = resources.get(rId);
		
		if(requested == null)
			throw new IllegalArgumentException("The RID does not exist");
		
		if(requested.allocate(this))
		{
			this.resources.put(rId, requested);
			return true;
		}
		
		else
		{
			status = Status.BLOCKED;
			return false;
		}
	}
	
	public void release(String rId, HashMap<String, RCB> resources)
	{
		RCB toRelease = resources.get(rId);
		
		if(toRelease == null)
			throw new IllegalArgumentException("The RID does not exist");
		
		this.resources.remove(rId);
		toRelease.release();
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
	
	public void unBlock()
	{
		if(status == Status.BLOCKED)
			status = Status.READY;
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
