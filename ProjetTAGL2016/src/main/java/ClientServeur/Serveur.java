package ClientServeur;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

import storage.KeyAlreadyUsedException;
import storage.NonExistingKeyException;
import storage.Storage;

public class Serveur implements ServeurInterface {

    static Random rand = new Random();
    
    private Storage storage = new Storage();
    
    @Override
    public boolean stockObject(String key, Object aStocker) throws RemoteException {
	try {
	    storage.store(aStocker, key);
	} catch (KeyAlreadyUsedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    @Override
    public Object getObject(String key) throws RemoteException {
	// TODO Auto-generated method stub
	try {
	    return storage.get(key);
	} catch (NonExistingKeyException e) {
	    return null;
	}
    }

    public static void main(String[] args) {

	try {
	    Serveur moi = new Serveur();
	    Registry registre = LocateRegistry.getRegistry();
	    ServeurInterface h_stub = (ServeurInterface) UnicastRemoteObject.exportObject(moi, 0);
	    String name = "serveur"+rand.nextInt(100);
	    registre.bind(name, h_stub);
	    System.out.println("Je suis " + name);
	} catch (Exception e) {
	    System.err.println("Erreur sur serveur : ");
	    e.printStackTrace();
	}

    }

}
