
package server;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Ashokkumar
 */
public class Server {
    
    private static ServerSocket serverSocket = null;
  // The client socket.
  private static Socket clientSocket = null;

  // This chat server can accept up to maxClientsCount clients' connections.
  private static final int maxClientsCount = 10;
  private static final ServerClientThread[] threads = new ServerClientThread[maxClientsCount];
    private static PrintStream PrintStream;
  
   public static void main(String args[]) {
       
       int portNumber = 2000;
       if(args.length < 1){
           
       }else{
           portNumber = Integer.parseInt(args[0]);
       }
       try{
           serverSocket =  new ServerSocket(portNumber);
       }catch(IOException e){
           e.printStackTrace();
       }
       
       while(true){
           try {
               clientSocket =  serverSocket.accept();
               int i = 0;
               for(i=0 ; i< maxClientsCount ;i++){
                   if (threads[i] == null){
                       (threads[i] = new ServerClientThread(clientSocket,threads,i)).start();
                       break;
                   }
               }
               if(i ==  maxClientsCount){
                   PrintStream os = new PrintStream(clientSocket.getOutputStream());
                   os.println("Sorry, Busy right now !! ");
                   os.close();
                   clientSocket.close();
               }
           } catch (IOException ex) {
               ex.printStackTrace();
           }
       }
   }
    
}
