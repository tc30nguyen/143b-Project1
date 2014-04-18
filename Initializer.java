public class Initializer 
{
	public static void main(String[] args)
	{
		run(args);
	}
	
	private static void run(String[] args)
	{
		Controller controller = null;
		if(args.length == 0)
			controller = new Controller();
		else
			controller = new Controller(args[0]);
		
		controller.run();
	}
}