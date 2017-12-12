import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


public class Server implements Message
  {
    private static Hashtable<String, ArrayList<String>> usermes = new Hashtable<String, ArrayList<String>>();
    String returnmes;

    public String WriteMessage(String user,String message,String to){

      int newmes = 0;
      String contact = user + "to" + to;
      Enumeration names;
      String key;

        // If messages have been sent add the new message to the string list associated with the key
           if(usermes.containsKey(contact)){
             System.out.println("adding new message");
             ArrayList<String> messagesList = new ArrayList<String>(usermes.get(contact));
             messagesList.add(message);
             usermes.put(contact,messagesList);
             newmes = 1;
           }

      // If this is the first message create a key and store the message in the string list associated with the key
        if(newmes == 0){
          System.out.println("adding new contact plus their message");
          ArrayList<String> messagesList = new ArrayList<String>();
          messagesList.add(message);
          usermes.put(contact, messagesList);
        }
        newmes = 0;

        return "";
    }

    public ArrayList<String> ReadMessage(String user, String from){

      ArrayList<String> messagesList = new ArrayList<String>();
      int newmes = 0;
      String message = from + "to" + user;
      Enumeration names;
      String key;

      // If users have sent messages return list
       if(usermes.containsKey(message)){
             messagesList = usermes.get(message);
             newmes = 1;
           }

      // If no messages have been sent return an empty list
        if(newmes == 0){
          ArrayList<String> nomessagesList = new ArrayList<String>();
          return nomessagesList;
        }
        return messagesList;
    }

    // Setup the server to implement message interface with stiub to export messages
    public static void main(String args[]){
        try
          { Server server = new Server();
      	    Message stub = (Message)UnicastRemoteObject.exportObject(server, 0);

      	    // Bind the remote object's stub in the registry
      	    Registry registry = LocateRegistry.getRegistry();
      	    registry.bind("MessageService", stub);

      	    System.err.println("WhatsChat Server ready");
          }
        catch (Exception e)
          { System.err.println("Server exception: " + e.toString());
	          e.printStackTrace();
          }
    }
} // Class Server
