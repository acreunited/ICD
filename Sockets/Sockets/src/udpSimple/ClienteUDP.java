package udpSimple;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class ClienteUDP {
	
	public final static int DIM_BUFFER  = 1024;
	
    public final static String DEFAULT_HOST = "localhost"; // "127.0.0.1";
    
    public final static int DEFAULT_PORT = 5025; 

    public static void main(String[] args) {

        DatagramSocket socket = null;

        try { 
        	// constroi mensagem
            String userInput = "Ol� mundo!!";

            // Cria socket - UDP com um porto atribu�do dinamicamente pelo sistema (anonymous port)
            socket = new DatagramSocket();

            // --- Envia pedido ---

            

            // Cria um datagrama
            DatagramPacket outputPacket = new DatagramPacket(userInput.getBytes(), 
                                                             userInput.length(),
                                                             InetAddress.getByName(DEFAULT_HOST), DEFAULT_PORT);
            socket.send(outputPacket);

            // --- Recebe resposta ---

            // Criar datagrama de recep��o
            byte[] buf = new byte[DIM_BUFFER];
            DatagramPacket inputPacket = new DatagramPacket(buf, buf.length);

            // Espera pela recep��o da resposta
            socket.receive(inputPacket);

            // Mostra Resposta
            String received = new String(inputPacket.getData(), 0, inputPacket.getLength());
            System.out.println("Dados recebidos: " + received);
            
        } // end try
        catch (UnknownHostException e) {
            System.err.println("Servidor " + DEFAULT_HOST + " n�o foi encontrado");
        }
        catch (SocketException e) {
            System.err.println("Erro na cria��o do socket: " + e.getMessage());
        }
        catch (IOException e) {
            System.err.println("Erro nas comunica��es: " + e);
        }
        finally {
            // No fim de tudo fechar o socket
            if (socket != null) socket.close();
        }
    } // end main
    
} // end ClienteUDP
