package serial_reflect;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
public class Deserialize {

   public static void main(String [] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      try (ServerSocket ss = new ServerSocket(5025);      
    	 // don't need to specify a hostname, it will be the current machine
    	) {
         System.out.println("ServerSocket awaiting connections...");
         Socket socket = ss.accept(); // blocking call, this will wait until a connection is attempted on this port.
         System.out.println("Connection from " + socket + "!");
         // get the input stream from the connected socket
         InputStream inputStream = socket.getInputStream();
         
         // a utilização de socket pode ser substituida pelo ficheiro
         // FileInputStream inputStream = new FileInputStream("WebContent/employee.ser");
         
         ObjectInputStream in = new ObjectInputStream(inputStream);
         
         // Employee e = (Employee) in.readObject(); // fazer por introspecao
         
         Object obj = in.readObject();
         System.out.println("The class of deserialized object is '" + obj.getClass().getName()+"'");
         
	     Method methodPrint = obj.getClass().getMethod("print");  // procura o metodo print
	     System.out.println("Invokes the public method '"+methodPrint.getName()+"' at runtime");
         methodPrint.invoke(obj);
         
         // e.print();  // foi realizado pelo metodo invoke
         in.close();
         inputStream.close();
      } catch (IOException i) {
         i.printStackTrace();
         return;
      } catch (ClassNotFoundException c) {
         System.out.println("Employee class not found");
         c.printStackTrace();
         return;
      }
   }
}
