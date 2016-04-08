package storage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Storage {
	
	private static final int MAX_MEMORY = 124780560;
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
			Runtime r = Runtime.getRuntime();
			Long usedMemory = r.totalMemory();
			memory.put(key, o);
			lastUses.put(key, System.currentTimeMillis());
			System.out.println(usedMemory);
			System.out.println(r.freeMemory());
			System.out.println(r.maxMemory());
			while(usedMemory > MAX_MEMORY){
				memory.remove(getLRU());
				lastUses.remove(getLRU());
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
