import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser 
{
	Scanner scanner;
	
	Parser()
	{
		scanner = new Scanner(System.in);
	}
	
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
		String line = scanner.nextLine();
		
		while(line.isEmpty())
		{
			System.out.println();
			sb.append("\n");
			line = scanner.nextLine();
		}
			
		Scanner lineScanner = new Scanner(line);
		Command current = null;
		
		switch(lineScanner.next())
		{
		case "init":
			current = new Command(Command.Type.INIT);
			break;
		case "quit":
			current = new Command(Command.Type.QUIT);
			break;
		//catch bad input
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
