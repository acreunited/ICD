package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class BroadcastingClient {
    private DatagramSocket socket;
    private InetAddress address;
    private int port=5025;
    private int expectedServerCount;
    private byte[] buf;
    private int SoTimeout=10000;  // milisegundos

    public BroadcastingClient(int expectedServerCount) throws Exception {
        this.expectedServerCount = expectedServerCount;
        this.address = InetAddress.getByName("255.255.255.255");
    }

    public int discoverServers(String msg) throws IOException {
        initializeSocketForBroadcasting();
        copyMessageOnBuffer(msg);

        // When we want to broadcast not just to local network, call listAllBroadcastAddresses() and execute broadcastPacket for each value.
        broadcastPacket(address);

        return receivePackets();
    }

    List<InetAddress> listAllBroadcastAddresses() throws SocketException {
        List<InetAddress> broadcastList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            broadcastList.addAll(networkInterface.getInterfaceAddresses()
                .stream()
                .filter(address -> address.getBroadcast() != null)
                .map(address -> address.getBroadcast())
                .collect(Collectors.toList()));
        }
        return broadcastList;
    }

    private void initializeSocketForBroadcasting() throws SocketException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);
    }

    private void copyMessageOnBuffer(String msg) {
        buf = msg.getBytes();
    }

    private void broadcastPacket(InetAddress address) throws IOException {
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
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
        	// socket.close();
        }
    }

    public void close() {
        socket.close();
    }
    
    public static void main(String args[]) throws Exception {
			new BroadcastingClient(5).discoverServers("Olá mundo!");
    }
}
