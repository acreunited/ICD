package serial_reflect;

import java.io.*;
import java.net.Socket;

public class Serialize {

   public static void main(String [] args) {
      Employee e = new Employee();
      
      try (Socket socket = new Socket("localhost", 5025);
    	  // need host and port, we want to connect to the ServerSocket at port 5025
    	){
         System.out.println("Connected!");
         // get the output stream from the socket.
         OutputStream outputStream = socket.getOutputStream();
         
         // a utilização de socket pode ser substituida pelo ficheiro
         // FileOutputStream outputStream = new FileOutputStream("WebContent/employee.ser");
        
         ObjectOutputStream out = new ObjectOutputStream(outputStream);
         System.out.println("Serializing...");
         e.print();
         out.writeObject(e);
         out.close();
         outputStream.close();
         System.out.printf("Serialized employee with success!");
      } catch (IOException i) {
         i.printStackTrace();
      }
   }
}
