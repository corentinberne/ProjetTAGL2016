package storage;

public class NonExistingKeyException extends Exception{

	private static final long serialVersionUID = 1L;

	public NonExistingKeyException(){
		System.err.println("Cette clé n'existe pas.");
	}
}
