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
					create(current.id, current.priority);
					break;
				case DESTROY:
					destroy(current.id);
					break;
				case REQUEST:
					request(current.id);
					break;
				case RELEASE:
					release(current.id);
					break;
				case TIMEOUT:
					timeout();
					break;
				case ERROR:
					System.out.println("Invalid input");
					break;
			}
			
			System.out.println(rl.getCurrentProcess().getPId() + " is running");
			current = parser.next();
		}
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
	
	private void create(String pId, int priority)
	{
		rl.add(pId, priority);
	}
	
	private void destroy(String pId)
	{
		
	}
	
	private void request(String rId)
	{
		rl.getCurrentProcess().request(rId, resources);
	}
	
	private void release(String rId)
	{
		rl.getCurrentProcess().release(rId, resources);
	}
	
	private void timeout()
	{
		rl.getCurrentProcess(true);
	}
}
