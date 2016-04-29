import org.junit.Before;
import org.junit.Test;

import storage.KeyAlreadyUsedException;
import storage.NonExistingKeyException;
import storage.Storage;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

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
			Thread.sleep(2);
			s.store("chaine2", "cleChaine2");
			Thread.sleep(2);
			s.store("chaine3", "cleChaine3");
			Thread.sleep(2);
			s.store("chaine4", "cleChaine4");
			Thread.sleep(2);
			s.store("chaine5", "cleChaine5");
			assertTrue(s.getMemory().size()==5);
			s.store("chaine6", "cleChaine6");
		} catch (Exception e) {
			e.printStackTrace();
		}
		s.get(key);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void LRUGetTest() throws NonExistingKeyException{
		assertTrue(s.getMemory().isEmpty());
		try {
			s.store(toStock, key);
			Thread.sleep(2);
			s.store("chaine2", "cleChaine2");
			Thread.sleep(2);
			s.store("chaine3", "cleChaine3");
			Thread.sleep(2);
			s.get(key);
			Thread.sleep(2);
			s.store("chaine4", "cleChaine4");
			Thread.sleep(2);
			s.store("chaine5", "cleChaine5");
			assertTrue(s.getMemory().size()==5);
			s.store("chaine6", "cleChaine6");
		} catch (Exception e) {
			e.printStackTrace();
		}
		s.get("cleChaine2");
	}
	
	@Test
	public void listTest(){
		assertTrue(s.getMemory().isEmpty());
		try {
			List<Object> list = new LinkedList<>();
			list.add(1);
			s.store(list, key);
			assertTrue(s.getMemory().size()==1);
			assertTrue(s.getListSize(key)==1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			s.addToList(key, 2);
			assertTrue(s.getListSize(key)==2);
			s.removeFromList(key, 0);
			assertTrue(s.getListSize(key)==1);
			assertTrue((int) s.getFromList(key, 0)==2);
			LinkedList<Object> listToAdd = new LinkedList<Object>();
			listToAdd.add(3);
			listToAdd.add(4);
			listToAdd.add(5);
			s.addAllToList(key, listToAdd);
			assertTrue((int) s.setInList(key, 3, 6)==6);
			assertTrue(s.getListSize(key)==4);
			List<Object> subList = s.getRangeFromList(key, 1, 2);
			assertTrue(subList.size()==2);
			assertTrue((int) subList.get(0)==3);
			assertTrue((int) subList.get(1)==4);
			s.addToListAtIndex(key, 12, 2);
			assertTrue(s.getListSize(key)==5);
			assertTrue((int) s.getFromList(key, 2)==12);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void addToNonExistingKey() throws NonExistingKeyException{
		s.addToList(key, 2);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void addToNonListObject() throws NonExistingKeyException{
		try {
			s.store(toStock, key);
		} catch (KeyAlreadyUsedException e) {
			e.printStackTrace();
		}
		s.addToList(key, 2);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void addAtIndexToNonExistingKey() throws NonExistingKeyException{
		s.addToListAtIndex(key, 2, 3);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void addAtIndexToNonListObject() throws NonExistingKeyException{
		try {
			s.store(toStock, key);
		} catch (KeyAlreadyUsedException e) {
			e.printStackTrace();
		}
		s.addToListAtIndex(key, 2, 3);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void addAllToNonExistingKey() throws NonExistingKeyException{
		LinkedList<Object> listToAdd = new LinkedList<Object>();
		listToAdd.add(3);
		listToAdd.add(4);
		listToAdd.add(5);
		s.addAllToList(key, listToAdd);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void addAllToNonListObject() throws NonExistingKeyException{
		try {
			s.store(toStock, key);
		} catch (KeyAlreadyUsedException e) {
			e.printStackTrace();
		}
		LinkedList<Object> listToAdd = new LinkedList<Object>();
		listToAdd.add(3);
		listToAdd.add(4);
		listToAdd.add(5);
		s.addAllToList(key, listToAdd);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void removeFromNonExistingKey() throws NonExistingKeyException{
		s.removeFromList(key, 2);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void removeFromNonListObject() throws NonExistingKeyException{
		try {
			s.store(toStock, key);
		} catch (KeyAlreadyUsedException e) {
			e.printStackTrace();
		}
		s.removeFromList(key, 2);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void getFromNonExistingKey() throws NonExistingKeyException{
		s.getFromList(key, 2);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void getFromNonListObject() throws NonExistingKeyException{
		try {
			s.store(toStock, key);
		} catch (KeyAlreadyUsedException e) {
			e.printStackTrace();
		}
		s.getFromList(key, 2);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void getListSizeFromNonExistingKey() throws NonExistingKeyException{
		s.getListSize(key);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void getListSizeFromNonListObject() throws NonExistingKeyException{
		try {
			s.store(toStock, key);
		} catch (KeyAlreadyUsedException e) {
			e.printStackTrace();
		}
		s.getListSize(key);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void getRangeFromNonExistingKey() throws NonExistingKeyException{
		s.getRangeFromList(key, 1, 3);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void getRangeFromNonListObject() throws NonExistingKeyException{
		try {
			s.store(toStock, key);
		} catch (KeyAlreadyUsedException e) {
			e.printStackTrace();
		}
		s.getRangeFromList(key, 1, 3);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void setInNonExistingKey() throws NonExistingKeyException{
		s.setInList(key, 1, 3);
	}
	
	@Test(expected = NonExistingKeyException.class)
	public void setInNonListObject() throws NonExistingKeyException{
		try {
			s.store(toStock, key);
		} catch (KeyAlreadyUsedException e) {
			e.printStackTrace();
		}
		s.setInList(key, 1, 3);
	}
}
