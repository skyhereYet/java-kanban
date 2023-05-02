import Custom_Exception.KVTaskServerException;
import Custom_Exception.ManagerSaveException;
import Server.HttpTaskServer;
import Server.KVServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException,
            ManagerSaveException {

        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();


    }
}
