public class Command 
{
	Type type;
	String id;
	int priority;
	
	public enum Type
	{
		INIT, QUIT, CREATE, DESTROY, REQUEST, RELEASE, TIMEOUT, ERROR;
	}
	
	//init, quit, timeout, error
	Command(Type ct)
	{
		this.type = ct;
	}
	
	//destroy, request, release
	Command(Type ct, String id)
	{
		this.type = ct;
		this.id = id;
	}
	
	//create
	Command(String id, int priority)
	{
		type = Type.CREATE;
		this.id = id;
		this.priority = priority;
	}
}
