import java.util.ArrayList;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.*;
//import java.io.InputStream;
//import java.io.OutputStream;

public class ServerSocketThread  
{
	ArrayList<ServerSender> sender_thread_list;
	ArrayList<ServerReceiver> receiver_thread_list;
	ArrayList<Socket> socket_list;
	boolean flag = true;
	int limit = 4;
	ServerSocket server_socket;
	//ArrayList<Socket> client_socket_list;

	ServerSocketThread(ServerSocket server, ArrayList<Socket> cl_list)
	{
		this.server_socket = server;
		this.socket_list = cl_list;
		sender_thread_list = new ArrayList<ServerSender>();
		receiver_thread_list = new ArrayList<ServerReceiver>();
	}

	public void acceptPlayers(ArrayList <String> client_name_list)
	{
		try {
			System.out.println("Server is listening on port " + this.server_socket.getLocalPort());
			for (int idx=1; idx < this.limit; idx++) {
				System.out.println("-- wating for [ " + idx + " ] client");
				Socket temp_socket = server_socket.accept();
				System.out.println("[ " + temp_socket.getInetAddress() + " ] client connected");
				socket_list.add(temp_socket);
				InputStream cur_din = temp_socket.getInputStream();
				DataInputStream in = new DataInputStream(cur_din);
				String temp_name = in.readUTF();
				client_name_list.add(temp_name);
			}
			for (int i = 0; i < this.limit; i++) {
				Socket socket = this.socket_list.get(i);
				ServerSender sender = new ServerSender(socket, i);
				ServerReceiver receiver = new ServerReceiver(socket, i);
				sender_thread_list.add(sender);
				receiver_thread_list.add(receiver);
				sender.start();
				receiver.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void sendToAll(String msg)
	{
		for (Thread sender : sender_thread_list) {
			try {
				((ServerSender) sender).output.writeUTF(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class ServerSender extends Thread
	{
		int id;
		Socket socket;
		DataOutputStream output;
		boolean flag;

		ServerSender(Socket socket, int idx) {
			this.id = idx;
			this.socket = socket;
			try {
				output = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {}
			flag = true;
		}

		@Override
		public void run()
		{
			while (this.flag) {
				try {
					Thread.sleep(10000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class ServerReceiver extends Thread
	{
		int id;
		Socket socket;
		DataInputStream input;
		boolean flag;

		ServerReceiver(Socket socket, int idx)
		{
			this.id = idx;
			this.socket = socket;
			try {
				input = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
			}
			flag = true;
		}

		@Override
		public void run()
		{
			while (this.flag) {
				try {
					Thread.sleep(10000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}