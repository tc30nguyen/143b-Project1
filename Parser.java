import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser 
{
	Scanner scanner;
	
	//takes input from the console
	Parser()
	{
		scanner = new Scanner(System.in);
	}
	
	//takes input from a file
	Parser(File input)
	{
		try
		{
			scanner = new Scanner(input);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("FileNotFoundException: " + e.getMessage());
		}
	}
	
	public Command next(StringBuilder sb)
	{	
		boolean counter = false;
		String line = "";
		
		//gets the next line from input. accounts for an empty line
		if(scanner.hasNextLine())
			line = scanner.nextLine();
		while(line.isEmpty())
		{
			sb.append("\n");
			if(!scanner.hasNextLine())
			{
				if(counter)
					return new Command(Command.Type.QUIT);
				else
					counter = true;
			}
			else
				line = scanner.nextLine();
			System.out.println();
		}
		
		Scanner lineScanner = new Scanner(line);
		Command current = null;
		
		//parses the current input line
		switch(lineScanner.next())
		{
		case "init":
			current = new Command(Command.Type.INIT);
			break;
		case "quit":
			current = new Command(Command.Type.QUIT);
			break;
		case "cr":
			current = new Command(lineScanner.next(), lineScanner.nextInt());
			break;
		case "de":
			current = new Command(Command.Type.DESTROY, lineScanner.next());
			break;
		case "req":
			current = new Command(Command.Type.REQUEST, lineScanner.next());
			break;
		case "rel":
			current = new Command(Command.Type.RELEASE, lineScanner.next());
			break;
		case "to":
			current = new Command(Command.Type.TIMEOUT);
			break;
		default:
			current = new Command(Command.Type.ERROR);
		}
		
		lineScanner.close();
		return current;
	}
	
	public void close()
	{
		scanner.close();
	}
}
