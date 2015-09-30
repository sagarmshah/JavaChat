import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;


public class ChatClientApp {

	private JFrame frame;
	private JTextField textField;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatClientApp window = new ChatClientApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChatClientApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnClear = new JButton("CLEAR");
		btnClear.setBounds(327, 229, 117, 29);
		frame.getContentPane().add(btnClear);
		
		JButton btnQuit = new JButton("QUIT");
		btnQuit.setBounds(6, 229, 117, 29);
		frame.getContentPane().add(btnQuit);
		
		textField = new JTextField();
		textField.setBounds(103, 172, 341, 28);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textArea = new JTextArea();
		textArea.setSize(341, 151);
		textArea.setLocation(103, 6);
        frame.getContentPane().add(textArea, "Center");
        
        JLabel lblEnterText = new JLabel("Enter Message");
        lblEnterText.setBounds(6, 175, 99, 22);
        frame.getContentPane().add(lblEnterText);
        
        JLabel lblChat = new JLabel("Chat");
        lblChat.setBounds(6, 6, 61, 16);
        frame.getContentPane().add(lblChat);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(458, 84, -226, 34);
        frame.getContentPane().add(scrollPane);
	
		
	}
}
