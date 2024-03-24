package bai1;

import java.io.*;
import java.net.*;

public class Server {
	private ServerSocket Server_ServerSocket;
	private Socket Server_Socket;
	
	private DataInputStream Server_Datain;
	private BufferedReader Server_Reader;
	private DataOutputStream Server_Dataout;
	private BufferedWriter Server_Writer;
	
	public Server(ServerSocket svSocketValue1) {
		this.Server_ServerSocket = svSocketValue1;
	}
	
	public void Server_Start() {
		try {
			while (!Server_ServerSocket.isClosed()) {
				Socket Server_Client = Server_ServerSocket.accept();
				this.Server_Socket = Server_Client;
				this.Server_Datain = new DataInputStream(Server_Client.getInputStream());
				this.Server_Reader = new BufferedReader(new InputStreamReader(Server_Datain));
				this.Server_Dataout = new DataOutputStream(Server_Client.getOutputStream());
				this.Server_Writer = new BufferedWriter(new OutputStreamWriter(Server_Dataout));
				
				// waiting for input from Client
				String msgReceived = "";
				
				if (Server_Socket.isConnected()) {
					msgReceived = Server_Reader.readLine();
					System.out.println("Client said: " + msgReceived);
				}
				
				// send messenger back to Client
				if (Server_Socket.isConnected()) {
					Server_Writer.write("The Server got the messenger: " + msgReceived);
					Server_Writer.newLine();
					Server_Writer.flush();
				}
				
			}
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
}