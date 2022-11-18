import javax.xml.XMLConstants;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author Eng. Porfírio Filipe
 *
 */
/**
 * Classe 'abstrata' para manipular os serviços/comandos associados ao protocolo
 * Define aspetos comuns ao cliente e ao servidor
 * 
 */

abstract class comando {
	Document cmd = XMLReadWrite.documentFromString(
			"<?xml version='1.0' encoding='ISO-8859-1'?>"
			+ "<protocol>"
			+ "</protocol>");

	
	public void show() {
		XMLReadWrite.writeDocument(cmd, System.out);
	}
	
	// valida se o comando respeita o protocolo
	protected boolean validar() throws SAXException {
			return XMLDoc.validDoc(cmd, Poema.contexto+"protocol.xsd", XMLConstants.W3C_XML_SCHEMA_NS_URI);
	}
	
}
