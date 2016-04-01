package ClientServeur;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class Serveur implements ServeurInterface {

    static Random rand = new Random();
    
    @Override
    public boolean stockObject(String key, Object aStocker) throws RemoteException {
	// TODO Auto-generated method stub
	return true;
    }

    @Override
    public Object getObject(String key) throws RemoteException {
	// TODO Auto-generated method stub
	return null;
    }

    public static void main(String[] args) {

	try {
	    Serveur moi = new Serveur();
	    ServeurInterface h_stub = (ServeurInterface) UnicastRemoteObject.exportObject(moi, 0);
	    Registry registre = LocateRegistry.getRegistry();
	    String name = "serveur"+rand.nextInt(100);
	    registre.bind(name, h_stub);
	    System.out.println("Je suis " + name);
	} catch (Exception e) {
	    System.err.println("Erreur sur serveur : ");
	    e.printStackTrace();
	}

    }

}
