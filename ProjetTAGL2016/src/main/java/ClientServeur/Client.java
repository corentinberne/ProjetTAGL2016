package ClientServeur;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Client {

    static List<ServeurInterface> lesServeurs;
    static HashMap<String, ServeurInterface> lesObjetsStockes;
    static Random rand = new Random();
    static Registry registre;

    public static void main(String[] args) {

	lesServeurs = new ArrayList<ServeurInterface>();
	lesObjetsStockes = new HashMap<String, ServeurInterface>();

	try {
	    Scanner input = new Scanner(System.in);
	    String host = "localhost";
	    registre = LocateRegistry.getRegistry(host);
	    String command;
	    boolean continuer = true;
	    while (continuer) {
		command = input.nextLine();
		switch (command) {
		case "add server":addServeur(input);
		case "q":
		case "quit":
		case "quitter":
		case "exit":
		    continuer = false;
		    break;
		default:
		    break;
		}
	    }

	    System.exit(0);

	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    public static void addServeur(Scanner input) throws AccessException, RemoteException, NotBoundException {
	System.out.println("Nom du serveur : ");
	ServeurInterface serveur = (ServeurInterface) registre.lookup(input.nextLine());
	lesServeurs.add(serveur);
    }

    public static Object retrieveObject(String key) {
	ServeurInterface serv = lesObjetsStockes.get(key);
	try {
	    return serv.getObject(key);
	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return null;
	}
    }

    public static boolean stockerObjet(Object o) {

	String key = generateKey();

	for (ServeurInterface courant : lesServeurs) {
	    try {
		if (courant.stockObject(key, o))
		    return true;
	    } catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	return false;
    }

    public static Integer creerObjetSimple() {

	return new Integer(rand.nextInt(Integer.MAX_VALUE));

    }

    public static String generateKey() {

	return "" + rand.nextLong();

    }

}
