import Custom_Exception.KVTaskServerException;
import Server.HttpTaskServer;
import Server.KVServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, KVTaskServerException {

        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();


    }
}
