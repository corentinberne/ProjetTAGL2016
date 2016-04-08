import org.junit.Before;
import org.junit.Test;

import ClientServeur.Client;
import storage.KeyAlreadyUsedException;
import storage.NonExistingKeyException;
import storage.Storage;

import static org.junit.Assert.*;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Scanner;

public class ClientTest {

    Client c;
    Scanner input;
    @Before
    public void clientCreation() {
	c = new Client();
	input = new Scanner(System.in);
	try {
	    c.addServeur("localhost","serveur56");
	} catch (AccessException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (NotBoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Test
    public void basicTest() {
	Integer i = c.creerObjetSimple();
	String key = c.stockerObjet(i);
	assertNotNull(key);
	Object j = c.retrieveObject(key);
	assertEquals(i, j);
    }

    @Test
    public void multipleServeurs() {
	try {
	    String serv1 = c.lesServeurs.keySet().iterator().next();
	    c.addServeur("localhost","serveur33");
	    Iterator<String> it = c.lesServeurs.keySet().iterator();
	    it.next();
	    String serv2 = it.next();
	    
	    Integer i = c.creerObjetSimple();
	    String key = c.stockerObjet(serv1,i);
	    assertNotNull(key);
	    Object j = c.retrieveObject(key);
	    assertEquals(i, j);
	    	    
	    Integer i2 = c.creerObjetSimple();
	    String key2 = c.stockerObjet(serv2,i2);
	    assertNotNull(key2);
	    Object j2 = c.retrieveObject(key2);
	    assertEquals(i2, j2);
	    
	    Integer i3 = c.creerObjetSimple();
	    String key3 = c.stockerObjet(serv1,i3);
	    assertNotNull(key3);
	    Object j3 = c.retrieveObject(key3);
	    assertEquals(i3, j3);
	    
	} catch (AccessException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (NotBoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }

}
