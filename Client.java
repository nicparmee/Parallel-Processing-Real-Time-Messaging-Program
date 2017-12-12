import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.*;
import java.io.*;
import java.util.concurrent.*;

public class Client
  {
    private static String user = "";
    private static String contact = "";
    private static int oldlength;
    private static String host;
    private static Registry registry;
    private static Message stub;

    public static void main (String args[]) throws InterruptedException
      {
        host = (args.length < 1) ? null : args[0];
        //Connect with Server
        try{
         registry = LocateRegistry.getRegistry(host);
         stub = (Message) registry.lookup("MessageService");
           }catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();}

        ExecutorService service = Executors.newFixedThreadPool(2); // Two processes to handle reading and writing messages simultaneously

        System.out.println("Welcome to WhatsChat");
        System.out.println("1) Contact - change who you are sending messages to");
        System.out.println("2) Exit - Terminate messaging app");
        System.out.println("Input user name...");                    // Once name is defined can't change user until restart
        user = System.console().readLine();

        System.out.println("Who would you like to contact?");
        contact = System.console().readLine();

        service.submit(new Messages());   // Process 1
        service.submit(new Reader());     // Process 2

        service.shutdown();
      } // main

      public static class Messages implements Runnable{
          public void run() {
            while(true){
            if(contact.equals("Exit"))break;
            String input ="";
            while(true){
            input = System.console().readLine();
              if(input.equals("Contact"))
              {
              contact = "Contact";    // Choose new contact
              break;
             }
              if(input.equals("Exit")){
                contact="Exit";       // Exit the application
                break;}

              try{
                stub.WriteMessage(user, input, contact);
                }catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();}
              }

            if(contact.equals("Exit"))break;
            System.out.println("Who would you like to contact?");
            contact = System.console().readLine();
          }
      }
    }

     public static class Reader implements Runnable{
          public void run(){
                String oldcontact = contact;
                String newcontact = "";
                while(true){
                    if(oldcontact.equals(newcontact)){
                      int x = 5; // wait 5 before checking for new messages
                      long startTime = System.currentTimeMillis();

                      while ((System.currentTimeMillis() - startTime) < x * 1000) {
                        //do nothing
                      }
                      try{
                             //Get any new messagess
                              ArrayList<String> response = stub.ReadMessage(user, contact);
                                 if(response.size() > oldlength){
                                   for(int i = oldlength; i < response.size(); i++)
                                    System.out.println(contact + ": " + response.get(i));
                                   oldlength = response.size();
                              }
                            } catch (Exception e) {
                              System.err.println("Client exception: " + e.toString());
                              e.printStackTrace();
                            }
                          }
                          // New Convo get previous chats
                          if(!oldcontact.equals(newcontact)){
                             try{
                                  ArrayList<String> oldconvo = stub.ReadMessage(user, contact);
                                  for(int i = 0; i < oldconvo.size();i++){
                                  System.out.println(contact + ": " + oldconvo.get(i));}
                                  oldlength = oldconvo.size();
                                  if(oldlength == 0)
                                  {
                                    System.out.println("No messages from: " +contact);
                                  }
                                  newcontact = contact;
                                  oldcontact = newcontact;
                                }catch (Exception e) {
                                System.err.println("Client exception: " + e.toString());
                                e.printStackTrace();}

                              }
                          newcontact = contact;

                          if(contact.equals("Contact")){
                            long start = System.currentTimeMillis();
                            // wait 3 seconds for user to put in new contact
                            while ((System.currentTimeMillis() - start) < 3 * 1000) {
                              //do nothing
                            }
                          }

                   }
              }
      }

} // class MessageClient
