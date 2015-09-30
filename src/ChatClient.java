import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



/**
 * Simple chat application using swings for the user interface.
 */
public class ChatClient {

    BufferedReader in;
    PrintWriter out;
    OutputStream os;
    Socket socket;
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);
    JButton quitButton = new JButton("Quit Chat");
    JButton clearButton = new JButton("Clear");
    JFrame newFrame = new JFrame("Welcome to Chat");
    JTextField  messageBox;
    JTextArea   chatBox;
    
    /**
     * This method is used to request messages that are delivered to the server
     * and it remains inactive until client receives NAMEACCEPTED message from server.
     */
    public ChatClient() {
    	textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        quitButton.setSize(80, 200);
        frame.getContentPane().add(quitButton, "South");
        frame.add(clearButton,"South");
        frame.pack();

        
        
        
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        quitButton.setSize(80, 200);
        frame.getContentPane().add(quitButton, "South");
        frame.add(clearButton,"South");
        frame.pack();

        // Add Listeners
        textField.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the textfield by sending
             * the contents of the text field to the server. Then clear
             * the text area in preparation for the next message.
             */
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
        
        quitButton.addActionListener(new ActionListener() {
			/**
			 * This listener responds to the button click for quitting the chat session.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				out.close();
				frame.dispose();
			}
		});
        
        clearButton.addActionListener(new ActionListener() {
			/**
			 * This listener responds to the button click for quitting the chat session.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				messageArea.setText("");
			}
		});
        
        
    }
    
    
    /**
     * Prompt for and return the desired screen name.
     * @throws InterruptedException 
     * @throws HeadlessException 
     */
    private String getName1() throws HeadlessException, InterruptedException {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a name for chatting",
            "Pick on screen name",
            JOptionPane.PLAIN_MESSAGE);
        
        
    }


    /**
     * Connects to the server then enters the processing loop.
     * @throws InterruptedException 
     */
    private void run() throws IOException, InterruptedException {

        // Make connection and initialize streams
        String serverAddress = "localhost";//getServerAddress();
        socket = new Socket(serverAddress, 9001);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Process all messages from server, according to the protocol.
        while (true) {
            String line = in.readLine();
            if (line.startsWith("SUBMITNAME")) {
            	String abc= getName1();
            	out.println(abc);
                frame.setTitle(abc);
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n");
            }
        }
    }

    /**
     * Runs the client as an application with a closeable frame.
     */
    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}