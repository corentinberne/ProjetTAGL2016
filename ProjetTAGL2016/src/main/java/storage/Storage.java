package storage;

import java.util.HashMap;
import java.util.Iterator;
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
	
	public void store(Object o, String key) throws KeyAlreadyUsedException{
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
	
	public void remove(String key) throws NonExistingKeyException{
		if(memory.containsKey(key)){
			memory.remove(key);
			lastUses.remove(key);
		}
		else {
			throw new NonExistingKeyException();
		}
	}
	
	public Object get(String key) throws NonExistingKeyException{
		if(memory.containsKey(key)){
			lastUses.put(key, System.currentTimeMillis());
			return memory.get(key);
		}
		else {
			throw new NonExistingKeyException();
		}
	}
	
}
