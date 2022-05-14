package co.edu.icesi.dev.uccareapp.transport.customexeptions;

@SuppressWarnings("serial")
public class ObjectAlreadyExistException extends Exception{
	public ObjectAlreadyExistException() {
		super("This object id, already existe please check the parameters and try again");
	}
	public ObjectAlreadyExistException(String message) {
		super(message);
	}
}
