import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Classe para comunicação de documentos XML
 */

/**
 * @author Porfírio Filipe
 *
 */

public final class XMLReadWrite {

	public static void main(String args[]) throws Exception {
		// Demonstra a utilização de include de um documento XML
		System.out.println("Exemplo que usa inlcude do ficheiro 'outroDoc.xml'");
		Document doc = documentFromFile("WebContent/exemplo/doc.xml");
		writeDocument(doc, System.out);
	}

	/**
	 * Copia a cadeia de carateres input para o output
	 * 
	 * @param input  - cadeira de carateres de origem
	 * @param output - cadeira de carateres de destino
	 * @throws IOException
	 */
	public static void copy(InputStream input, OutputStream output) throws IOException {
		int DEFAULT_BUFFER_SIZE = 1024 * 4;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int n = 0;
		while (-1 != (n = input.read(buffer)))
			output.write(buffer, 0, n);
	}

	/**
	 * Devolve o documento gerado a partir do ficheiro xml indicado
	 * 
	 * @param inputFile - nome do ficheiro a ler
	 * @return - documento DOM
	 */
	public static final Document documentFromFile(String inputFile) {
		try {
			return readDocument(new FileInputStream(inputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Escreve o conteudo do documento no ficheiro indicado
	 * 
	 * @param inputDOM   - documento DOM
	 * @param outputFile - nome do ficheiro a escrever
	 * @return
	 */
	public static final boolean documentToFile(Document inputDOM, String outputFile) {
		try {
			return writeDocument(inputDOM, new FileOutputStream(outputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Escreve o conteudo do documento na cadeia de carateres indicada
	 * 
	 * @param input  - documento DOM
	 * @param output - cadeia de carates de destino
	 * @return - erro
	 */
	public static final boolean writeDocument(Document input, OutputStream output) {
		try {
			DOMSource domSource = new DOMSource(input);
			StreamResult resultStream = new StreamResult(output);
			TransformerFactory transformFactory = TransformerFactory.newInstance();
			Transformer transformer = transformFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			transformer.transform(domSource, resultStream);
			return true;
		} catch (Exception e) {
			System.err.println("Error: Unable to write XML to stream!\n\t" + e);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Devolve o conteudo do documento obtido a partir da cadeia de carateres indicada
	 * 
	 * @param input - cadeia de carateres de origem
	 * @return - documento DOM
	 */
	public static final Document readDocument(InputStream input) {
		Document xmlDoc = null;
		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			// enable XInclude processing
			factory.setNamespaceAware(true);
			factory.setXIncludeAware(true);

			DocumentBuilder parser = factory.newDocumentBuilder();
			xmlDoc = parser.parse(input);

		} catch (Exception e) {
			System.err.println("Error: Unable to read XML from stream!\n\t" + e);
			e.printStackTrace();
		}
		return xmlDoc;
	}

	/**
	 * Lê o conteudo do documento a partir do socket indicado 
	 * Fazendo a sicronização com mudança de linha
	 * 
	 * @param socket - socket de origem
	 * @return - document DOM
	 */
	public static final Document documentFromSocket(Socket socket) {
		Document xmlDoc = null;
		try {
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String xml = is.readLine();  // termina quando encontra mudança de linha
			xmlDoc = documentFromString(xml); 
		} catch (IOException e) {
			System.err.println("Error: Unable to read XML from socket!\n\t" + e);
			e.printStackTrace();
		}
		return xmlDoc;
	}

	/**
	 * Lê o conteudo do documento a partir do socket indicado 
	 * Fazendo a sicronização baseada em seriação
	 * 
	 * @param socket - socket de origem
	 * @return - document DOM
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static final Document documentFromSSocket(Socket socket) throws IOException, ClassNotFoundException {
		ObjectInputStream iis = new ObjectInputStream(socket.getInputStream());
		return (Document) iis.readObject();
	}

	
	/**
	 * Escreve o conteudo do documento no socket indicado
	 * Fazendo a sicronização baseada em seriação
	 * @param xmlDoc
	 * @param socket
	 * @return
	 * @throws IOException 
	 */
	public static boolean documentToSSocket(Document xmlDoc, Socket socket) throws IOException {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(xmlDoc);
			return true;
	}

	/**
	 * Escreve o conteudo do documento no socket indicado
	 * Fazendo a sicronização com mudança de linha
	 * @param xmlDoc
	 * @param socket
	 * @return
	 */
	public static boolean documentToSocket(Document xmlDoc, Socket socket) {
		try {
			PrintWriter os = new PrintWriter(socket.getOutputStream(), true);
			String xml = documentToString(xmlDoc);
			os.println(xml.replaceAll("\n|\r", "")); // descarta eventuais linhas
			return true;
		} catch (IOException e) {
			System.err.println("Error: Unable to write XML to socket!\n\t" + e);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Devolve o conteudo do documento obtido a partir do string indicado
	 * @param strXML - string que representa o documento
	 * @return - documento DOM
	 */
	public static final Document documentFromString(String strXML) {
		Document xmlDoc = null;
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			xmlDoc = builder.parse(new InputSource(new StringReader(strXML)));
		} catch (Exception e) {
			System.err.println("Error: Unable to read XML from string!\n\t" + e);
			e.printStackTrace();
		}
		return xmlDoc;
	}

	/**
	 * Devolve o string obtido a partir do documento DOM indicado
	 * @param xmlDoc - documento DOM
	 * @return - string que representa o documento
	 */
	public static final String documentToString(Document xmlDoc) {
		Writer out = new StringWriter();
		try {
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1"); // "UTF-8"
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.transform(new DOMSource(xmlDoc), new StreamResult(out));
		} catch (Exception e) {
			System.out.println("Error: Unable to write XML to string!\n\t" + e);
			e.printStackTrace();
		}
		return out.toString();
	}
	
	// devolve array de strings com lista de ficheiros numa pasta
	public static String[] getFiles(final String pasta) {
		int j=0;
		File folder = new File(pasta);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++)
			if (listOfFiles[i].isFile())
				j=j+1;
		String[] files= new String[j];
		j=0;
		for (int i = 0; i < listOfFiles.length; i++)
			if (listOfFiles[i].isFile())
				files[j++]=listOfFiles[i].getName();
		return files;
		}
	
/**
 * Append the given byte arrays to one big array
 *
 * @param arrays The arrays to append
 * @return The complete array containing the appended data
 */
public static final byte[] append(final byte[]... arrays) {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    if (arrays != null) {
        for (final byte[] array : arrays) {
            if (array != null) {
                out.write(array, 0, array.length);
            }
        }
    }
    return out.toByteArray();
}

}