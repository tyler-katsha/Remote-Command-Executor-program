package RemoteCommandExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
  
  public static void main(String[] args) {
    
    try(Scanner scan = new Scanner(System.in);
      Socket sock = new Socket("localhost",9999);){
      
      while(true){
        System.out.print("Command> ");
        String strRequest = scan.next();

        InputStream in = sock.getInputStream();
        OutputStream out = sock.getOutputStream();
        
        System.out.println("Sending command to server...");
        out.write(strRequest.getBytes());
        out.flush();
        
        byte[] response = new byte[2048];
        int n = in.read(response);

        if(n == -1){
          System.out.println("Server disconnected");
          break;
        }
        String strResponse = new String(response,0,n,StandardCharsets.UTF_8).trim();
        System.out.println("\n"+strResponse+"\n");
      }
      
      
    } catch(SocketException e){
      e.printStackTrace();
    }catch(IOException e){
      e.printStackTrace();
    }
  }
}
