import java.io.*;
import java.net.*;

public class Client extends Thread{
    private Socket socket;
    private BufferedReader stdin;
    private PrintWriter stdout;
    private String name;
    private Boolean connected = true;
    private BufferedReader b = new BufferedReader(new InputStreamReader(System.in));

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

        } catch (IOException e) {
            System.out.println("Connection failed");
            System.exit(0);
        } 
    }

    private void sendMessages() {
        String line = "";
        while (!line.equals("exit")) {
            try {
                line = b.readLine();
                if (!line.equals("exit")) {
                    sendMessage(line);
                }
            } catch (IOException e) {}
        }
        connected = false;
        close();
    }

    private void sendMessage(String message) {
        stdout.printf("[%s] %s\n", name, message);
    }

    private void close() {
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
                System.out.println(line);
            } catch (IOException e) {}
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
        client.sendMessages();
    }
}