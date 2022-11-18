package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ClienteUDP {

	public final static int DIM_BUFFER = 1024;

	public final static String DEFAULT_HOST = "localhost"; // "127.0.0.1";

	public final static int DEFAULT_PORT = 5025;

	private final static int SoTimeout = 10000; // milisegundos

	public static void main(String[] args) {
		try 
		(DatagramSocket socket = new DatagramSocket();
		// Cria socket - UDP com um porto atribuído dinamicamente pelo sistema
		// (anonymous port)
		)
		
		{
			// constroi mensagem
			String userInput = "Olá mundo!!";

			// --- Envia pedido ---

			// Cria um datagrama
			DatagramPacket outputPacket = new DatagramPacket(userInput.getBytes(), userInput.length(),
					InetAddress.getByName(DEFAULT_HOST), DEFAULT_PORT);
			int tentativas = 0;
			boolean continua = true;
			do { // tentativas
				socket.send(outputPacket);
				
				// --- Recebe resposta ---

				// Criar datagrama de recepção
				byte[] buf = new byte[DIM_BUFFER];
				DatagramPacket inputPacket = new DatagramPacket(buf, buf.length);

				socket.setSoTimeout(SoTimeout); // set the timeout in millisecounds.

				try {
					// espera pela resposta
					socket.receive(inputPacket); // recieve data until timeout

					// Mostra Resposta
					String received = new String(inputPacket.getData(), 0, inputPacket.getLength());
					System.out.println("Dados recebidos (" + inputPacket.getAddress() + ", " + inputPacket.getPort()
							+ "): " + received);
					continua = false;
				} catch (SocketTimeoutException e) {
					// timeout exception.
					System.out.println("("+tentativas+")Timeout reached!!! ");
				}
			} while (tentativas++ < 3 && continua);
		} // end try
		catch (UnknownHostException e) {
			System.err.println("Servidor " + DEFAULT_HOST + " não foi encontrado");
		} catch (SocketException e) {
			System.err.println("Erro na criação do socket: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Erro nas comunicações: " + e);
		} 
	} // end main

} // end ClienteUDP
