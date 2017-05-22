/**
 * Created by ducnx on 5/17/2017.
 */

import com.savarese.rocksaw.net.RawSocket;

import java.io.IOException;
import java.net.InetAddress;

public class Client  {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        int clientPort = 1997;

        // LISTEN THREAD
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ClientSocket clientSocket = new ClientSocket();
                    clientSocket.open(RawSocket.PF_INET, RawSocket.getProtocolByName("ip"));
                    clientSocket.write(InetAddress.getLocalHost(), new byte[0]);
                    clientSocket.mess = args[2];
                    clientSocket.listen(clientPort);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // SEND MESSAGE
        RawSocket clientSendSocket = new RawSocket();
        clientSendSocket.open(RawSocket.PF_INET, RawSocket.getProtocolByName("ip"));
        clientSendSocket.setReceiveBufferSize(200);
        clientSendSocket.write(InetAddress.getLocalHost(), new byte[0]);
        String clientRevPort = Integer.toString(clientPort);
        while (clientRevPort.length() < 5) clientRevPort = "0" + clientRevPort;
        Packet packet = new Packet(clientPort, Integer.parseInt(args[1]), 0,clientRevPort + args[2]);
        System.out.println("Client " + clientPort + " send messge: " + args[2]);
        new SendPacket(clientSendSocket, packet, InetAddress.getByName(args[0])).run();
    }

}
