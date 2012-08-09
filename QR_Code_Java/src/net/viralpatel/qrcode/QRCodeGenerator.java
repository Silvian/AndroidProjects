package net.viralpatel.qrcode;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

public class QRCodeGenerator {

	private QRCodeJava qrGenerator = new QRCodeJava();
	private QRListener qrListener = new QRListener();
	private JFrame frame;
	private final JLabel lblQrCodeGenerator = new JLabel("QR Code Generator");
	private JTextField textFieldContent;
	private JTextField textFieldLocation;
	private JButton btnGenerate = new JButton("Generate");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QRCodeGenerator window = new QRCodeGenerator();
					window.frame.setResizable(false);
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
	public QRCodeGenerator() {
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
		lblQrCodeGenerator.setBounds(167, 6, 290, 33);
		frame.getContentPane().add(lblQrCodeGenerator);
		
		JLabel lblContent = new JLabel("Content:");
		lblContent.setBounds(35, 63, 85, 15);
		frame.getContentPane().add(lblContent);
		
		textFieldContent = new JTextField();
		textFieldContent.setBounds(113, 57, 264, 27);
		frame.getContentPane().add(textFieldContent);
		textFieldContent.setColumns(10);
		
		JLabel lblLocation = new JLabel("Location:");
		lblLocation.setBounds(35, 139, 85, 15);
		frame.getContentPane().add(lblLocation);
		
		textFieldLocation = new JTextField();
		textFieldLocation.setText("/home/silvian/JavaProjects/AndroidProjects/QR_Code.PNG");
		textFieldLocation.setBounds(113, 133, 264, 27);
		frame.getContentPane().add(textFieldLocation);
		textFieldLocation.setColumns(10);
		
		
		btnGenerate.setBounds(164, 202, 122, 27);
		frame.getContentPane().add(btnGenerate);
		
		//Add Listeners
		textFieldContent.addActionListener(qrListener);
		textFieldLocation.addActionListener(qrListener);
		btnGenerate.addActionListener(qrListener);
	}
	
	/**
	 * Add class functions listener to the frame
	 */
	private class QRListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			if(textFieldLocation.getText().equals("")){
				JOptionPane.showMessageDialog(frame, "Location field is empty.", "Warning",
				        JOptionPane.WARNING_MESSAGE); 
			}
			
			if(textFieldContent.getText().equals("")){
				JOptionPane.showMessageDialog(frame, "Content field is empty.", "Warning",
				        JOptionPane.WARNING_MESSAGE); 
			}
			
			else{
				qrGenerator.setContent(textFieldContent.getText());
				qrGenerator.setDestination(textFieldLocation.getText());
				
				qrGenerator.generate();
			}
			
		}
		
	}
}
