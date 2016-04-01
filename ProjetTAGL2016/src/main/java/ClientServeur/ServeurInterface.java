package ClientServeur;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServeurInterface  extends Remote{


	public boolean stockObject(String key, Object aStocker) throws RemoteException;
	
	
	public Object getObject(String key) throws RemoteException;

}
