package multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class MulticastingClient {
    private DatagramSocket socket;
    private InetAddress group;
    private int port=5025;
    private int expectedServerCount;
    private byte[] buf;
    private int SoTimeout=10000;  // milisegundos

    public MulticastingClient(int expectedServerCount) throws Exception {
        this.expectedServerCount = expectedServerCount;
        this.socket = new DatagramSocket();
        this.group = InetAddress.getByName("228.5.6.7");
    }

    public int discoverServers(String msg) throws IOException {
        copyMessageOnBuffer(msg);
        multicastPacket();

        return receivePackets();
    }

    private void copyMessageOnBuffer(String msg) {
        buf = msg.getBytes();
    }

    private void multicastPacket() throws IOException {
        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, port);
        socket.send(packet);
    }

    private int receivePackets() throws IOException {
        int serversDiscovered = 0;
        while (serversDiscovered != expectedServerCount) {
        	if(socket.isClosed()) break;
            receivePacket();
            serversDiscovered++;
        }
        return serversDiscovered;
    }

    private void receivePacket() throws IOException {
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        
        socket.setSoTimeout(SoTimeout);   // set the timeout in millisecounds.

        try {
        	socket.receive(packet);   // recieve data until timeout

        	String messageStr = new String(packet.getData(), 0, packet.getLength());
        	System.out.println("Dados recebidos: " + messageStr);
        	System.out.println("Número de bytes recebidos: " + packet.getLength());
        	System.out.println("Endereço do servidor: " + packet.getAddress()
        	+ " Porto: " + packet.getPort());
        }
        catch (SocketTimeoutException e) {
        	// timeout exception.
        	System.out.println(e.getMessage());
        	socket.close();
        }
    }

    public void close() {
        socket.close();
    }
    
    public static void main(String args[]) throws Exception {
    	new MulticastingClient(10).discoverServers("Olá Mundo!");
    }
}
