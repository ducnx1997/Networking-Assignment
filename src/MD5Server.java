import com.savarese.rocksaw.net.RawSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ducnx on 5/20/2017.
 */
public class MD5Server {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        MD5ListenSocket md5ListenSocket = new MD5ListenSocket();
        md5ListenSocket.open(RawSocket.PF_INET, RawSocket.getProtocolByName("ip"));
        md5ListenSocket.write(InetAddress.getLocalHost(), new byte[0]);
        md5ListenSocket.setReceiveBufferSize(200);
        md5ListenSocket.listen(Integer.parseInt(args[0]));
    }
}
