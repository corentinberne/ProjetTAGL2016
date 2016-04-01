package storage;

public class KeyAlreadyUsedException extends Exception{

	private static final long serialVersionUID = 1L;

	public KeyAlreadyUsedException(){
		System.err.println("Clé déjà utilisée, merci d'en utiliser une autre.");
	}
}
