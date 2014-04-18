import java.util.HashMap;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Controller 
{
	File input;
	HashMap<String, RCB> resources;
	Parser parser;
	ReadyList rl;
	File output;
	FileWriter fw;
	BufferedWriter bw;
	StringBuilder sb;
	
	Controller()
	{
		initIO("");
		sb = new StringBuilder();
		parser = new Parser();
	}
	
	Controller(String input)
	{
		initIO(input);
		sb = new StringBuilder();
		parser = new Parser(this.input);
	}
	
	@SuppressWarnings("incomplete-switch")
	public void run()
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
					if(!rl.getCurrentProcess().request(resources.get(current.id), resources))
						rl.block();
					break;
				case RELEASE:
					release(current.id);
					break;
				case TIMEOUT:
					rl.timeout();
					break;
				case ERROR:
					String error = "Invalid input";
					System.out.println(error);
					sb.append(error + "\n");
					break;
			}
			current = Scheduler();
		}
		
		String ended = "process terminated";
		System.out.println(ended);
		sb.append(ended);
		
		try
		{
		bw.write(sb.toString());
		bw.close();
		}
		catch(IOException e)
		{}
		parser.close();
	}
	
	private Command Scheduler()
	{
		String currentString = rl.getCurrentProcess().getPId() + " is running";
		System.out.println(currentString);
		sb.append(currentString + "\n");
		return parser.next(sb);
	}
	
	private void initIO(String input)
	{
		try
		{
			if(input.equals(""))
			{
				this.input = null;
				output = new File("output.txt");
				
			}
			else
			{
				this.input = new File(input);
				output = new File(input + "-output.txt");
			}
			
			if(!output.exists())
				output.createNewFile();
			fw = new FileWriter(output.getAbsoluteFile());
			bw = new BufferedWriter(fw);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.err.println("Input file error");
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
