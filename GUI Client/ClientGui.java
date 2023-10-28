import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGui {
    private Client client;
    private JTextField msgEntr;
    private JTextArea msgDsp;

    public ClientGui(Client client) {
        this.client = client;

        JFrame frame = new JFrame("CadetChat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                client.close();
            }
        });

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Message:");
        msgEntr = new JTextField(10);
        msgEntr.addActionListener(new SendAction());

        JButton send = new JButton(new SendAction("Send"));
        panel.add(label);
        panel.add(msgEntr);
        panel.add(send);

        msgDsp = new JTextArea();
        msgDsp.setEditable(false);

        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, msgDsp);
        frame.setVisible(true);
    }

    public void displayMessage(String message) {
        msgDsp.insert(message + "\n", msgDsp.getText().length());
    }

    private class SendAction extends AbstractAction {
        public SendAction(String name) {
            super(name);

        }
        public SendAction() {
            super();
        }

        public void actionPerformed(ActionEvent e) {
            String message = msgEntr.getText();
            client.sendMessage(message);
            msgEntr.setText("");
        }
    }
}
