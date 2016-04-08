import org.junit.Before;
import org.junit.Test;

import storage.KeyAlreadyUsedException;
import storage.NonExistingKeyException;
import storage.Storage;

import static org.junit.Assert.*;

public class StorageTest {
	Storage s;
	String toStock = "chaine1";
	String key = "cleChaine1";
	
	@Before
	public void storageCreation(){
		s = new Storage();
	}
	
	@Test
	public void basicTest(){
		assertTrue(s.getMemory().isEmpty());
		try {
			s.store(toStock, key);
			assertTrue(s.getMemory().size()==1);
		} catch (KeyAlreadyUsedException e) {
			e.printStackTrace();
		}
		
		try {
			Object o = s.get(key);
			assertTrue(o.equals(toStock));
		} catch (NonExistingKeyException e) {
			e.printStackTrace();
		}	
		
		try {
			s.remove(key);
			assertTrue(s.getMemory().isEmpty());
		} catch (NonExistingKeyException e) {
			e.printStackTrace();
		}
	}
	
	@Test(expected = KeyAlreadyUsedException.class)
	public void storeFailTest() throws KeyAlreadyUsedException{
		s.store(toStock, key);
		assertTrue(s.getMemory().size()==1);
		s.store("chaineNonStockee", key);		
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void getFailTest() throws NonExistingKeyException{
		s.get(key);	
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void removeFailTest() throws NonExistingKeyException{
		s.remove(key);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void LRUStoreTest() throws NonExistingKeyException{
		assertTrue(s.getMemory().isEmpty());
		try {
			s.store(toStock, key);
			s.store("chaine2", "cleChaine2");
			s.store("chaine3", "cleChaine3");
			s.store("chaine4", "cleChaine4");
			s.store("chaine5", "cleChaine5");
			assertTrue(s.getMemory().size()==5);
			s.store("chaine6", "cleChaine6");
		} catch (Exception e) {
			e.printStackTrace();
		}
		s.get(key);
	}
}
