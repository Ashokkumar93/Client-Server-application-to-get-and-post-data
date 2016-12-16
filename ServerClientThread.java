package server;


import static client.Client.CLIENT_FOLDER;
import static client.Client.FILE_SIZE;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ashokkumar
 */
public class ServerClientThread extends Thread {
    
    private String clientName = null;
    private DataInputStream is = null;
    private PrintStream os = null;
    private Socket clientSocket = null;
    private final ServerClientThread[] threads;
    private int maxClientsCount;
    private String name = null;
    private int threadId ;
    private final String SERVER_FOLDER = "folders\\server";
    
    public ServerClientThread(Socket clientSocket, ServerClientThread[] threads, int threadID) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
        this.threadId = threadID;
    }
    
    public void run(){
        System.out.println("client "+threadId);
        int maxClientsCount =  this.maxClientsCount;
        ServerClientThread[] threads = this.threads;
        
        try {
            is = new DataInputStream(clientSocket.getInputStream());
            os = new PrintStream(clientSocket.getOutputStream(),true);
            
            while(true){
                boolean brk=false;
                String req = is.readLine().trim();
                System.out.println("--"+req);
                if((req.toLowerCase()).equalsIgnoreCase("rls")){
                        doRLS();
                       
                    }
                else if((req.toLowerCase()).equalsIgnoreCase("get")){
                        String fileName = is.readLine().trim();
                        File file = new File(SERVER_FOLDER+"\\"+fileName);

                        Scanner input = new Scanner(file);
                        while(input.hasNextLine()){
                            os.println(input.nextLine());
                        }

                        os.println("EOF");
                       
                    }
                else if((req.toLowerCase()).equalsIgnoreCase("put")){{
                         String fileName = is.readLine().trim();
                           

                            File file = new File(SERVER_FOLDER+"\\"+fileName);

                          if (file.createNewFile()){
                            System.out.println("File is created!");
                          }else{
                            System.out.println("File already exists.");
                          }
                            BufferedWriter out = new BufferedWriter(
                                    new FileWriter(file));

                            do {
                                    String s = is.readLine();
                                    System.out.println(""+s);
                                    if(s.endsWith("EOF")){
                                        break;  
                                    }else{
                                        out.write(s);
                                    }
                                } while(true);

                            System.out.println("File " + 
                                    CLIENT_FOLDER+"/"+clientName+"/"+fileName + " downloaded )");
                            out.close();
                           
                    }
                }
                else if((req.toLowerCase()).equalsIgnoreCase("quit")){{
                        brk=true;
                        
                    }
                }
               
            }
            
        } catch (IOException ex) {
           
        }finally{
            try {
                os.close();
                is.close();
                clientSocket.close();
                threads[threadId] = null;
            } catch (IOException ex1) {
               ex1.printStackTrace();
            }
        }
        
    }
    
    
    public  void doRLS(){
        String s = null;
        try {
                Process p = Runtime.getRuntime().exec("cmd /c dir "+SERVER_FOLDER);
                BufferedReader stdInput = new BufferedReader(new
                     InputStreamReader(p.getInputStream()));
                BufferedReader stdError = new BufferedReader(new
                     InputStreamReader(p.getErrorStream()));
                while ((s = stdInput.readLine()) != null) {
                    os.println(s);

                }
                while ((s = stdError.readLine()) != null) {
                    os.println(s);
                }

               os.println("END");;
            }
            catch (IOException e) {
                System.out.println("exception happened - here's what I know: ");
                e.printStackTrace();
                System.exit(-1);
            }
    }
        
}
