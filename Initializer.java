public class Initializer 
{
	public static void main(String[] args)
	{
		run(args);
	}
	
	private static void run(String[] args)
	{
		Controller controller = null;
		
		//take commands through the console
		if(args.length == 0)
			controller = new Controller();
		//take commands through an input file
		else
			controller = new Controller(args[0]);
		
		controller.run();
	}
}