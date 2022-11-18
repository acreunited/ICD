package tcpbyte;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

//import java.util.Scanner;

public class ServidorTCPConcorrente {

	public final static int DEFAULT_PORT = 5025; // porto onde vai ficar á espera
												
	public static void main(String[] args) {

		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(DEFAULT_PORT);

			Socket newSock = null;

			for (;;) {
				System.out
						.println("Servidor TCP concorrente aguarda ligacao no porto "
								+ DEFAULT_PORT + "...");

				// Espera connect do cliente
				newSock = serverSocket.accept();

				Thread th = new HandleConnectionThread(newSock);
				th.start();
			}
		} catch (IOException e) {
			System.err.println("Excepção no servidor: " + e);
		}
	} // end main

} // end ServidorTCP

class HandleConnectionThread extends Thread {

	private Socket connection;

	public HandleConnectionThread(Socket connection) {
		this.connection = connection;
	}

	public void run() {

		try {
			// circuito virtual estabelecido: socket cliente na variavel newSock
			System.out.println("Thread " + this.getId() + ", "
					+ connection.getRemoteSocketAddress());
			
			
	 		byte[] inbuffer = new byte[8192]; // or 4096, or more
	 		byte[] result = new byte[0];
	 		int nbytes=0;
	 		connection.setSoTimeout(1*1000);
	 		try {
	 		while ((nbytes=connection.getInputStream().read(inbuffer)) > 0) {// pode nao receber tudo
	 			byte[] aux = new byte[nbytes];
	 			for(int i=0; i<nbytes; i++)
	 				aux[i]=inbuffer[i];
	 			result = ByteBuffer.allocate(result.length+aux.length).put(result).put(aux).array();
	 			System.out.println("Leu "+nbytes+" bytes...");
	 		} 
	 		}catch (SocketTimeoutException e) {
			// timeout exception.
			System.out.println("Timeout reached!!! ");
	 		}
	 		System.out.println("Total de bytes lidos: "+result.length+".");
	 		String inputLine = new String(result, StandardCharsets.UTF_8);
			System.out.println("Recebi do cliente -> (" + inputLine+")");
			
			inputLine= "@" + inputLine.toUpperCase(); // converte para maiusculas
			System.out.println("Vai enviar para o cliente -> (" + inputLine+")");
			byte[] outbuffer = inputLine.getBytes(StandardCharsets.UTF_8);
			connection.getOutputStream().write(outbuffer,0,outbuffer.length);
			System.out.println("Escreveu "+outbuffer.length+" bytes.");
			
		} catch (IOException e) {
			System.err.println("erro na ligaçao " + connection + ": "
					+ e.getMessage());
		} finally {
			// garantir que o socket é fechado
			try {
				if (connection != null)
					connection.close();
			} catch (IOException e) {
			}
		}
		System.out.println("Terminou a Thread " + this.getId() + ", "
				+ connection.getRemoteSocketAddress());
	} // end run

} // end HandleConnectionThread
