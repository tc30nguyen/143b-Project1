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
	
	//takes input from console
	Controller()
	{
		initIO("");
		sb = new StringBuilder();
		parser = new Parser();
	}
	
	//takes input from an input file
	Controller(String input)
	{
		initIO(input);
		sb = new StringBuilder();
		parser = new Parser(this.input);
	}
	
	//initializes output file
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
	
	//runs input commands until QUIT or multiple empty lines
	@SuppressWarnings("incomplete-switch")
	public void run()
	{
		Command current = new Command(Command.Type.INIT);
		while(current.type != Command.Type.QUIT)
		{
			switch(current.type)
			{
				case INIT:
					init();
					break;
				case CREATE:
					rl.add(current.id, current.priority);
					break;
				case DESTROY:
					PCB currentProcess = rl.getCurrentProcess();
					currentProcess.delete(current.id, rl);
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
		
		//write to output file
		try
		{
			bw.write(sb.toString());
			bw.close();
			parser.close();
		}
		catch(IOException e){}
	}
	
	//prints the current running process
	private Command Scheduler()
	{
		String currentString = rl.getCurrentProcess().getPId() + " is running";
		System.out.println(currentString);
		sb.append(currentString + "\n");
		return parser.next(sb);
	}
	
	//reset state
	private void init()
	{
		rl = new ReadyList();
		
		resources = new HashMap<>();
		resources.put("R1", new RCB("R1"));
		resources.put("R2", new RCB("R2"));
		resources.put("R3", new RCB("R3"));
		resources.put("R4", new RCB("R4"));
	}
	
	//current process releases the specified resource, unblocking the next waiting process, if any
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
