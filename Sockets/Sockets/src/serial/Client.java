package serial;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {

    public static void main(String[] args) throws IOException {
        // need host and port, we want to connect to the ServerSocket at port 5025
        Socket socket = new Socket("localhost", 5025);
        System.out.println("Connected!");

        // get the output stream from the socket.
        OutputStream outputStream = socket.getOutputStream();
        
        // a utilização de socket pode ser substituida pelo ficheiro
        // FileOutputStream outputStream = new FileOutputStream("WebContent/message.ser");
        
        // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                
        // make a bunch of messages to send.
        List<Message> listOfMessages = new ArrayList<>();
        listOfMessages.add(new Message("Hello from the other side!"));
        listOfMessages.add(new Message("How are you doing?"));
        listOfMessages.add(new Message("What time is it?"));
        listOfMessages.add(new Message("Hi hi hi hi."));
        
        listOfMessages.get(0).setTempInfo("Message 1");
        listOfMessages.get(1).setTempInfo("Message 2");
        listOfMessages.get(2).setTempInfo("Message 3");
        listOfMessages.get(3).setTempInfo("Message 4");
        

        System.out.println("Sending all messages to the ServerSocket:\n");
        
        listOfMessages.forEach((msg)-> msg.print());
        
        objectOutputStream.writeObject(listOfMessages);

        System.out.println("Closing socket and terminating program.");
        socket.close();
    }
}