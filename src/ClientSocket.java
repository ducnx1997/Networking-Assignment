import com.savarese.rocksaw.net.RawSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by ducnx on 5/20/2017.
 */
public class ClientSocket extends RawSocket {
    public String mess;

    public void listen(int port) throws IOException, InterruptedException {
        byte[] data = new byte[1000];
        byte[] address = new byte[4];
        String SHA256Hash = null;
        String MD5Hash = null;
        int running = 2;
        while (running > 0) {
            int dataLength = this.read(data, address);
            if (dataLength >= 40) {
                Packet packet = new Packet(Arrays.copyOfRange(data, 20, dataLength));
                if (packet.destPort == port && packet.checksum == packet.calChecksum() && packet.isACK == 0) {
                    Packet resPacket = new Packet(packet.destPort, packet.sourcePort, 1,  packet.message);
                    this.write(InetAddress.getByAddress(address), resPacket.toBytes());
                    System.out.println("Client" + port + " receive hash: " + packet.message);
                    int position = packet.message.indexOf('|');
                    String hashType = packet.message.substring(0, position);
                    if (Objects.equals(hashType, "MD5") && MD5Hash == null) {
                        MD5Hash = packet.message.substring(position + 1, packet.message.length());
                        running--;
                    }
                    if (Objects.equals(hashType, "SHA-256") && SHA256Hash == null) {
                        SHA256Hash = packet.message.substring(position + 1, packet.message.length());
                        running--;
                    }
                }
            }
        }
        Thread.sleep(500);
        System.out.println("\n\n");
        System.out.println("Response from server: ");
        System.out.println("Hash value of \"" + mess + "\"");
        System.out.println("SHA 256: " + SHA256Hash);
        System.out.println("MD5: " + MD5Hash);
    }
}
