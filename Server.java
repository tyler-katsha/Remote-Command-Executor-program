package RemoteCommandExecutor;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
  private static long startTime;   
  public static void main(String[] args) {
    startTime = System.currentTimeMillis(); 
    try(ServerSocket serSock = new ServerSocket(9999);){
      System.out.println("Listening for port 9999");
      
      while(true){
        Socket sock = serSock.accept();

        ClientHandler handler =new ClientHandler(sock);
        handler.start();

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

    //temp solution
    return "Cape Town, South Africa, Western Cape";

    // try{
    //   String API_KEY = System.getenv("API_KEY");
    //   System.out.println("API_KEY="+API_KEY);
    //   if(API_KEY == null){
    //     return "API KEY not configured";
    //   }
    //   String urlString = "https://maps.googleapis.com/maps/api/geocode/json"+
    //                      "?latlng=LATITUDE,LONGITUDE&key="+API_KEY;

    //   URI uri = new URI(urlString);
    //   URL url = uri.toURL();

    //   HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    //   conn.setRequestMethod("GET");

    //   BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    //   String inputLine;
      
    //   StringBuilder response = new StringBuilder();

      
    //   while((inputLine = in.readLine()) != null){
    //     response.append(inputLine);
    //   }
    //   in.close();
  
    //   return response.toString();    
    // } catch(URISyntaxException e){
    //   e.printStackTrace();
    // }catch(MalformedURLException e){
    //   e.printStackTrace();
    // } catch(ProtocolException e){
    //   e.printStackTrace();
    // }catch(IOException e){
    //   e.printStackTrace();
    // } 
    // return "Location not found";
  }
}
class ClientHandler extends Thread{
  private Socket sock;
  private static AtomicInteger users = new AtomicInteger(0);
  public ClientHandler(Socket sock){
    this.sock = sock;
    users.incrementAndGet();
  }

  public static int getUsers(){
    return users.get();
  }
  @Override
  public void run(){
    try{
      InputStream in = sock.getInputStream();
      OutputStream out = sock.getOutputStream();

      byte[] buffer = new byte[2048];

      while(true){
        int n = in.read(buffer);
        if(n == -1) break;

        String str = new String(buffer,0,n,StandardCharsets.UTF_8).trim();
        String response;
        System.out.println("[" + sock.getInetAddress() +"] -> " + str);

        if(str.equals("whoami")){
          response = sock.getInetAddress().toString();
        }else{
          response = Server.display(str);
        }
         
      
        if("__DISCONNECT__".equals(response)){
          break;
        }
        out.write((response + "\n").getBytes());
        out.flush();
      }
      System.out.println("User disconnected...");
      sock.close();
      
    } catch(IOException e){
      e.printStackTrace();
    }
    finally{
      users.decrementAndGet();
      try{
        sock.close();
      } catch(Exception i){}
    }
    
  }
}