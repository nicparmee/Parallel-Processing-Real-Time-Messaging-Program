 // @author Nic

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;


public interface Message extends Remote // Methods for remote interface
  {
    public String WriteMessage (String user ,String message, String to)
     throws RemoteException;
     public ArrayList<String> ReadMessage (String user , String from)
      throws RemoteException;

}
