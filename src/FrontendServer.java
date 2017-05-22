import com.savarese.rocksaw.net.RawSocket;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by ducnx on 5/19/2017.
 */
public class FrontendServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        // RUN LISTEN SOCKET
        FrontendListenSocket frontendListenSocket = new FrontendListenSocket();
        frontendListenSocket.open(RawSocket.PF_INET, RawSocket.getProtocolByName("ip"));
        frontendListenSocket.setReceiveBufferSize(200);
        frontendListenSocket.write(InetAddress.getLocalHost(), new byte[0]);
        frontendListenSocket.listen(Integer.parseInt(args[0]));
    }
}
