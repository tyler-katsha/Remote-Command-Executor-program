package RemoteCommandExecutor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Server {
  private static long startTime;   
  static void main(String[] args) {
    startTime = System.currentTimeMillis(); 
    try(ServerSocket serSock = new ServerSocket(9999);){
      System.out.println("Listening for port 9999");
      
      while(true){
        Socket sock = serSock.accept();

        ClientHandler handler =new ClientHandler(sock);
        handler.run();

        System.out.println("User connected: " + sock.getInetAddress());
        System.out.println("Users count: " + ClientHandler.getUsers());
        
      }
      
    } catch(SocketException e){
      e.printStackTrace();
    }catch(IOException e){
      e.printStackTrace();
    }
  }


  public static String display(String str){

    if(str == null){
      return "No command sent...";
    }
      return switch(str.toLowerCase()){
      case "time" -> displayTime();
      case "ping" -> pingDetails();
      case "date" -> displayDate();
      case "os" -> displayOS();
      case "uptime" -> displayUpTime();
      case "location","coords","map" -> displayGeoLocation();
      case "clients" -> displayUserAmount();
      case "exit","leave" -> "__DISCONNECT__";
      case "help" -> displayListOfCommands();
      default -> displayError();
      };
    
  }
  private static String displayTime(){
    LocalTime localTime = LocalTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    return localTime.format(formatter);
  }

  private static String displayDate(){
    LocalDate localDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    return localDate.format(formatter);
  }
  private static String displayOS(){
    try{
      return System.getProperty("os.name");
    } catch(NullPointerException e){
      return "Null pointer exception caught";
    }
    
  }
  private static String displayUserAmount(){
    return String.valueOf(ClientHandler.getUsers());
  }
  private static String pingDetails(){
    return "pong";
  }
  private static String displayUpTime(){
    long upTimeMillis = System.currentTimeMillis() - startTime;
    long seconds = (upTimeMillis / 1000) % 60;
    long minutes = (upTimeMillis /(1000 * 60)) % 60;
    long hours = (upTimeMillis / (1000 * 60 * 60)) % 24;
    long days = (upTimeMillis / (1000 * 60 * 60 * 24));

    return String.format("%d days %d hours %d minutes %d seconds",days,hours,minutes,seconds);
  }
  private static String displayListOfCommands(){
    return "--List of commands--\n\n"+
    "1 - time -> display the current time to the user\n"+
    "2 - uptime -> display how long the server is up for in seconds\n"+
    "3 - date -> displays the current date"+
    "\n4 - os -> Lists the user's current operating system"
    +"\n5 - help -> lists all the commands available\n" +
    "6 - location,map,coords -> Sends the user's current location"+
    "\n7 - whoami -> displays ip address\n" +
    "8 - ping -> server sends back a 'pong' respond\n"+
    "9 - clients -> sends the user how much other clients are connected\n" +
    "10 - exit,leave -> to exit the server\n";
  }
  private static String displayError(){
    return "Invalid command inserted";
  }
  private static String displayGeoLocation(){

    return "Cape Town, South Africa, Western Cape";
  }
}

