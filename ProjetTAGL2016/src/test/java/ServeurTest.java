import org.junit.Before;
import org.junit.Test;

import ClientServeur.Serveur;
import storage.KeyAlreadyUsedException;
import storage.NonExistingKeyException;
import storage.Storage;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ServeurTest {
    Serveur s;
    String toStock = "chaine1";
    String key = "cleChaine1";

    @Before
    public void storageCreation() {
	s = new Serveur();
    }

    @Test
    public void basicTest() {
	Integer i = 8;
	try {
	    s.stockObject("cle", i);
	    assertTrue(i == s.getObject("cle"));
	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Test
    public void basicTestList() {
	ArrayList<Integer> l = new ArrayList<Integer>();
	l.add(78);
	l.add(99);
	try {
	    s.stockObject("cle", l);
	    List r = (List) s.getObject("cle");
	    assertTrue(r.get(0) == l.get(0) && r.get(1) == l.get(1));
	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Test
    public void testlIndex() {
	ArrayList<Integer> l = new ArrayList<Integer>();
	l.add(78);
	l.add(99);
	l.add(499);
	l.add(979);

	try {
	    s.stockObject("cle", l);
	    assertTrue(s.lIndex("cle", 2) == l.get(2));
	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @Test
    public void testlInsert() {

	ArrayList<Integer> l = new ArrayList<Integer>();
	l.add(78);
	l.add(99);
	l.add(499);
	l.add(979);

	try {
	    s.stockObject("cle", l);
	    s.lInsert("cle", "AFTER", 3, 1);
	    List r = (List) s.getObject("cle");
	    assertTrue((Integer) r.get(4) == 1);

	    s.lInsert("cle", "AFTER", 5, 70);
	    List r2 = (List) s.getObject("cle");
	    assertTrue((Integer) r2.get(5) == 70);

	    s.lInsert("cle", "BEFORE", 0, 8);
	    List r3 = (List) s.getObject("cle");
	    assertTrue((Integer) r3.get(0) == 8);

	    s.lInsert("cle", "BEFORE", 1, 42);
	    List r4 = (List) s.getObject("cle");
	    assertTrue((Integer) r4.get(1) == 42);

	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @Test
    public void testlPush() {
	ArrayList<Integer> l = new ArrayList<Integer>();
	l.add(78);
	l.add(99);
	l.add(499);
	l.add(979);
	ArrayList<Object> l2 = new ArrayList<Object>();
	l2.add(1);
	l2.add(2);

	try {
	    s.stockObject("cle", l);
	    s.lPush("cle", l2);
	    List r = (List) s.getObject("cle");
	    assertTrue((Integer) r.get(0) == 1);
	    assertTrue((Integer) r.get(1) == 2);
	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Test
    public void testlPushX() {
	ArrayList<Integer> l = new ArrayList<Integer>();
	l.add(78);
	l.add(99);
	l.add(499);
	l.add(979);
	try {
	    s.stockObject("cle", l);
	    s.lPushX("cle", 42);
	    List r = (List) s.getObject("cle");
	    assertTrue((Integer) r.get(0) == 42);
	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Test
    public void testlLen() {
	ArrayList<Integer> l = new ArrayList<Integer>();
	l.add(78);
	l.add(99);
	l.add(499);
	l.add(979);
	try {
	    s.stockObject("cle", l);
	    assertTrue(s.lLen("cle") == 4);
	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Test
    public void testlPop() {

	ArrayList<Integer> l = new ArrayList<Integer>();
	l.add(78);
	l.add(99);
	l.add(499);
	l.add(979);
	try {
	    s.stockObject("cle", l);
	    Integer i = (Integer) s.lPop("cle");
	    assertTrue(i == 78);

	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @Test
    public void testlRange() {

	ArrayList<Integer> l = new ArrayList<Integer>();
	l.add(9);
	l.add(8);
	l.add(7);
	l.add(6);
	l.add(5);
	l.add(4);
	l.add(3);
	l.add(2);
	l.add(1);

	try {
	    s.stockObject("cle", l);
	    List<Object> range = s.lRange("cle", 3, 6);
	    List<Object> range2 = s.lRange("cle", 6, 3);
	    List<Object> attendu = new ArrayList<Object>();
	    attendu.add(6);
	    attendu.add(5);
	    attendu.add(4);
	    attendu.add(3);
	    for (int i = 0; i < attendu.size(); i++)
		assertTrue(range.get(i) == attendu.get(i) && range2.get(i) == attendu.get(i));
	    

	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @Test
    public void testlRem() {
	ArrayList<Integer> l = new ArrayList<Integer>();
	l.add(9);
	l.add(8);
	l.add(7);
	l.add(6);
	l.add(5);
	l.add(9);
	l.add(4);
	l.add(3);
	l.add(2);
	l.add(1);
	l.add(9);

	try {
	    s.stockObject("cle", l);
	    s.lRem("cle", 1, 9);
	    int nbVal = 0;
	    for (int i = 0; i < s.lLen("cle"); i++) {
		if ((Integer) s.lIndex("cle", i) == 9)
		    nbVal++;
	    }
	    assertTrue(nbVal == 2);
	    s.lRem("cle", 2, 9);
	    nbVal = 0;
	    for (int i = 0; i < s.lLen("cle"); i++) {
		if ((Integer) s.lIndex("cle", i) == 9)
		    nbVal++;
	    }
	    assertTrue(nbVal == 0);

	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Test
    public void testlSet() {
	ArrayList<Integer> l = new ArrayList<Integer>();
	l.add(9);
	l.add(8);
	l.add(7);
	l.add(6);
	l.add(5);
	l.add(9);
	l.add(4);
	l.add(3);
	l.add(2);
	l.add(1);
	l.add(9);

	try {
	    s.stockObject("cle", l);
	    s.lSet("cle", 4, 12);
	    assertTrue((Integer)s.lIndex("cle", 4) == 12);
	    s.lSet("cle", 36, 12);

	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Test
    public void testLTrim() {
	ArrayList<Integer> l = new ArrayList<Integer>();
	l.add(9);
	l.add(8);
	l.add(7);
	l.add(6);
	l.add(5);
	l.add(4);
	l.add(3);
	l.add(2);
	l.add(1);

	try {
	    s.stockObject("cle", l);
	    s.lTrim("cle", 1, 7);
	    List<Object> attendu = new ArrayList<Object>();
	    attendu.add(8);
	    attendu.add(7);
	    attendu.add(6);
	    attendu.add(5);
	    attendu.add(4);
	    attendu.add(3);
	    attendu.add(2);
	    List<Object> range = s.lRange("cle", 0, (int)s.lLen("cle")-1);
	    for (int i = 0; i < attendu.size(); i++)
		assertTrue(range.get(i) == attendu.get(i));
	    
	    s.lTrim("cle", 5, 2);
	    List<Object> attendu2 = new ArrayList<Object>();
	    attendu.add(6);
	    attendu.add(5);
	    attendu.add(4);
	    attendu.add(3);
	    List<Object> range2 = s.lRange("cle", 0, (int)s.lLen("cle"));
	    for (int i = 0; i < attendu2.size(); i++)
		assertTrue(range2.get(i) == attendu2.get(i));

	} catch (RemoteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
