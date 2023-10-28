import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerClient extends Thread {
    private String address;
    private BufferedReader stdin;
    private PrintWriter stdout;
    private Server server;

    public ServerClient(Socket socket, Server server) {
        try {
            this.server = server;
            address = socket.getRemoteSocketAddress().toString();
            stdin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            stdout = new PrintWriter(socket.getOutputStream(), true);

            System.out.printf("New client %s connected\n", address);

        } catch (IOException e) {}
    }

    public void sendMessage(String message) {
        stdout.println(message);
    }

    public void run() {
        String line = "";
        while (!(line == null)) {
            try {
                line = stdin.readLine();
                if (!(line == null)) server.broadcast(line);
            } catch (IOException e) {}
        }
        server.dropClient(this);
        System.out.printf("Client %s disconnected\n", address);
        close();
    }

    public void close() {
        try {
            stdin.close();
            stdout.close();
        } catch (IOException e) {}
    }
    
}
