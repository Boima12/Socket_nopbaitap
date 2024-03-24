// WARNING: THIS CODE ONLY SUPPORT 1 CLIENT AT ONCE. MULTIPLE CLIENTS NEEDS Handler.java file to work. :d
package bai2;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Server {
	private ServerSocket Server_ServerSocket;
	private Socket Server_Socket;
	
	private DataInputStream Server_Datain;
	private BufferedReader Server_Reader;
	private DataOutputStream Server_Dataout;
	private BufferedWriter Server_Writer;
	
	private String currentTime;
	
	public Server(ServerSocket svSocketValue1) {
		this.Server_ServerSocket = svSocketValue1;
	}
	
	public void Server_Start() {
		try {
			// Start the timer
			TimeStart();
			
			while (!Server_ServerSocket.isClosed()) {
				Socket Server_Client = Server_ServerSocket.accept();
				System.out.println("connected!");
				this.Server_Socket = Server_Client;
				this.Server_Datain = new DataInputStream(Server_Client.getInputStream());
				this.Server_Reader = new BufferedReader(new InputStreamReader(Server_Datain));
				this.Server_Dataout = new DataOutputStream(Server_Client.getOutputStream());
				this.Server_Writer = new BufferedWriter(new OutputStreamWriter(Server_Dataout));
				
				Server_Listening(); // this is the reason why this code only support 1 client at once
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void TimeStart() {
		Thread thread = new Thread(new Runnable() {
			
			private Calendar calendar = Calendar.getInstance();
			private Date customDate;
			private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			
			@Override
			public void run() {
				calendar.set(1, 1, 1, 7, 0, 0);
				
				while (true) {
					try {
						calendar.add(Calendar.SECOND, 1);
						customDate = calendar.getTime();
						setCurrentTime(sdf.format(customDate));
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		
		thread.start();
	}
	
	
	public void Server_Listening() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				String msgFromClient;
				try {
					while (Server_Socket.isConnected()) {
						msgFromClient = Server_Reader.readLine();
						System.out.println(currentTime);
						broadcastMessage(currentTime);
					}
				} catch (IOException e) {
//					e.printStackTrace();
					System.out.println("Client disconnected!");
				}
			}
		});
		
		thread.start();
	}
	
	
	public void broadcastMessage(String msgToBroad) {
		try {
			Server_Writer.write(msgToBroad);
			Server_Writer.newLine();
			Server_Writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String []args) {
		try {
			ServerSocket Server001 = new ServerSocket(51002);
			
			Server ServerObj = new Server(Server001);
			System.out.println("Server started! Waiting for connection...");
			ServerObj.Server_Start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
}