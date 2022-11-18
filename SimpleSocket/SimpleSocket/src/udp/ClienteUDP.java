package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class ClienteUDP {
	
	public final static int DIM_BUFFER  = 1024;
	
    public final static String DEFAULT_HOST = "localhost";
    
    public final static int DEFAULT_PORT = 5025; 

    public static void main(String[] args) {

        DatagramSocket socket = null;

        try { 
            // Obtém endereço do servidor 
            InetAddress hostAddress = InetAddress.getByName(DEFAULT_HOST);

            // Cria socket - UDP com um porto atribuído dinamicamente pelo sistema (anonymous port)
            socket = new DatagramSocket();


            // --- Envia pedido ---

            // constroi mensagem
            String userInput = new String("Olá mundo!!");

            // Cria um datagrama
            DatagramPacket outputPacket = new DatagramPacket(userInput.getBytes(), 
                                                             userInput.length(),
                                                             hostAddress, DEFAULT_PORT);
            socket.send(outputPacket);


            // --- Recebe resposta ---

            // Criar datagrama de recepção
            byte[] buf = new byte[DIM_BUFFER];
            DatagramPacket inputPacket = new DatagramPacket(buf, buf.length);

            // Espera pela recepção da resposta
            socket.receive(inputPacket);

            // Mostra Resposta
            String received = new String(inputPacket.getData(), 0, inputPacket.getLength());
            System.out.println("Dados recebidos: " + received);
            
        } // end try
        catch (UnknownHostException e) {
            System.err.println("Servidor " + DEFAULT_HOST + " não foi encontrado");
        }
        catch (SocketException e) {
            System.err.println("Erro na criação do socket: " + e.getMessage());
        }
        catch (IOException e) {
            System.err.println("Erro nas comunicações: " + e);
        }
        finally {
            // No fim de tudo fechar o socket
            if (socket != null) socket.close();
        }
    } // end main
    
} // end ClienteUDP
