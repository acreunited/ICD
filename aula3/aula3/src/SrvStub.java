import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Eng. Porfírio Filipe
 *
 */
// class para ser usada no lado do servidor
public class SrvStub extends comando {

	public SrvStub(Document D) {
		cmd = D;
	}

	// constroí a resposta ao comando listar
	@SuppressWarnings("unused")
	private Document Listar() {
		Document titulos = Poema.Listar(); // obter os titulos dos poemas
		NodeList T = titulos.getElementsByTagName("título");
		Element reply = (Element) cmd.getElementsByTagName("reply").item(0);
		for (int i = 0; i < T.getLength(); i++) {
			Element clone = (Element) cmd.importNode(T.item(i), true);
			reply.appendChild(clone);
		}
		return cmd;
	}
	// constroí a resposta ao comando consultar
	@SuppressWarnings("unused")
	private Document Consultar() {
		NodeList T = cmd.getElementsByTagName("título");
		Document poema = Poema.Consultar(T.item(0).getTextContent());
		if (poema != null) {
			Element reply = (Element) cmd.getElementsByTagName("reply").item(0);
			Element p = (Element) poema.getElementsByTagName("poema").item(0);
			Element clone = (Element) cmd.importNode(p, true);
			if (clone.hasAttribute("xmlns:xsi"))
				clone.removeAttribute("xmlns:xsi");
			if (clone.hasAttribute("xsi:noNamespaceSchemaLocation"))
				clone.removeAttribute("xsi:noNamespaceSchemaLocation");
			reply.appendChild(clone);
		}
		return cmd;
	}

	// constroí a resposta ao comando obter
	@SuppressWarnings("unused")
	private Document Obter() {
		NodeList pal = cmd.getElementsByTagName("palavra");

		ArrayList<String> palList = new ArrayList<String>();
		for (int i = 0; i < pal.getLength(); i++) {
			palList.add(pal.item(i).getTextContent());
		}
		String[] palavras = palList.toArray(new String[palList.size()]);

		Element reply = (Element) cmd.getElementsByTagName("reply").item(0);
		ArrayList<Poema> poemas = Poema.Obter(palavras);
		for (int i = 0; i < poemas.size(); i++) {
			Element p = (Element) poemas.get(i).DOMDoc().getElementsByTagName("poema").item(0);
			Element clone = (Element) cmd.importNode(p, true);
			if (clone.hasAttribute("xmlns:xsi"))
				clone.removeAttribute("xmlns:xsi");
			if (clone.hasAttribute("xsi:noNamespaceSchemaLocation"))
				clone.removeAttribute("xsi:noNamespaceSchemaLocation");
			reply.appendChild(clone);
		}
		return cmd;
	}

	// constroí a resposta ao comando submeter
	@SuppressWarnings("unused")
	private Document Submeter() {
		NodeList P = cmd.getElementsByTagName("poema");
		Poema pm = new Poema((Element) P.item(0));
		Element ret = null;
		if (pm.grava(pm.getTitulo() + ".xml"))
			ret = cmd.createElement("sucesso");
		else
			ret = cmd.createElement("erro");
		Element reply = (Element) cmd.getElementsByTagName("reply").item(0);
		reply.appendChild(ret);
		return cmd;
	}

	// chama dinamicamente (usando reflexão) os metodos referidos no protocolo
	public Document executar() {
		/*
		 * if(cmd.getElementsByTagName("listar").getLength()==1) return Listar();
		 * if(cmd.getElementsByTagName("consultar").getLength()==1) return Consultar(); 
		 * if(cmd.getElementsByTagName("obter").getLength()==1) return Obter(); 
		 * if(cmd.getElementsByTagName("submeter").getLength()==1) return Submeter();
		 */
		// podia ser feito chamando os metodos por instrospecao// podia ser feito
		// chamando os metodos por instrospecao
		// cshow();
		Node protocolo = cmd.getElementsByTagName("protocol").item(0);
		NodeList filhos = protocolo.getChildNodes(); // podem haver nós de texto
		for (int i = 0; i < filhos.getLength(); i++) {
			Node filho = filhos.item(0);
			if (filho.getNodeType() == Node.ELEMENT_NODE) {
				try {
					String mt = filho.getNodeName();
					mt = mt.substring(0, 1).toUpperCase() + mt.substring(1, mt.length());
					System.out.println("Chamada do metodo: "+mt);
					Method methodTocall = getClass().getDeclaredMethod(mt);
					return (Document) methodTocall.invoke(this);
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					System.err.println("Execpção na chamada de metodo  (mt): " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
