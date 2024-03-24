package bai2;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.SwingConstants;

public class Client2 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private Socket Client_Socket;
	private DataInputStream Client_Datain;
	private BufferedReader Client_Reader;
	private DataOutputStream Client_Dataout;
	private BufferedWriter Client_Writer;
	private JLabel timer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Socket socket = new Socket("localhost", 51002);
					
					Client2 frame = new Client2(socket);
					frame.updateTimer();
					
					frame.setVisible(true);
					
					frame.requestUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Client2(Socket sk1) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		setTitle("Bai2");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// setting up for socket
		try {
			// setup Client_Socket
			Client_Socket = sk1;
			
			// setup Client_Input
			Client_Datain = new DataInputStream(sk1.getInputStream());
			Client_Reader = new BufferedReader(new InputStreamReader(Client_Datain));
			
			// setup Client_Output
			Client_Dataout = new DataOutputStream(sk1.getOutputStream());
			Client_Writer = new BufferedWriter(new OutputStreamWriter(Client_Dataout));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		timer = new JLabel("New label");
		timer.setHorizontalAlignment(SwingConstants.CENTER);
		timer.setFont(new Font("Tahoma", Font.PLAIN, 16));
		timer.setBounds(243, 223, 355, 52);
		contentPane.add(timer);
	}
	
	public void updateTimer() {
		Thread thread_ReceiveMsg = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String msgReceived;
					
					while (Client_Socket.isConnected()) {
						msgReceived = Client_Reader.readLine();
						timer.setText(msgReceived);
					}
				} catch (IOException e) {
					System.out.println("Client disconnected succesfully!");
				}
			}
		});
		
		thread_ReceiveMsg.start();
	}
	
	
	public void requestUpdate() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {					
					while (Client_Socket.isConnected()) {
						Client_Writer.write("time");
						Client_Writer.newLine();
						Client_Writer.flush();
						Thread.sleep(1000);
					}
				} catch (IOException e) {
					System.out.println("sendMessage method break, check your server connection!");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}			
			}
		});
		
		thread.start();
	}
	
}
