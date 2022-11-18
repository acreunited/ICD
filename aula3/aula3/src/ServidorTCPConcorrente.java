import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.XMLConstants;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ServidorTCPConcorrente {

    public final static int DEFAULT_PORT =  5025;  // 80;
    
    public static void main(String[] args) 
    {
        int port = DEFAULT_PORT; 

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e) {
                System.err.println("Erro no porto indicado");
            }
        }
        
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);

            Socket newSock    = null;

            for( ; ; ) {
                System.out.println("Servidor TCP concorrente aguarda ligacao no porto " + port + "..." );

                // Espera connect do cliente
                newSock = serverSocket.accept(); 

                Thread th = new HandleConnectionThread(newSock);
                th.start();
            }
        } 
        catch (Exception e) {
            System.err.println("Excepção no servidor: " + e);
        }
    } // end main

} // end ServidorTCP


class HandleConnectionThread extends Thread {

    private Socket connection;


    public HandleConnectionThread(Socket connection) {
        this.connection = connection;
    }
    
    // processamento especifico do servidor
    public Document processar(Document xmlRequest) {  // usar só no servidor
    	SrvStub cmd = new SrvStub(xmlRequest);
		// cmd.show();
		try {// verifica se não existe nenhum erro na recepção do pedido
			if (cmd.validar())
				System.out.println("\nValidação da mensagem do cliente realizada com sucesso!");
		} catch (SAXException e) {
			System.err.println("\nFalhou a validação da mensagem do cliente (protocol.xsd)!"+e.getMessage());
			cmd.show();
			//e.printStackTrace();
			return xmlRequest;   // devolve o mesmo comando
		}
		Document com=cmd.executar();
		if(com==null)
			return xmlRequest; // devolve o mesmo comando
		else {
			//new comando(com).show();
			try {// verifica se não existe nenhum erro na construção da respostas
				if (XMLDoc.validDoc(com, Poema.contexto+"protocol.xsd", XMLConstants.W3C_XML_SCHEMA_NS_URI))
					System.out.println("\nValidação da mensagem do servidor realizada com sucesso!");
			} catch (SAXException e) {
				System.err.println("\nFalhou a validação da mensagem do servidor XSD (protocol.xsd)!"+e.getMessage());
				new SrvStub(com).show();
				//e.printStackTrace();
			}
		  return com;
		}
	}  
    
	public void run() { 
		try {
			// circuito virtual estabelecido: socket cliente na variavel newSock
			System.out.println("Numero de ligações ativas: "+count.get(1));
			System.out.println("Thread " + this.getId() + ": "
					+ connection.getRemoteSocketAddress());
			
			// a tarefa só termina quando o circuito virtual for quebrado
			for (;;) { // forever
				// le pedido processa e envia a resposta

				Document xmlRequest = XMLReadWrite.documentFromSSocket(connection);

				Document xmlReply = processar(xmlRequest);  // processa o comando

				XMLReadWrite.documentToSSocket(xmlReply, connection);
			}
		} catch (Exception e) {
			System.out.println("Excepção: "+ e.getMessage());
		} finally {
			// garantir que o socket é fechado
			try {
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				System.err.println("Erro no fecho da ligaçao: "+ e.getMessage());
			}
		}
		System.out.println("Terminou a ligaçao (circuito virtual), Thread " + this.getId() + ": "
				+ connection.getRemoteSocketAddress());
		System.out.println("Numero de ligações ativas: "+count.get(-1));
	} // end run

} // end HandleConnectionThread

class count {
    private static long count = 0;

    public static synchronized long get(long step) {
        return count=count+step;
    }
} 
