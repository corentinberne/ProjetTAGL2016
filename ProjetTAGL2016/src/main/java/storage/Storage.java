package storage;

import java.util.HashMap;

public class Storage {
	private HashMap<String, Object> memory;
	
	public HashMap<String, Object> getMemory() {
		return memory;
	}

	public Storage(){
		memory = new HashMap<String, Object>();
	}
	
	public void store(Object o, String key) throws KeyAlreadyUsedException{
		if(memory.containsKey(key)){
			throw new KeyAlreadyUsedException();
		}
		else {
			memory.put(key, o);
		}
	}
	
	public void remove(String key) throws NonExistingKeyException{
		if(memory.containsKey(key)){
			memory.remove(key);
		}
		else {
			throw new NonExistingKeyException();
		}
	}
	
	public Object get(String key) throws NonExistingKeyException{
		if(memory.containsKey(key)){
			return memory.get(key);
		}
		else {
			throw new NonExistingKeyException();
		}
	}
	
}
