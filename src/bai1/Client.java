package bai1;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
	
	private Socket Client_Socket;
	private DataInputStream Client_Datain;
	private BufferedReader Client_Reader;
	private DataOutputStream Client_Dataout;
	private BufferedWriter Client_Writer;
	
	public Client(Socket sk1) {
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
	}
	
	public void Client_ReceiveMsg() {
		Thread thread_ReceiveMsg = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String msgReceived;
					
					while (Client_Socket.isConnected()) {
						msgReceived = Client_Reader.readLine();
						System.out.println(msgReceived);
					}
				} catch (IOException e) {
					System.out.println("Client disconnected succesfully!");
				}
			}
		});
		
		thread_ReceiveMsg.start();
	}
	
	public void sendMessage() {
		try {					
			Scanner scanner = new Scanner(System.in);
			String msgToSend;
			while (Client_Socket.isConnected()) {
				msgToSend = scanner.nextLine();
				Client_Writer.write(msgToSend);
				Client_Writer.newLine();
				Client_Writer.flush();
				
			}
			
			scanner.close();
		} catch (IOException e) {
			System.out.println("sendMessage method break, check your server connection!");
		}

	}
	
	public static void main(String []args) {
		try {
			Socket socket = new Socket("localhost", 51002);
			
			Client ClientObj = new Client(socket);
			ClientObj.Client_ReceiveMsg();
			ClientObj.sendMessage();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}