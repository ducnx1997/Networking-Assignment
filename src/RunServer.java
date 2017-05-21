import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ducnx on 5/21/2017.
 */
public class RunServer {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        new Thread(() -> {
            try {
                FrontendServer.main(new String[]{args[0]});
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                MD5Server.main(new String[]{args[1]});
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {

                try {
                    SHA256Server.main(new String[]{args[2]});
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
        }).run();
    }
}
