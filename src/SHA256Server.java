import com.savarese.rocksaw.net.RawSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ducnx on 5/22/2017.
 */
public class SHA256Server {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        SHA256Socket sha256Socket = new SHA256Socket();
        sha256Socket.open(RawSocket.PF_INET, RawSocket.getProtocolByName("ip"));
        sha256Socket.write(InetAddress.getLocalHost(), new byte[0]);
        sha256Socket.listen(Integer.parseInt(args[0]));
    }
}
