
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author PFilipe
 *
 */
public class ClienteTCP {
	
	private Socket sock = ligar();   // mantem-se ligado durante a existencia do cliente
	
	public Socket ligar() {

		final String DEFAULT_HOSTNAME = "localhost"; // "95.94.149.133"; 
		final int DEFAULT_PORT = 5025;  // 80;

		Socket socket = null;
		try {
			socket = new Socket(DEFAULT_HOSTNAME, DEFAULT_PORT);

		} catch (Exception e) {
			System.err.println("Erro desconhecido: " + e.getMessage());
		}
		return socket;
	}
	

	/**
	 * 
	 */
	public void menu() {
		char op;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println();
			System.out.println();
			System.out.println("*** Menu Cliente ("+
			sock.getInetAddress().getCanonicalHostName()+"/"+sock+") ***");
			System.out.println("1 – Consultar um poema dado o seu título.");
			System.out.println("2 – Obter os poemas que incluam um conjunto de palavras.");
			System.out.println("3 – Submissão de um poema.");
			System.out.println("0 - Terminar!");
			String str = sc.nextLine();
			if (str != null && str.length() > 0)
				op = str.charAt(0);
			else
				op = ' ';
			switch (op) 
			{
			case '1':
				System.out.println("Consultar um poema dado o seu título.");
				System.out.println("\nLista de títulos:");
				NodeList titulos = Listar();   // obtem a lista de títulos
				if (titulos.getLength() == 0) {
					System.out.println("\nNão existe nenhum poema!");
					break;
				}
				for (int i = 0; i < titulos.getLength(); i++) 
					System.out.println((i+1)+"-"+titulos.item(i).getTextContent());
		        int num;
		        do {
		        	System.out.print("Indique o número associado ao título: ");
		        	num = sc.nextInt();
		        	sc.nextLine();
		        } while (num <1 || num >titulos.getLength());
		        String titulo=titulos.item(num-1).getTextContent();
		        NodeList L = Consultar(titulo);
				if (L.getLength() == 0)
					System.out.println("\nNão existe nenhum poema com o título \""+titulo+"\"!");
				else {
					System.out.println("\nPoemas com o título \""+titulo+"\":\n");
					for (int i = 0; i < L.getLength(); i++)   // podem existir mais do que um poema?
						new Poema((Element) L.item(i)).apresenta();
				}
				break;
			case '2':
				System.out.println("Obter os poemas que incluam um conjunto de palavras.");
				System.out.println("Indique as palavras: ");
				ArrayList<String> palList = new ArrayList<String>();
				String pv = "";
				do {
					pv = sc.nextLine();
					if (pv.compareTo("") == 0)
						break;
					palList.add(pv);
					System.out
							.println("Indique outra palavra ou <enter> para terminar:");
				} while (true);
				String[] palArray = palList.toArray(new String[palList.size()]);
				NodeList P = Obter(palArray);
				if (P.getLength() == 0)
					System.out
							.println("\nNão existe nenhum poema com todas as palavras indicadas!");
				else {
					System.out.println("\nPoemas:\n");
					for (int i = 0; i < P.getLength(); i++)  {
						System.out.println("**********************************");
						new Poema((Element) P.item(i)).apresenta();
						}
					}
				break;
			case '3':
				System.out.println("Submissão de um poema.\n");
				System.out.println("Lista de ficheiros em \"" + Poema.poemas+"\":");
				Arrays.stream(XMLReadWrite.getFiles(Poema.poemas)).forEach(System.out::println);
				System.out.println("\nIndique o nome do ficheiro (ex: poema.xml):");
				String poemaFileName = sc.nextLine();
				if(Submeter(poemaFileName))
					System.out.println("\nSubmissão realizada com sucesso!");
				else
					System.out.println("\nFalhou a submissão!");
				break;
			case '0':
				break;
			default:
				System.out.println("Opção ("+(int) op+")inválida, esolha uma opção do menu.");
			}
		} while (op != '0');
		sc.close();
		System.out.println("Terminou a execução.");
		System.exit(0);
	}

	/**
	 * Devolve a lista dos títulos dos poemas
	 */
	private NodeList Listar() {
			CliStub cmd = new CliStub();
			Document request = cmd.Listar();

			Document reply;
			try {
				// envia pedido
				XMLReadWrite.documentToSSocket(request, sock);
				// obtém resposta
				reply = XMLReadWrite.documentFromSSocket(sock);
			} catch (ClassNotFoundException e) {
				return null;
			} catch (IOException e) {
				return null;
			}
			return reply.getElementsByTagName("título");
	}
	
	/**
	 * Devolve a lista dos poemas que têm o título indicado
	 */
	private NodeList Consultar(String titulo) {
		CliStub cmd = new CliStub();
		Document request = cmd.Consultar(titulo);

		Document reply;
		try {
			// envia pedido
			XMLReadWrite.documentToSSocket(request, sock);
			// obtém resposta
			reply = XMLReadWrite.documentFromSSocket(sock);
		} catch (ClassNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		
		return reply.getElementsByTagName("poema");
	}

	/**
	 * Devolve a lista dos poemas que têm as palavras indicadas
	 */
	private NodeList Obter(String[] palavras) {
		CliStub cmd = new CliStub();
		Document request = cmd.Obter(palavras);
		Document reply;
		try {
			// envia pedido
			XMLReadWrite.documentToSSocket(request, sock);
			// obtém resposta
			reply = XMLReadWrite.documentFromSSocket(sock);
		} catch (ClassNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return reply.getElementsByTagName("poema");
	}
	
	/**
	 * Devolve sucesso ou erro em resultado da submissão
	 */
	private boolean Submeter(String poemaFileName) {
		
		Poema p=new Poema(poemaFileName);
		Element pm = (Element)p.DOMDoc().getElementsByTagName("poema").item(0);
		if (pm.hasAttribute("xmlns:xsi"))
			pm.removeAttribute("xmlns:xsi");
		if (pm.hasAttribute("xsi:noNamespaceSchemaLocation"))
			pm.removeAttribute("xsi:noNamespaceSchemaLocation");
		
		if (p.validar()) {
			System.out.println("Validação do poema realizada com sucesso!\n");
			p.addComment("\nSubmitted by ("+sock.getLocalAddress()+"/"+sock+")\n"
						+"\nFrom file ("+poemaFileName+") at "+ LocalDateTime.now());
			p.apresenta();
		}
		else {
			System.out.println("Falhou a validação!");
			return false;
		}
		
		CliStub cmd = new CliStub();
		Document request = cmd.Submeter(p);
		
		Document reply;
		try {
			// envia pedido
			XMLReadWrite.documentToSSocket(request, sock);
			// obtém resposta
			reply = XMLReadWrite.documentFromSSocket(sock);
		} catch (ClassNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		
		NodeList S = reply.getElementsByTagName("sucesso");
		return S.getLength()==1;
	}

	public static void main(String[] args) {

	    new ClienteTCP().menu();

	} // end main

} // end ClienteTCP



