import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.io.*;

public class ClientBackGround
{
	String server_ip;
	int port;
	String user_name;
	DataOutputStream output;
	ClientGUI client_gui;
	static String[] args = new String[3];

	public ClientBackGround()
	{
		args[1] = args[1].replaceAll("[^0-9]", "");
		this.server_ip = args[0];
		this.port = Integer.parseInt(args[1]);
		this.client_gui = new ClientGUI(args[2]);

		InetSocketAddress sockAddress = new InetSocketAddress(this.server_ip, this.port);
		Socket socket = new Socket();
		try {
			int wait_time = 7777;
			socket.connect(sockAddress, wait_time);
			InetAddress inetAdddress;
			if ((inetAdddress = socket.getInetAddress()) != null) {
				this.client_gui.guide_area.setText("Server Connected (" + inetAdddress+")\n");
				OutputStream cur_dout = socket.getOutputStream();
				DataOutputStream out = new DataOutputStream(cur_dout);
				out.writeUTF(this.args[2]);
				//out.close();
				//cur_dout.close();
				new Thread(new ClientReceiver(socket, client_gui)).start();
			} else {
				this.client_gui.text_field.setText("Connection Fail");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.client_gui.send_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!client_gui.text_field.getText().equals("")) {
					try {
						output = new DataOutputStream(socket.getOutputStream());
						while (true) {
							String msg = client_gui.text_field.getText();
							if (msg.length() > 0) {
								if (msg.charAt(0) == '-') {
									msg = msg.replaceAll("[^0-9]", "");
									if (msg.length() == 0) {
										client_gui.guide_area.setText("Enter ingeter!");
										continue;
									}
									msg = "-"+msg;
									output.writeUTF(msg);
									client_gui.text_field.setText("");
									break;
								}
								else {
									msg = msg.replaceAll("[^0-9]", "");
									if (msg.length() == 0) {
										client_gui.guide_area.setText("Enter ingeter!");
										continue;
									}
									output.writeUTF(msg);
									client_gui.text_field.setText("");
									break;
								}
							}
							client_gui.guide_area.setText("Enter ingeter!");
							continue;
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

	}
}
