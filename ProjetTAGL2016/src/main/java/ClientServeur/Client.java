package ClientServeur;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Client {

    public static HashMap<String, ServeurInterface> lesServeurs = new HashMap<String, ServeurInterface>();
    public static HashMap<String, ServeurInterface> lesObjetsStockes = new HashMap<String, ServeurInterface>();
    public static Random rand = new Random();
    public static Registry registre;

    public static void main(String[] args) {


	try {
	    Scanner input = new Scanner(System.in);
	    String command;
	    boolean continuer = true;
	    while (continuer) {
		command = input.nextLine();
		switch (command) {
		case "add serveur":
		    addServeur(input);
		    break;
		case "stock":
		    stockerObjet(creerObjetSimple());
		    break;
		case "retrieve":
		    System.out.println(retrieveObject(input.nextLine()));
		    break;
		case "-h":
		case "--help":
		case "help":
		    help();
		    break;
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
	System.out.println("Saisir l'adresse du serveur :");
	String hostName = input.nextLine();
	registre = LocateRegistry.getRegistry(hostName);
	System.out.println("Saisir le nom du serveur : ");
	String name = input.nextLine();
	ServeurInterface serveur = (ServeurInterface) registre.lookup(name);
	lesServeurs.put(name, serveur);
   	System.out.println("Connexion etablie sur " + hostName+':' + name);
    }

    public static void addServeur(String hostName, String serverName) throws AccessException, RemoteException, NotBoundException {
   	registre = LocateRegistry.getRegistry(hostName);
   	ServeurInterface serveur = (ServeurInterface) registre.lookup(serverName);
   	lesServeurs.put(serverName, serveur);
   	System.out.println("Connexion etablie sur " + hostName+':' + serverName);
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

    public static String stockerObjet(Object o) {

	String key = generateKey();
	System.out.println("La clef de l'objet est : " + key);
	for (ServeurInterface courant : lesServeurs.values()) {
	    try {
		if (courant.stockObject(key, o)) {
		    lesObjetsStockes.put(key, courant);
		    return key;
		}
	    } catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	return null;
    }

    public static String stockerObjet(String nameServeur, Object o) {

	String key = generateKey();
	System.out.println("La clef de l'objet est : " + key);
	ServeurInterface courant = lesServeurs.get(nameServeur);
	try {
	    if (courant.stockObject(key, o)) {
		lesObjetsStockes.put(key, courant);
		return key;
	    }
	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return null;
    }

    public static Integer creerObjetSimple() {

	return new Integer(rand.nextInt(Integer.MAX_VALUE));

    }

    public static String generateKey() {

	return "" + rand.nextLong();

    }

    private static void help() {
	System.out.println("********************* Manuel *********************");
	System.out.println("add serveur : pour ajouter un serveur");
	System.out.println("stock : pour stocker un couple clé-valeur");
	System.out.println("retrieve : pour obtenir la valeur associé à une clé");
	System.out.println("-h, --help, help : pour afficher cette aide");
	System.out.println("q, quit, quitter, exit : pour terminer l'execution du client");
    }

}
