public class Command 
{
	Type ct;
	String id;
	int priority;
	
	public enum Type
	{
		INIT, QUIT, CREATE, DESTROY, REQUEST, RELEASE, TIMEOUT, ERROR;
	}
	
	//init, quit, timeout, error
	Command(Type ct)
	{
		this.ct = ct;
	}
	
	//destroy, request, release
	Command(Type ct, String id)
	{
		this.ct = ct;
		this.id = id;
	}
	
	//create command
	Command(String id, int priority)
	{
		ct = Type.CREATE;
		this.id = id;
		this.priority = priority;
	}
}
