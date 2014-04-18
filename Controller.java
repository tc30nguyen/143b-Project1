import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;

public class Controller 
{
	File input;
	HashMap<String, RCB> resources;
	Parser parser;
	ReadyList rl;
	
	Controller()
	{
		input = null;
		parser = new Parser();
		run();
	}
	
	Controller(String input)
	{
		this.input = new File(input);
		parser = new Parser(this.input);
		run();
	}
	
	@SuppressWarnings("incomplete-switch")
	private void run()
	{
		Command current = new Command(Command.Type.INIT);
		while(current.ct != Command.Type.QUIT)
		{
			switch(current.ct)
			{
				case INIT:
					init();
					break;
				case CREATE:
					rl.add(current.id, current.priority);
					break;
				case DESTROY:
					PCB currentProcess = rl.getCurrentProcess();
					currentProcess.delete(current.id, rl); //HANDLE BLOCKED PROCESSES
					break;
				case REQUEST:
					if(!rl.getCurrentProcess().request(current.id, resources))
						rl.block();
					break;
				case RELEASE:
					release(current.id);
					break;
				case TIMEOUT:
					rl.timeout();
					break;
				case ERROR:
					System.out.println("Invalid input");
					break;
			}
			current = Scheduler();
		}
	}
	
	private Command Scheduler()
	{
		String currentProcess = rl.getCurrentProcess().getPId();
		System.out.println(currentProcess + " is running");
		return parser.next();
	}
	
	private void init()
	{
		rl = new ReadyList();
		
		resources = new HashMap<>();
		resources.put("R1", new RCB("R1"));
		resources.put("R2", new RCB("R2"));
		resources.put("R3", new RCB("R3"));
		resources.put("R4", new RCB("R4"));
	}
	
	private void release(String rId)
	{
		RCB toRelease = resources.get(rId);
		if(rl.getCurrentProcess().release(rId))
		{
			//give newly released resource to the next process in its blocked list
			PCB releasedProcess = toRelease.release();
			if(releasedProcess != null)
			{
				releasedProcess.unBlock(toRelease);
				rl.add(releasedProcess);
			}
		}
	}
}
