package tcpbyte;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


public class ClienteTCP {

    public final static String DEFAULT_HOST =  "localhost"; // Máquina onde reside o servidor
    /* "95.94.149.133";*/ /* "192.168.1.253";*/
    public final static int DEFAULT_PORT = 5025; // porto onde o servidor está á espera
    
    public static void main(String[] args) {
     
        try (
        		Socket socket = new Socket(DEFAULT_HOST, DEFAULT_PORT);
        	){
        	String outputLine="Olá mundo!!!";
 
        	// Mostrar os parametros da ligação
            System.out.println("Ligação: " + socket);
            byte[] outbuffer = outputLine.getBytes(StandardCharsets.UTF_8);
            socket.getOutputStream().write(outbuffer,0,outbuffer.length); // escreve 
            System.out.println("Enviou para o servidor -> (" + outputLine+")");
            
            // Só em um read pode nao receber tudo
	 		
	 		byte[] buffer = new byte[8192]; // or 4096, or more
	 		byte[] result = new byte[0];
	 		int nbytes=0;
	 		while ((nbytes=socket.getInputStream().read(buffer)) > 0) {
	 			byte[] aux = new byte[nbytes];
	 			for(int i=0; i<nbytes; i++)
	 				aux[i]=buffer[i];
	 			result = ByteBuffer.allocate(result.length + aux.length)
	 								.put(result).put(aux).array();
	 			System.out.println("Leu "+nbytes+" bytes...");
	 		}
	 		System.out.println("Total de bytes lidos: "+result.length+".");
	 		String inputLine = new String(result, StandardCharsets.UTF_8);
               
            // Mostrar o que se recebe do socket
            System.out.println("Recebi do servidor -> (" + inputLine+")"); 
            
        } 
        catch (IOException e) {
            System.err.println("Erro na ligação " + e.getMessage());   
        }
    } // end main

} // end ClienteTCP



