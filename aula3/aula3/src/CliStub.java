import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Eng. Porfírio Filipe
 *
 */

// class para ser usada no lado do cliente
public class CliStub extends comando {
	
	public CliStub() {
	}

	public CliStub(Document D) {
		cmd = D;
	}
	
	// constroí o comando listar
	public Document Listar() {
		Element listar = cmd.createElement("listar");
		Element request = cmd.createElement("request");
		listar.appendChild(request);
		Element reply = cmd.createElement("reply");
		listar.appendChild(reply);
		Element protocol = (Element) cmd.getElementsByTagName("protocol").item(0);
		protocol.appendChild(listar);
		return cmd;
	}

	// constroí o comando consultar
	public Document Consultar(String tit) {
		Element consultar = cmd.createElement("consultar");
		Element request = cmd.createElement("request");
		Element reply = cmd.createElement("reply");
		Element titulo = cmd.createElement("título");
		// acrescentar o titulo
		titulo.appendChild(cmd.createTextNode(tit));
		request.appendChild(titulo);
		consultar.appendChild(request);
		consultar.appendChild(reply);
		Element protocol = (Element) cmd.getElementsByTagName("protocol").item(0);
		protocol.appendChild(consultar);
		return cmd;
	}

	// constroí o comando obter
	public Document Obter(String[] palavras) {
		Element obter = cmd.createElement("obter");
		Element request = cmd.createElement("request");
		Element reply = cmd.createElement("reply");
		obter.appendChild(request);
		obter.appendChild(reply);
		Element protocol = (Element) cmd.getElementsByTagName("protocol").item(0);
		protocol.appendChild(obter);
		// criar lista de palavras
		for (int i = 0; i < palavras.length; i++) {
			Element palavra = cmd.createElement("palavra");
			palavra.appendChild(cmd.createTextNode(palavras[i]));
			request.appendChild(palavra);
		}
		return cmd;
	}

	// constroí o comando submeter
	public Document Submeter(Poema p) {
		Element submeter = cmd.createElement("submeter");
		Element request = cmd.createElement("request");
		Element reply = cmd.createElement("reply");
		submeter.appendChild(request);
		submeter.appendChild(reply);
		Element protocol = (Element) cmd.getElementsByTagName("protocol").item(0);
		protocol.appendChild(submeter);

		Element root = p.DOMDoc().getDocumentElement();
		Element clone = (Element) cmd.importNode(root, true);
		if (clone.hasAttribute("xmlns:xsi"))
			clone.removeAttribute("xmlns:xsi");
		if (clone.hasAttribute("xsi:noNamespaceSchemaLocation"))
			clone.removeAttribute("xsi:noNamespaceSchemaLocation");
		request.appendChild(clone);
		return cmd;
	}
}
