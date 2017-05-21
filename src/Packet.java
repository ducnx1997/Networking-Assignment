import java.nio.ByteBuffer;

/**
 * Created by ducnx on 5/20/2017.
 */
public class Packet {
    public int sourcePort;
    public int destPort;
    public int checksum;
    public int length;
    public int isACK;
    public String message;
    public int base = 1000000007;

    public int calChecksum() {
        // USING HASH
        int sum = sourcePort % base;
        sum = (sum + destPort) % base;
        sum = (sum + length) % base;
        sum = (sum + isACK) % base;
        for (int i = 0; i < message.length(); i++) sum = (sum + message.charAt(i)) % base;
        return sum;
    }

    public Packet(int sourcePort, int destPort, int isACK,  String message) {
        this.sourcePort = sourcePort;
        this.destPort = destPort;
        this.length = message.length();
        this.isACK = isACK;
        this.message = message;
        this.checksum = calChecksum();
    }

    public Packet(byte[] data) {
        //CREATE PACKET FROM READ BUFFER
        ByteBuffer byteBuffer =  ByteBuffer.wrap(data);
        sourcePort = byteBuffer.getInt();
        destPort = byteBuffer.getInt();
        checksum = byteBuffer.getInt();
        length = byteBuffer.getInt();
        isACK = byteBuffer.getInt();
        message = "";
        while (byteBuffer.hasRemaining()) message += byteBuffer.getChar();
    }

    public byte[] toBytes() {
        // CONVERT PACKET TO BYTE ARRAY
        ByteBuffer byteBuffer = ByteBuffer.allocate(20 + message.length() * 2);
        byteBuffer.putInt(sourcePort);
        byteBuffer.putInt(destPort);
        byteBuffer.putInt(checksum);
        byteBuffer.putInt(length);
        byteBuffer.putInt(isACK);
        for (int i = 0; i < length; i++) byteBuffer.putChar(message.charAt(i));
        return byteBuffer.array();
    }

    public String toString() {
        return "Source Port: " + sourcePort +
                "\nDest Port: " + destPort +
                "\nLength: " + length +
                "\nChecksum: " + checksum +
                "\nisACK: " + isACK +
                "\nMessage: " + message + "\n";
    }
}
