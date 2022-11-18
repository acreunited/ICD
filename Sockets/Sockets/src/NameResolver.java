
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NameResolver {

    public static void main(String[] args) {
    	// nome ou endere�o ip de uma m�quina remota, existente na rede local ou na Internet
        String remoteHost = "pfenvy"; // "www.isel.ipl.pt"; 
        
        try {
        	// ontem o endere�o da m�quina local
            InetAddress address = InetAddress.getLocalHost();
            String nome = address.getHostName();
            String ip = address.getHostAddress();
            System.out.print("M�quina local: " + address);
            System.out.println(" -> nome: " + nome + " tem o IP: " + ip);
             
            // Obter todos os endere�os da m�quina local
            System.out.println("Interfaces de rede da m�quina local:");
            InetAddress [] addresses = InetAddress.getAllByName(nome);
            for (int i=0; i < addresses.length; ++i) {
            	ip = addresses[i].getHostAddress();
                nome = addresses[i].getHostName();
                System.out.println(i+"- nome: " + nome + " tem o IP: " + ip);
            }

            // Nome -> InetAddress m�quina remota
            address = InetAddress.getByName(remoteHost);
            ip = address.getHostAddress();
            nome = address.getCanonicalHostName();
            System.out.print("M�quina remota: " + address);
            System.out.println(" -> nome: " + nome + " tem o IP: " + ip);
            
            // Obter todos os endere�os de uma m�quina remota
            System.out.println("Interfaces de rede da m�quina remota:");
            addresses = InetAddress.getAllByName(remoteHost);
            for (int i=0; i < addresses.length; ++i) {
            	ip = addresses[i].getHostAddress();
                nome = addresses[i].getHostName();
                System.out.println(i+"- nome: " + nome + " tem o IP: " + ip);
            }
            
            // Returns the raw IP address of this InetAddressobject. 
            // The result is in network byte order: 
            // the highest orderbyte of the address is in getAddress()[0].
            byte[] rawIp = address.getAddress();
            InetAddress addr2 = InetAddress.getByAddress(rawIp);
            System.out.println("-raw IP address->" + addr2.getHostName());

        } catch (UnknownHostException uhe) {
            System.err.println(uhe);
            System.err.println("Nao foi possivel encontrar " + remoteHost);
        }
    }

}
