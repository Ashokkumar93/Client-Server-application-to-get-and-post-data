package client;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
/**
 *
 * @author Ashokkumar
 */
public class Client {
  private static PrintStream os = null;
  private static DataInputStream is = null;
  public final static int FILE_SIZE = 6022386; // file size temporary hard coded
                                               // should bigger than the file to be downloaded
  public final static String CLIENT_FOLDER = "folders\\client";
  

  public static void main (String [] args ) throws IOException {
    int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    String serverIP=null;
    int port ;
    Socket sock = null;
    Scanner scn =  new Scanner(System.in);
    if(args == null || args.length < 2){
       
       System.out.print("Server ip :");
       serverIP = scn.next();
       System.out.print("Port Number :");
       port = scn.nextInt();
               
    }else{
        serverIP = args[0];
        port = Integer.parseInt(args[1]);
    }
  
    try {
      sock = new Socket(serverIP, port);
      System.out.println("Connecting .. ");
      os =  new PrintStream(sock.getOutputStream());
      is =  new DataInputStream(sock.getInputStream());
      System.out.println("Connected .. ");
 
      
      
      while(true){
        System.out.print(">");
        String cmd = scn.next();
          System.out.println("cmd :" + cmd);
         boolean brk=false;
         if((cmd.toLowerCase()).equalsIgnoreCase("rls")){
                    os.println("rls");
                    byte [] mybytearray  = new byte [FILE_SIZE];
                     StringBuffer ret = new StringBuffer();
                     do {
                         String s = is.readLine();
                         if(s.equals("END")){
                             break;
                         }else{
                             ret.append(s+"\n");
                         }

                        } while(true);

                         System.out.println(""+ret.toString());

     
                
            }
         else if((cmd.toLowerCase()).equalsIgnoreCase("get")){
                String fileName = scn.next();
                os.println("get");
                os.println(fileName);
                
                byte [] mybytearray  = new byte [FILE_SIZE];
                
                File file = new File(CLIENT_FOLDER+"\\"+fileName);
	      
	      if (file.createNewFile()){
	        System.out.println("File is created!");
	      }else{
	        System.out.println("File already exists.");
	      }
                BufferedWriter out = new BufferedWriter(
                        new FileWriter(file,true));
                
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
                        CLIENT_FOLDER+"/"+fileName + " downloaded )");
                out.close();
                
              
            }
            
         else if((cmd.toLowerCase()).equalsIgnoreCase("put")){
                String fileName = scn.next();
                os.println("put");
                os.println(fileName);
              
                        File file = new File(CLIENT_FOLDER+"\\"+fileName);

                        Scanner input = new Scanner(file);
                        while(input.hasNextLine()){
                            os.println(input.nextLine());
                        }
                  
                        os.println("EOF");
                        
                        System.out.println("Successfully sent........");
               
            }
         else  if((cmd.toLowerCase()).equalsIgnoreCase("quit")){
                os.println("quit");
                System.out.println("Quitted successfully.....");
                
                 brk=true;
                break;
            }
           
        }
           
      }
      
    finally {
      if (fos != null) fos.close();
      if (bos != null) bos.close();
      if (sock != null) sock.close();
    }
  }
}
