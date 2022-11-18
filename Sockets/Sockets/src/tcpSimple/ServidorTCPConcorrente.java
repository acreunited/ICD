package tcpSimple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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

		BufferedReader is = null;
		PrintWriter os = null;

		try {
			// circuito virtual estabelecido: socket cliente na variavel newSock
			System.out.println("Thread " + this.getId() + ", "
					+ connection.getRemoteSocketAddress());

			is = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			os = new PrintWriter(connection.getOutputStream(), true);
			 
			String inputLine = is.readLine();
			
			if (inputLine != null) {
				System.out.println("Recebi do cliente " + inputLine);
				inputLine= "@" + inputLine.toUpperCase(); // converte para maiusculas
				os.println(inputLine);
			}
			
		} catch (IOException e) {
			System.err.println("erro na ligaçao " + connection + ": "
					+ e.getMessage());
		} finally {
			// garantir que o socket é fechado
			try {
				if (is != null)
					is.close();
				if (os != null)
					os.close();
				if (connection != null)
					connection.close();
			} catch (IOException e) {
			}
		}
		System.out.println("Terminou a Thread " + this.getId() + ", "
				+ connection.getRemoteSocketAddress());
	} // end run

} // end HandleConnectionThread
