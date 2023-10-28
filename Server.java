import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
    private ServerSocket socket;
    private Socket client;
    private ArrayList<ServerClient> clients;

    public Server(int port) {
        try {
            socket = new ServerSocket(port);
            System.out.printf("Hosting on %s:%s\n", InetAddress.getLocalHost(), port);
            clients = new ArrayList<ServerClient>();
        } catch (IOException e) {}
    }

    public void acceptClients() {
        try {
            while (true) {
                client = socket.accept();
                clients.add(new ServerClient(client, this));
                clients.get(clients.size()-1).start();
            }
        } catch (IOException e) {}
    }

    public void broadcast(String Message) {
        System.out.println(Message);
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).sendMessage(Message);
        }
    }

    public void dropClient(ServerClient client) {
        clients.remove(client);
    }

    public static void main(String[] args) {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 5000;
        }

        Server server = new Server(port);
        server.acceptClients();
    }
}
