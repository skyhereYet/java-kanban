import Manager.*;
import Server.HttpTaskServer;
import Server.KVServer;
import Server.KVTaskClient;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, ManagerSaveException {

        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer httpTaskServer = new HttpTaskServer();


    }
}
