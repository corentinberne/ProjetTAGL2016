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

    public static HashMap<String, ServeurInterface> lesServeurs = new HashMap<String, ServeurInterface>();
    public static HashMap<String, ServeurInterface> lesObjetsStockes = new HashMap<String, ServeurInterface>();
    public static Random rand = new Random();
    public static Registry registre;

    public static void main(String[] args) {

	try {
	    Scanner input = new Scanner(System.in);
	    String[] command;
	    boolean continuer = true;
	    help();
	    ServeurInterface serv;
	    while (continuer) {
		command = input.nextLine().split(" ");
		switch (command[0]) {
		case "add-serveur":
		    addServeur(input);
		    break;
		case "stock":
		    if (stockerObjet(creerObjetSimple()) == null)
			System.err.println("Erreur, pas de serveur de libre");
		    break;
		case "stock-list":
		    if (stockerObjet(creerObjetComplexe(input)) == null)
			System.err.println("Erreur, pas de serveur de libre");
		    break;
		case "retrieve":
		    System.out.println(retrieveObject(command[1]));
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
		case "lindex":
		    serv = lesObjetsStockes.get(command[1]);
		    try {
			System.out.println(serv.lIndex(command[1], Integer.parseInt(command[2])));
		    } catch (RemoteException e) {

		    }
		    break;
		case "linsert":
		    serv = lesObjetsStockes.get(command[1]);
		    try {
			serv.lInsert(command[1], command[2], Integer.parseInt(command[3]), Integer.parseInt(command[4]));
		    } catch (RemoteException e) {

		    }
		    break;
		case "llen":
		    serv = lesObjetsStockes.get(command[1]);
		    try {
			System.out.println(serv.lLen(command[1]));
		    } catch (RemoteException e) {

		    }
		    break;
		case "lpop":
		    serv = lesObjetsStockes.get(command[1]);
		    try {
			System.out.println(serv.lPop(command[1]));
		    } catch (RemoteException e) {

		    }
		    break;
		case "lpush":
		    serv = lesObjetsStockes.get(command[1]);
		    try {
			serv.lPush(command[1], creerObjetComplexe(input) );
		    } catch (RemoteException e) {

		    }
		    break;
		case "lpushx":
		    serv = lesObjetsStockes.get(command[1]);
		    try {
			serv.lPushX(command[1], command[2]);
		    } catch (RemoteException e) {

		    }
		    break;
		case "lrange":
		    serv = lesObjetsStockes.get(command[1]);
		    try {
			List<Object> l = serv.lRange(command[1], Integer.parseInt(command[2]), Integer.parseInt(command[3]));
			System.out.println("Resultat : "+l);
		    } catch (RemoteException e) {
			System.err.println("erreur");
			e.printStackTrace();
		    }
		    break;
		case "lrem":
		    serv = lesObjetsStockes.get(command[1]);
		    try {
			serv.lRem(command[1], Integer.parseInt(command[2]), Integer.parseInt(command[3]));
		    } catch (RemoteException e) {

		    }
		    break;
		case "lset":
		    serv = lesObjetsStockes.get(command[1]);
		    try {
			serv.lSet(command[1], Integer.parseInt(command[2]), Integer.parseInt(command[3]));
		    } catch (RemoteException e) {

		    }
		    break;
		case "ltrim":
		    serv = lesObjetsStockes.get(command[1]);
		    try {
			serv.lTrim(command[1], Integer.parseInt(command[2]), Integer.parseInt(command[3]));
		    } catch (RemoteException e) {

		    }
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
	System.out.println("Saisir l'adresse IP du serveur :");
	String hostName = input.nextLine();
	registre = LocateRegistry.getRegistry(hostName);
	System.out.println("Saisir le nom du serveur : ");
	String name = input.nextLine();
	ServeurInterface serveur = (ServeurInterface) registre.lookup(name);
	lesServeurs.put(name, serveur);
	System.out.println("Connexion etablie sur " + hostName + ':' + name);
    }

    public static void addServeur(String hostName, String serverName) throws AccessException, RemoteException, NotBoundException {
	registre = LocateRegistry.getRegistry(hostName);
	ServeurInterface serveur = (ServeurInterface) registre.lookup(serverName);
	lesServeurs.put(serverName, serveur);
	System.out.println("Connexion etablie sur " + hostName + ':' + serverName);
    }

    public static Object retrieveObject(String key) {
	ServeurInterface serv = lesObjetsStockes.get(key);
	try {
	    return serv.getObject(key);
	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    System.err.println("Erreur, impossible de trouver l'objet associé a la cle " + key);
	    // e.printStackTrace();
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

	Integer res = new Integer(rand.nextInt(Integer.MAX_VALUE));
	System.out.println("Création de l'entier " + res);
	return res;

    }

    public static List<Object> creerObjetComplexe(Scanner input) {

	List<Object> l = new ArrayList<Object>();
	System.out.println("Veuillez entrer les entiers a stocker ligne par ligne\n Tapez 'quit' pour terminer.");
	String line = input.nextLine();
	while (!line.equals("quit")) {
	    try {
		l.add(Integer.parseInt(line));
	    } catch (Exception e) {
	    }
	    line = input.nextLine();
	}
	return l;

    }

    public static String generateKey() {

	String res = "" + rand.nextLong();
	while (lesObjetsStockes.containsKey(res))
	    res = "" + rand.nextLong();

	return res;

    }

    private static void help() {
	System.out.println("********************* Manuel *********************");
	System.out.println("add-serveur : pour ajouter un serveur");
	System.out.println("stock : pour stocker un couple clé-valeur");
	System.out.println("stock-list: pour stocker une liste d'entiers");
	System.out.println("retrieve <key>: pour obtenir l'objet associé à une clé");
	System.out.println("-h, --help, help : pour afficher cette aide");
	System.out.println("q, quit, quitter, exit : pour terminer l'execution du client");
	System.out.println("lindex <key> <index> : get an element from a list by its index");
	System.out.println("linsert <key> <BEFORE|AFTER> <pivot> : Insert a element before of after an other element");
	System.out.println("llen <key> : get length of a list");
	System.out.println("lpop <key> : remove and get the first element in a list");
	System.out.println("lpush <key> : prepend one or multi element to a list");
	System.out.println("lpushx <key> <value> : prepend one element to a list, only if the list exists");
	System.out.println("lrange <key> <start> <stop> : get a range of elements from a list");
	System.out.println("lrem <key> <count> <value>: remove elements from a list");
	System.out.println("lset <key> <index> <value>: set the value of a element in a list by its index");
	System.out.println("ltrim <key> <start> <stop>: trim a list to a specified range");
    }

}
