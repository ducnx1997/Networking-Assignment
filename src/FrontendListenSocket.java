import com.savarese.rocksaw.net.RawSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by ducnx on 5/20/2017.
 */
public class FrontendListenSocket extends RawSocket{
    public static int port;
    public void forwardMessage(String message, byte[] clientAddress) throws UnknownHostException, InterruptedException {
        String forwardMessage = (InetAddress.getByAddress(clientAddress)) +  "|" + message;
        new SendPacket(this, new Packet(FrontendListenSocket.port, MD5ListenSocket.port, 0, forwardMessage), InetAddress.getLocalHost()).run();
        new SendPacket(this, new Packet(FrontendListenSocket.port, SHA256Socket.port, 0, forwardMessage), InetAddress.getLocalHost()).run();
    }

    public void listen(int port) throws IOException, InterruptedException {
        byte[] data = new byte[1000];
        FrontendListenSocket.port = port;
        byte[] address = new byte[4];
        while (true) {
            int dataLength = this.read(data, address);
            if (dataLength >= 40) {
                Packet packet = new Packet(Arrays.copyOfRange(data, 20, dataLength));
                if (packet.destPort == port && packet.checksum == packet.calChecksum() && packet.isACK == 0) {
                    Packet resPacket = new Packet(packet.destPort, packet.sourcePort, 1,  packet.message);
                    System.out.println("Frontend received message: " + packet.message);
                    this.write(InetAddress.getByAddress(address), resPacket.toBytes());
                    forwardMessage(packet.message, address);
                }
            }
        }
    }

}
