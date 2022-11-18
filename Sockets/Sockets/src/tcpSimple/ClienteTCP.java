package tcpSimple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClienteTCP {

    public final static String DEFAULT_HOST =  "localhost"; // M�quina onde reside o servidor
    /* "95.94.149.133";*/ /* "192.168.1.253";*/
    public final static int DEFAULT_PORT = 5025; // porto onde o servidor est� � espera
    
    public static void main(String[] args) {
        
        Socket     socket = null;
        BufferedReader is = null;
        PrintWriter    os = null;
        
        try {
        	String outputLine="Ol� mundo!!!";
            socket = new Socket(DEFAULT_HOST, DEFAULT_PORT);

            // Mostrar os parametros da liga��o
            System.out.println("Liga��o: " + socket);
            
            // Stream para escrita no socket
            os = new PrintWriter(socket.getOutputStream(), true); 
            // Stream para leitura do socket
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            os.println(outputLine);            			// escreve
            String inputLine = is.readLine();  			// le */
            
            // Mostrar o que se recebe do socket
            System.out.println("Recebi do servidor -> " + inputLine);  
            
            
        } 
        catch (IOException e) {
            System.err.println("Erro na liga��o " + e.getMessage());   
        }
        finally {
            // No fim de tudo, fechar os streams e o socket
            try {
                if (os != null) os.close();
                if (is != null) is.close();
                if (socket != null ) socket.close();
            }
            catch (IOException e) { 
                // if an I/O error occurs when closing this socket
            }
        } // end finally


    } // end main

} // end ClienteTCP



