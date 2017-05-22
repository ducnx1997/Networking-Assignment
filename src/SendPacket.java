import com.savarese.rocksaw.net.RawSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * Created by ducnx on 5/20/2017.
 */
public class SendPacket extends Thread {
    public int isACK;
    public RawSocket socket;
    public Packet packet;
    public InetAddress ip;
    byte[] data = new byte[200];
    byte[] packetBytes;

    public SendPacket(RawSocket socket, Packet packet, InetAddress ip) {
        this.isACK = 0;
        this.socket = socket;
        this.packet = packet;
        this.ip = ip;
        packetBytes = packet.toBytes();
    }

    @Override
    public void run() {
        // SEND THREAD
        new Thread(() -> {
            while (this.isACK == 0) {
                try {
                    System.out.println("Sending from " + packet.sourcePort + " to " + packet.destPort + " message: " + packet.message);
                    socket.write(ip, packetBytes);
                    Thread.sleep(10000);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // LISTEN TO ACK THREAD
        while (this.isACK == 0) {
            try {
                int dataLength = socket.read(data);
                if (dataLength >= 40) {
                    Packet revPacket = new Packet(Arrays.copyOfRange(data, 20, dataLength));
                    if (revPacket.destPort == packet.sourcePort && revPacket.sourcePort == packet.destPort && revPacket.checksum == revPacket.calChecksum() && revPacket.isACK == 1) {
                        this.isACK = 1;
                        System.out.println(packet.sourcePort + " received ACK from " + packet.destPort);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        System.out.println("Sent from " + packet.sourcePort + " to " + packet.destPort + " message: " + packet.message);
    }
}
