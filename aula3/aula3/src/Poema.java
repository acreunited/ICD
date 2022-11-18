import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Classe para manipulação de poemas
 */

/**
 * @author Porfírio Filipe
 *
 */

public class Poema {
	public final static String contexto = "WebContent\\xml\\";
	public final static String poemas = "WebContent\\poemas\\";
	private Document D = null; // representa a arvore DOM com o poema

	public Poema(Element pm) {
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			D = builder.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Element clone = (Element) D.importNode(pm, true);
		D.appendChild(clone);
	}

	public Poema(String XMLdoc) {
		XMLdoc = poemas + XMLdoc;
		DocumentBuilder docBuilder;
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			File sourceFile = new File(XMLdoc);
			D = docBuilder.parse(sourceFile);
		} catch (ParserConfigurationException e) {
			System.out.println("Wrong parser configuration: " + e.getMessage());
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Could not read source file: " + e.getMessage());
		}
	}

	/**
	 * @return documento DOM que representa o poema
	 */
	public Document DOMDoc() {
		return D;
	}

	public boolean validar() {
		if (D == null)
			return false;
		try {
			return XMLDoc.validDoc(D, contexto + "poema.xsd", XMLConstants.W3C_XML_SCHEMA_NS_URI);
		} catch (SAXException e) {
			System.err.println("\n"+e.getMessage()+"\n");
			//e.printStackTrace();
			return false;
		}
	}

	public void menu() {
		char op;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println();
			System.out.println();
			System.out.println("*** Menu Poema ***");
			System.out.println("1 - Apresenta o poema na sua forma escrita clássica.");
			System.out.println("2 – Classifica (DOM) as estrofes quanto ao número de versos.");
			System.out.println("3 – Acrescenta um verso a determinada estrofe.");
			System.out.println("4 – Remove uma determinada estrofe.");
			System.out.println("5 – Indica os versos que contêm determinada palavra.");
			System.out.println("6 – Gravar o poema.");
			System.out.println("7 – Validar/Reconhecer genéricamente um poema.");
			System.out.println("8 – Validar/Reconhecer soneto.");
			System.out.println("9 – Classifica (XPATH) as estrofes quanto ao número de versos.");
			System.out.println("A – Poema na sua forma escrita (em html) clássica.");
			System.out.println("B – Poema (em text) numerando as estrofes e os respetivos versos.");
			System.out.println("C – Indica os versos que contêm determinada palavra.");
			System.out.println("0 - Terminar!");
			String str = sc.nextLine();
			if (str != null && str.length() > 0)
				op = str.charAt(0);
			else
				op = ' ';
			switch (op) {
			case '1':
				apresenta();
				break;
			case '2':
				classificaDOM();
				break;
			case '3':
				System.out.println("Indique o numero da estrofe:");
				short i = sc.nextShort();
				sc.nextLine();
				System.out.println("Escreva o verso:");
				String verso = sc.nextLine();
				if (acrescenta(i, verso))
					apresenta();
				else
					System.out.println("Não acrescentou!");
				break;
			case '4':
				System.out.println("Indique o numero da estrofe a remover:");
				short r = sc.nextShort();
				sc.nextLine();
				if (remove(r))
					apresenta();
				else
					System.out.println("Não removeu!");
				break;
			case '5':
				System.out.println("Escreva a palavra:");
				String palavra = sc.nextLine();
				indica(palavra);
				break;
			case '6':
				System.out.println("Indique o nome do ficheiro (em " + Poema.contexto
						+ ") para guardar o poema (ex: novopoema.xml):");
				String poemaFileName = sc.nextLine();
				grava(poemaFileName);
				break;
			case '7':
				System.out.println("Indique o nome do ficheiro (em " + Poema.contexto
						+ ") que representa o esquema XML (ex: poema.xsd, poemax.xsd):");
				String xsdFileName = sc.nextLine();
				if (xsdFileName.length() == 0) {
					xsdFileName = "poema.xsd";
					System.out.println("Foi assumido o esquema XML representado em: " + xsdFileName);
				}
				try {
					if (XMLDoc.validDoc(D, contexto + xsdFileName, XMLConstants.W3C_XML_SCHEMA_NS_URI))
						System.out.println("Validação realizada com sucesso!");
					else
						System.out.println("Falhou a validação (" + xsdFileName + ")!");
				} catch (SAXException e) {
					e.printStackTrace();
					System.out.println("Ocorrer um erro inesperado na validação (" + xsdFileName + ")!");
				}
				break;
			case '8':
				System.out.println(
						"Aplica ao DOM a transformação (poema_xml_to_xml.xsl) e aplica ao resultdo (soneto.xml) o esquema XML (soneto.xsd).");
				// gera o ficheiro soneto.xml para manter um registo intermédio
				XMLDoc.transfDoc(D, contexto + "poema_xml_to_xml.xsl", contexto + "soneto.xml");
				if (XMLDoc.validDocXSD(contexto + "soneto.xml", contexto + "soneto.xsd"))
					System.out.println("Validação do soneto realizada com sucesso!");
				else
					System.out.println("Falhou a validação do soneto (soneto.xsd)!");
				break;
			case '9':
				classificaXPATH();
				break;
			case 'A':
			case 'a':
				System.out.println("Gera o poema na sua forma escrita (em html) clássica 'out.html'");
				XMLDoc.transfDoc(D, contexto + "poema_xml_to_html.xsl", contexto + "out.html");
				break;
			case 'B':
			case 'b':
				System.out.println("Gera o poema (em text) numerando as estrofes e os respetivos versos 'out.txt'");
				XMLDoc.transfDoc(D, contexto + "poema_xml_to_txt.xsl", contexto + "out.txt");
				break;
			case 'C':
			case 'c':
				System.out.println("Escreva a palavra:");
				String pal = sc.nextLine();
				indicaXPATH(pal);
				break;
			case '0':
				break;
			default:
				System.out.println("Opção inválida, esolha uma opção do menu.");
			}
		} while (op != '0');
		sc.close();
		System.out.println("Terminou a execução.");
		System.exit(0);
	}

	private String escreveExtenso(short numero) {
		switch (numero) {
		case 1:
			return "Monástico";
		case 2:
			return "Dístico ou parelha";
		case 3:
			return "Terceto";
		case 4:
			return "Quadra";
		case 5:
			return "Quintilha";
		case 6:
			return "Sextilha";
		case 7:
			return "Sétima";
		case 8:
			return "Oitava";
		case 9:
			return "Nona";
		case 10:
			return "Décima";
		default:
			return "Irregular (" + numero + ")";
		}
	}

	private boolean estaPresente(String palavra, String verso) {
		verso = verso.replaceAll("[^a-zA-Z0-9 ]", "");
		StringTokenizer st = new StringTokenizer(verso);
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.compareToIgnoreCase(palavra) == 0)
				return true;
		}
		return false;
	}

	public void apresenta() {
		Element root = D.getDocumentElement();
		Element titulo = (Element) root.getElementsByTagName("título").item(0);
		System.out.println("Título: " + titulo.getTextContent());
		System.out.println();
		NodeList estrofes = root.getElementsByTagName("estrofe");
		for (int e = 0; e < estrofes.getLength(); e++) {
			Element estrofe = (Element) estrofes.item(e);
			NodeList versos = estrofe.getElementsByTagName("verso");
			for (int i = 0; i < versos.getLength(); i++)
				System.out.println(versos.item(i).getTextContent());
			System.out.println();
		}
		Element autor = (Element) root.getElementsByTagName("autor").item(0);
		System.out.println("Autor: " + autor.getTextContent());
	}

	public void classificaDOM() {
		System.out.println("Classificação das estrofes quanto à quantidade de versos:");
		Element root = D.getDocumentElement();
		NodeList estrofes = root.getElementsByTagName("estrofe");
		for (int e = 0; e < estrofes.getLength(); e++) {
			Element estrofe = (Element) estrofes.item(e);
			NodeList versos = estrofe.getElementsByTagName("verso");
			System.out.println(e + 1 + "ª estrofe: " + escreveExtenso((short) versos.getLength()));
		}
	}

	public void classificaXPATH() {
		System.out.println("Classificação das estrofes quanto à quantidade de versos:");
		// só está disponivel XPATH 1.0 :
		// https://docs.oracle.com/en/java/javase/13/docs/api/java.xml/javax/xml/xpath/package-summary.html
		// com XPATH 2.0 seria só usar : "/poema/estrofe/count(verso)"

		// NodeList estrofes = XMLDoc.getXPath("/poema/estrofe", D);

		int qtEstrofes = XMLDoc.getXPathN("count(/poema/estrofe)", D);
		System.out.println("O poema tem " + qtEstrofes + " estrofes.");
		for (int e = 0; e < qtEstrofes; e++) {
			// contar os versos
			NodeList versos = XMLDoc.getXPath("/poema/estrofe[position()=" + (e + 1) + "]/verso", D);
			System.out.println(e + 1 + "ª estrofe: " + escreveExtenso((short) versos.getLength()));
		}
	}

	public boolean acrescenta(short numEstrofe, String verso) {
		System.out.println("Acrescenta o verso \"" + verso + "\" à estrofe " + numEstrofe + ".");
		Element root = D.getDocumentElement();
		NodeList estrofes = root.getElementsByTagName("estrofe");
		if (numEstrofe <= estrofes.getLength()) {
			Element estrofe = (Element) estrofes.item(numEstrofe - 1);
			Element vers = D.createElement("verso");
			vers.setTextContent(verso);
			estrofe.appendChild(vers);
			return true;
		}
		return false;
	}

	public boolean remove(short numEstrofe) {
		Element root = D.getDocumentElement();
		NodeList estrofes = root.getElementsByTagName("estrofe");
		if (numEstrofe <= estrofes.getLength()) {
			Element estrofe = (Element) estrofes.item(numEstrofe - 1);
			estrofe.getParentNode().removeChild(estrofe);
			System.out.println("Removeu a " + numEstrofe + "ª estrofe.");
			return true;
		}
		return false;
	}

	public void indica(String palavra) {
		System.out.println("Indica os versos com a  palavra \"" + palavra + "\"");
		Element root = D.getDocumentElement();
		NodeList versos = root.getElementsByTagName("verso");
		for (int i = 0; i < versos.getLength(); i++)
			if (estaPresente(palavra, versos.item(i).getTextContent()))
				System.out.println(versos.item(i).getTextContent());
	}

	/*
	 * Usa XPATH
	 */
	public void indicaXPATH(String palavra) {
		System.out.println("Indica os versos com a  palavra \"" + palavra + "\"");
		NodeList versos = XMLDoc.getXPath("/poema/estrofe/verso[contains(text(), " + palavra + ")]", D);
		for (int i = 0; i < versos.getLength(); i++)
			if (estaPresente(palavra, versos.item(i).getTextContent()))
				System.out.println(versos.item(i).getTextContent());
	}

	private String sanitizeFilename(String inputName) {
		return inputName.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
	}

	/**
	 * Escreve arvore DOM num ficheiro
	 *
	 * @param output ficheiro usado para escrita
	 */
	public boolean grava(final String dtaFile) {
		String dataFile = poemas + sanitizeFilename(dtaFile);
		String bkFile = dataFile + new Date().getTime();
		final File file = new File(dataFile);
		if (file.exists()) {
			if (!file.renameTo(new File(bkFile))) {
				System.err.println("Falhou a alteração do nome '" + dataFile + "' do ficheiro para '" + bkFile + "'!");
				return false;
			} else {
				// System.out.println("Ficheiro de backup gerado '" + bkFile+
				// "'!");
			}
		}
		OutputStream out = null;
		boolean x = false;
		try {
			out = new FileOutputStream(dataFile);
			writeDocument(D, out);
			x = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (out != null)
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return x;
	}

	/**
	 * implementação da escrita da arvore num ficheiro recorrendo ao XSLT
	 * 
	 * @param input  arvore DOM
	 * @param output stream usado para escrita
	 */
	public static final void writeDocument(final Document input, final OutputStream output) {
		try {
			DOMSource domSource = new DOMSource(input);
			StreamResult resultStream = new StreamResult(output);
			TransformerFactory transformFactory = TransformerFactory.newInstance();

			// transformação vazia

			Transformer transformer = transformFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
			if (input.getXmlEncoding() != null)
				transformer.setOutputProperty(OutputKeys.ENCODING, input.getXmlEncoding());
			else
				transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			try {
				transformer.transform(domSource, resultStream);
			} catch (javax.xml.transform.TransformerException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// permite testar se o poema tem a palavra assinalada em parametro
	public boolean contem(String palavra) {
		Element root = D.getDocumentElement();
		NodeList versos = root.getElementsByTagName("verso");
		for (int i = 0; i < versos.getLength(); i++) {
			if (estaPresente(palavra, versos.item(i).getTextContent()))
				return true;
		}
		return false;
	}

	/**
	 * Devolve o título do poema
	 */
	public String getTitulo() {
		Element root = D.getDocumentElement();
		Element titulo = (Element) root.getElementsByTagName("título").item(0);
		if (titulo == null)
			return null;
		return titulo.getTextContent();
	}

	// permite testar se o poema tem o título indicado
	public boolean temTitulo(String titulo) {
		return titulo.compareTo(getTitulo()) == 0;
	}

	// permite testar se o poema tem as palavras assinaladas em parametro
	public boolean contem(String[] palavras) {
		for (int i = 0; i < palavras.length; i++)
			if (!contem(palavras[i]))
				return false;
		return true;
	}

	// devolve os nome dos ficheiros com poemas
	public static String[] getPoemas() {
		return XMLReadWrite.getFiles(poemas);
	}

	// Devolve os poemas que incluam um conjunto de palavras. rever!!!!
	public static ArrayList<Poema> Obter(String[] palavras) {
		ArrayList<Poema> docPoemas = new ArrayList<Poema>();
		String[] xmlPoemas = getPoemas();
		for (int i = 0; i < xmlPoemas.length; i++) {
			Poema p = new Poema(xmlPoemas[i]);
			if (p.contem(palavras))
				docPoemas.add(p);
		}
		return docPoemas;
	}

	// Devolve um poema com um determinado título.
	public static Document Consultar(String titulo) {
		String[] poemas = getPoemas();
		for (int i = 0; i < poemas.length; i++) {
			Poema p = new Poema(poemas[i]);
			if (p.temTitulo(titulo))
				return p.DOMDoc();
		}
		return null;
	}

	// Lista os títulos dos poemas existentes.
	public static Document Listar() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
		Document titulos = builder.newDocument();
		// create the root element node
		Element root = titulos.createElement("títulos");
		titulos.appendChild(root);
		// create a comment node given the specified string
		Comment comment = titulos.createComment("Lista de títulos dos poemas");
		titulos.insertBefore(comment, root);
		String[] poemas = getPoemas();
		for (int i = 0; i < poemas.length; i++) {
			// add titulo
			// System.out.println(i+"-"+poemas[i]);
			Poema p = new Poema(poemas[i]);
			String t = p.getTitulo();
			if (t != null) {
				Element tituloElement = titulos.createElement("título");
				tituloElement.setTextContent(t);
				root.appendChild(tituloElement);
			}
		}
		return titulos;
	}

	public void addComment(String com) {
		if (D == null)
			return;
		Node element = D.getElementsByTagName("poema").item(0);
		Comment comment = D.createComment(com);
		element.appendChild(comment);
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Indique o nome do ficheiro (em " + Poema.contexto + ") com o poema (ex: poema.xml):");
		String poemaFileName = sc.nextLine();
		if (poemaFileName.length() == 0) {
			poemaFileName = "poema.xml";
			System.out.println("Foi assumido o poema representado em: " + poemaFileName);
		}
		Poema pm = new Poema(poemaFileName);
		pm.menu();
		sc.close();
	}
}
