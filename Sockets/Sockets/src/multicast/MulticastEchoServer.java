package multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastEchoServer {

    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];
    protected InetAddress group = null;
    private int port = 5025;

    public MulticastEchoServer() throws IOException {
        socket = new MulticastSocket(port);
        socket.setReuseAddress(true);
        //A multicast group is specified by a class D IP address and by a standard UDP port number. 
        //Class D IP addresses are in the range 224.0.0.0 to 239.255.255.255, inclusive. 
        //The address 224.0.0.0 is reserved and should not be used.
        group = InetAddress.getByName("228.5.6.7");
        socket.joinGroup(group);
    }

    public void run() {
        try {
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                String received = new String(packet.getData(), 0, packet.getLength());
                
                String messageStr = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Dados recebidos: " + messageStr);
                System.out.println("Número de bytes recebidos: " + packet.getLength());
                System.out.println("Endereço do cliente: " + packet.getAddress()
                        + " Porto: " + packet.getPort());
                
                if (received.equals("end")) {
                    break;
                }
                socket.send(packet);
            }
            socket.leaveGroup(group);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String args[]) throws IOException {
			new MulticastEchoServer().run();

    }
}
