import java.util.LinkedList;

public class RCB 
{
	final String rId;
	boolean allocated;
	LinkedList<PCB> blockedList;
	
	public RCB(String rId)
	{
		this.rId = rId;
		allocated = false;
		blockedList = new LinkedList<>();
	}
	
	public boolean allocate(PCB pcb)
	{
		if(allocated)
		{
			blockedList.add(pcb);
			return false;
		}
		
		allocated = true;
		return true;
	}
	
	public void release()
	{
		PCB nextProcess = blockedList.poll();
		
		if(nextProcess == null)
			allocated = false;
		else
			nextProcess.unBlock();
	}
	
	//Accesssors---------------------------------------------
	public String getRId()
	{
		return rId;
	}
	
	public boolean isAllocated()
	{
		return allocated;
	}
	
	public PCB getNextBlocked()
	{
		return blockedList.poll();
	}
}
