package co.edu.icesi.dev.uccareapp.transport.customexeptions;

import java.security.InvalidAlgorithmParameterException;

@SuppressWarnings("serial")
public class InvalidValueException extends InvalidAlgorithmParameterException{
	public InvalidValueException() {
		super("The value is not allowed here, please edit and try again");
	}
	public InvalidValueException(String message) {
		super(message);
	}
}
