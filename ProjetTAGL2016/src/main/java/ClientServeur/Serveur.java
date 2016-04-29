package ClientServeur;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
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
	    String name = "serveur" + rand.nextInt(100);
	    registre.bind(name, h_stub);
	    System.out.println("Nom du serveur " + name);
	} catch (Exception e) {
	    System.err.println("Erreur sur serveur : ");
	    e.printStackTrace();
	}

    }

    @Override
    public Object lIndex(String key, int index) throws RemoteException {
	try {
	    return storage.getFromList(key, index);
	} catch (NonExistingKeyException e) {
	    return null;
	}
    }

    @Override
    public void lInsert(String key, String position, int pivot, Object value) throws RemoteException {
	try {
	    if (position.equals("BEFORE") && pivot > 0)
		this.storage.addToListAtIndex(key, value, pivot);
	    else if (position.equals("BEFORE") && pivot == 0)
		this.storage.addToListAtIndex(key, value, 0);
	    else if (pivot < this.storage.getListSize(key))
		this.storage.addToListAtIndex(key, value, pivot + 1);
	    else if (pivot == this.storage.getListSize(key))
		this.storage.addToList(key, value);
	} catch (NonExistingKeyException e) {
	}
    }

    @Override
    public long lLen(String key) throws RemoteException {
	try {
	    return storage.getListSize(key);
	} catch (NonExistingKeyException e) {
	    return -1;
	}
    }

    @Override
    public Object lPop(String key) throws RemoteException {
	try {
	    Object res = this.storage.getFromList(key, 0);
	    this.storage.removeFromList(key, 0);
	    return res;
	} catch (NonExistingKeyException e) {
	    return null;
	}
    }

    @Override
    public void lPush(String key, List<Object> values) throws RemoteException {
	try {
	    for (int i = values.size() - 1; i > -1; i--)
		this.storage.addToListAtIndex(key, values.get(i), 0);
	} catch (NonExistingKeyException e) {
	    try {
		this.storage.store(values, key);
	    } catch (KeyAlreadyUsedException e1) {
	    }
	}

    }

    @Override
    public void lPushX(String key, Object value) throws RemoteException {
	try {
	    this.storage.addToListAtIndex(key, value, 0);
	} catch (NonExistingKeyException e) {
	}

    }

    @Override
    public List<Object> lRange(String key, int start, int stop) throws RemoteException {
	
	if(start < 0 || stop >= lLen(key) || start > lLen(key) || stop < 0)
	    return null;
	
	try {
	    if(start <= stop)
		return this.storage.getRangeFromList(key, start, stop);
	    else
		return this.storage.getRangeFromList(key, stop, start);
	} catch (NonExistingKeyException e) {
	}
	return null;
    }

    @Override
    public void lRem(String key, int count, Object value) throws RemoteException {
	int countSupp = 0;
	try {
	    int size = this.storage.getListSize(key);
	    if (count > 0) {
		for (int i = 0; i < size; i++) {
		    if (this.storage.getFromList(key, i).equals(value) && countSupp < count){
			countSupp++;
			this.storage.removeFromList(key, i);
			i--;
			size--;
		    }
		}
	    } else if (count < 0) {
		for (int i = size - 1; i > 0; i--) {
		    if (this.storage.getFromList(key, i).equals(value) && countSupp > count){
			countSupp--;
			this.storage.removeFromList(key, i);
			i++;
		    }
		}
	    } else {
		for (int i = 0; i < size; i++) {
		    if (this.storage.getFromList(key, i).equals(value)){
			this.storage.removeFromList(key, i);
			i--;
			size--;
		    }
		}
	    }
	} catch (NonExistingKeyException e) {
	}
    }

    @Override
    public void lSet(String key, int index, Object value) throws RemoteException {
	try {
	    
	    if(index < 0 || index >= lLen(key))
		    return;
	    
	    this.storage.setInList(key, index, value);
	} catch (NonExistingKeyException e) {
	}
    }

    @Override
    public void lTrim(String key, int start, int stop) throws RemoteException {

	if(start < 0 || stop >= lLen(key) || start > lLen(key) || stop < 0)
	    return;
	
	try {

	    int borneSup = (start < stop) ? stop : start;
	    int borneInf = (start < stop) ? start : stop;
	    
	    for (int i = this.storage.getListSize(key) - 1; i > borneSup; i--)
		this.storage.removeFromList(key, i);

	    for (int i = 0; i < borneInf; i++)
		this.storage.removeFromList(key, 0);
	} catch (Exception e) {
	}
    }

}
