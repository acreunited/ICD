package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//import java.util.Scanner;

public class ServidorTCPConcorrente {

	public final static int DEFAULT_PORT = 5025; // porto onde vai ficar � espera

	public static void main(String[] args) {

		try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);) {
			Socket newSock = null;

			for (;;) {
				System.out.println("Servidor TCP concorrente aguarda ligacao no porto " + DEFAULT_PORT + "...");

				// Espera connect do cliente
				newSock = serverSocket.accept();

				Thread th = new HandleConnectionThread(newSock);
				th.start();
			}
		} catch (IOException e) {
			System.err.println("Excep��o no servidor: " + e);
		}
	} // end main

} // end ServidorTCP

class HandleConnectionThread extends Thread {

	private Socket connection;

	public HandleConnectionThread(Socket connection) {
		this.connection = connection;
	}

	public void run() {

		try (BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				PrintWriter os = new PrintWriter(connection.getOutputStream(), true);) {
			
			// circuito virtual estabelecido: socket cliente na variavel newSock
			System.out.println("Thread " + this.getId() + ", " + connection.getRemoteSocketAddress());

			String inputLine = is.readLine();

			if (inputLine != null) {
				System.out.println("Recebi do cliente " + inputLine);
				inputLine = "@" + inputLine.toUpperCase(); // converte para maiusculas
				os.println(inputLine);
			}

		} catch (IOException e) {
			System.err.println("Erro na liga�ao " + connection + ": " + e.getMessage());
		} finally {
			// garantir que o socket � fechado
			if (connection != null)
				try {
					connection.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
		}
		System.out.println("Terminou a Thread " + this.getId() + ", " + connection.getRemoteSocketAddress());
	} // end run

} // end HandleConnectionThread
