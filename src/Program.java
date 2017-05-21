import com.savarese.rocksaw.net.RawSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ducnx on 5/19/2017.
 */
public class Program {
    public static void main(String[] args) throws InterruptedException, IOException {
        //RUN SERVER
        new Thread(() -> {
            try {
                RunServer.main(new String[] {args[0], args[1], args[2]});
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }).start();

        // SET TIMEOUT
        Thread.sleep(1000);

        // RUN CLIENT
        try {
            Client.main(new String[] {args[3], args[4], args[5]});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
