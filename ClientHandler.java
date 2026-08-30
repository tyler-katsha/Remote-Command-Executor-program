package RemoteCommandExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler implements Runnable {
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

                String str = new String(buffer,0,n, StandardCharsets.UTF_8).trim();
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
            } catch(Exception _){}
        }

    }
}
