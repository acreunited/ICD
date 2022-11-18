package serial;

import java.io.Serializable;

//must implement Serializable in order to be sent
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private String Info="?";
	private /*final*/ transient String tempInfo;  
	// whenever any final field/reference is evaluated as “constant expression“, 
	// it is serialized by JVM ignoring the presence of transient keyword.
	private static int count=0;

	public Message(String Info) {
		this.Info = Info;
		count++;
	}

	public void setTempInfo(String tempInfo) {
		this.tempInfo = tempInfo;
	}
	
	public void print() {
		System.out.println("Quantidade de mensagens: "+count);
		System.out.println("Informa disponível: "+Info);
		System.out.println("Informação temporária: "+tempInfo);
		System.out.println();
	}

}