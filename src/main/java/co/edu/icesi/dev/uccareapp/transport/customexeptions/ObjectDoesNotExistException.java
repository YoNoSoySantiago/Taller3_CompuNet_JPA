package co.edu.icesi.dev.uccareapp.transport.customexeptions;

@SuppressWarnings("serial")
public class ObjectDoesNotExistException extends Exception {
	public ObjectDoesNotExistException(String message) {
		super(message);
	}

}
