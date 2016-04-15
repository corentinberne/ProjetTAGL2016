package storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Storage {
	
	private static final int MAX_NB_OBJECTS = 5;
	private HashMap<String, Object> memory;
	private HashMap<String, Long> lastUses;
	
	public HashMap<String, Object> getMemory() {
		return memory;
	}

	public Storage(){
		memory = new HashMap<String, Object>();
		lastUses = new HashMap<String, Long>();
	}
	
	public String getLRU(){
		Long LRUTime = System.currentTimeMillis();
		String LRU = "";
		Set<String> cles = lastUses.keySet();
		Iterator<String> it = cles.iterator();
		while (it.hasNext()){
			String key = it.next();
			if(lastUses.get(key) < LRUTime){
				LRUTime = lastUses.get(key);
				LRU = key;
			}
		}
		return LRU;
	}
	
	public synchronized void store(Object o, String key) throws KeyAlreadyUsedException{
		if(memory.containsKey(key)){
			throw new KeyAlreadyUsedException();
		}
		else {
			memory.put(key, o);
			lastUses.put(key, System.currentTimeMillis());
			int size = memory.size();
			if(size > MAX_NB_OBJECTS){
				String keyToRemove = getLRU();
				memory.remove(keyToRemove);
				lastUses.remove(keyToRemove);
			}
		}
	}
	
	public synchronized void remove(String key) throws NonExistingKeyException{
		if(memory.containsKey(key)){
			memory.remove(key);
			lastUses.remove(key);
		}
		else {
			throw new NonExistingKeyException();
		}
	}
	
	public synchronized Object get(String key) throws NonExistingKeyException{
		if(memory.containsKey(key)){
			lastUses.put(key, System.currentTimeMillis());
			return memory.get(key);
		}
		else {
			throw new NonExistingKeyException();
		}
	}
	
	/* Fonctions pour manipuler des listes */
	
	public synchronized void addToList(String key, Object o) throws NonExistingKeyException{
		List<Object> list = (List<Object>) get(key);
		list.add(o);
	}
	
	public synchronized void addToListAtIndex(String key, Object o, int index) throws NonExistingKeyException{
		List<Object> list = (List<Object>) get(key);
		list.add(index, o);
	}
	
	public synchronized void addAllToList(String key, Collection<Object> o) throws NonExistingKeyException{
		List<Object> list = (List<Object>) get(key);
		list.addAll(o);
	}
	
	public synchronized Object removeFromList(String key, int index) throws NonExistingKeyException{
		List<Object> list = (List<Object>) get(key);
		return list.remove(index);
	}
	
	public synchronized Object getFromList(String key, int index) throws NonExistingKeyException{
		List<Object> list = (List<Object>) get(key);
		return list.get(index);
	}
	
	public synchronized int getListSize(String key) throws NonExistingKeyException{
		List<Object> list = (List<Object>) get(key);
		return list.size();
	}
	
	public synchronized List<Object> getRangeFromList(String key, int start, int end) throws NonExistingKeyException{
		List<Object> list = (List<Object>) get(key);
		return list.subList(start, end+1);
	}
	
	public synchronized Object setInList(String key, int index, Object value) throws NonExistingKeyException{
		List<Object> list = (List<Object>) get(key);
		list.set(index, value);
		return list.get(index);
	}
	
}
