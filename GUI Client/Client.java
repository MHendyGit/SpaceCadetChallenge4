import java.io.*;
import java.net.*;

public class Client extends Thread{
    private Socket socket;
    private BufferedReader stdin;
    private PrintWriter stdout;
    private String name;
    private Boolean connected = true;
    private BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
    private ClientGui gui;

    public Client() {
        try {
            System.out.print("Enter host ip: ");
            String ip = b.readLine();
            System.out.print("Enter port: ");
            int port = Integer.parseInt(b.readLine());

            socket = new Socket(ip, port);
            socket.setSoTimeout(2000);
            System.out.printf("Connected to %s:%s\n", ip, port);

            System.out.print("Enter name: ");
            name = b.readLine();

            stdin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            stdout = new PrintWriter(socket.getOutputStream(), true);

            gui = new ClientGui(this);

        } catch (IOException e) {
            System.out.println("Connection failed");
            System.exit(0);
        } 
    }

    public void sendMessage(String message) {
        stdout.printf("[%s] %s\n", name, message);
    }

    public void close() {
        connected = false;
        try {
            b.close();
            stdin.close();
            stdout.close();
            socket.close();
        } catch (IOException e) {}
    }

    public void run() {
        String line = "";
        while (connected) {
            try {
                line = stdin.readLine();
                gui.displayMessage(line);
            } catch (IOException e) {}
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}