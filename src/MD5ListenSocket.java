import com.savarese.rocksaw.net.RawSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by ducnx on 5/20/2017.
 */
public class MD5ListenSocket extends RawSocket {
    public static int port;

    public void sendMD5Hash(String message) throws NoSuchAlgorithmException, UnknownHostException {
        int position = message.indexOf("|");
        String clientAdress = message.substring(1, position);
        message = message.substring(position + 1, message.length());
        int clientPort = Integer.parseInt(message.substring(0, 5));
        message = message.substring(5);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] mdbytes = md.digest(message.getBytes());
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++)
            stringBuffer.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        new SendPacket(this, new Packet(MD5ListenSocket.port, clientPort, 0, "MD5|" + stringBuffer.toString()), InetAddress.getByName(clientAdress)).run();
    }

    public void listen(int port) throws IOException, NoSuchAlgorithmException {
        byte[] data = new byte[1000];
        MD5ListenSocket.port = port;
        while (true) {
            int dataLength = this.read(data);
            if (dataLength >= 40) {
                Packet packet = new Packet(Arrays.copyOfRange(data, 20, dataLength));
                if (packet.destPort == port && packet.checksum == packet.calChecksum() && packet.isACK == 0) {
                    Packet resPacket = new Packet(packet.destPort, packet.sourcePort, 1,  packet.message);
                    this.write(InetAddress.getLocalHost(), resPacket.toBytes());
                    sendMD5Hash(packet.message);
                }
            }
        }
    }
}
