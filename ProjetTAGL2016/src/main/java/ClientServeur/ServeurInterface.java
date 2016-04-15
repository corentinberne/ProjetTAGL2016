package ClientServeur;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServeurInterface  extends Remote{


	public boolean stockObject(String key, Object aStocker) throws RemoteException;
	
	
	public Object getObject(String key) throws RemoteException;
	
	public Object lIndex(String key, int index) throws RemoteException;
	
	public void lInsert(String key, String position, int pivot, Object value) throws RemoteException;
	
	public long lLen(String key) throws RemoteException;
	
	public Object lPop(String key) throws RemoteException;
	
	public void lPush(String key, List<Object> values) throws RemoteException;
	
	public void lPushX(String key, Object value) throws RemoteException;
	
	public List<Object> lRange(String key, int start, int stop) throws RemoteException;
	
	public void lRem(String key, int count, Object value) throws RemoteException;
	
	public void lSet(String key, int index, Object value) throws RemoteException;
	
	public void lTrim(String key, int start, int stop) throws RemoteException;

}
