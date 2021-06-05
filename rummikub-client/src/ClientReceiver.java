import java.net.*;

import javax.swing.JOptionPane;

//import java.awt.HeadlessException;
import java.io.*;

public class ClientReceiver implements Runnable 
{
    Socket socket;
    DataInputStream input;
    ClientGUI client_gui;
    //Singleton client_Singleton;
    ClientReceiver (Socket socket, ClientGUI client_gui)
    {
        this.socket = socket;
        this.client_gui = client_gui;
    }
    @Override
    public void run()
    {
        try {
            input = new DataInputStream(socket.getInputStream());
        } catch (IOException e1) {
            //e1.printStackTrace();
        }
        while (input != null) {
            String str = "";
            try {
                str = input.readUTF();
            } catch (IOException e) {
                //e.printStackTrace();
            }
            if (str.split(" ")[0].equals("Table")) {
                client_gui.table_area.setText("");
                client_gui.table_area.append(str.substring(6).trim());
            }
            else if (str.split(" ")[0].equals("Pool")) {
                // client_gui.pool_area.setText("");;
                client_gui.pool_label.setText(str.trim());
            }
            else if (str.split(" ")[0].equals("PlayerHeader")) {
                client_gui.player_area.setText("");
                client_gui.player_area.append(str.substring(13)+"\n");
            }
            else if (str.split(" ")[0].equals("Player")) {
                client_gui.player_area.append(str.substring(7));
            }
            else if (str.split(" ")[0].equals("GameoverHeader")) {
                client_gui.guide_area.setText("");
                client_gui.guide_area.append(str.substring(15));
            }
            else if (str.split(" ")[0].equals("Gameover")) {
                client_gui.guide_area.append(str.substring(9));
            }
            else if (str.split(" ")[0].equals("Select")) {
                client_gui.selected_area.setText("");
                client_gui.selected_area.append(str.substring(7));
            }
            else if (str.split(" ")[0].equals("@popup")) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                   // e.printStackTrace();
                }
                //client_gui.setVisible(false);
                String msg = str.substring(7);
                //client_gui.endmsg = msg;
                input = null;
                JOptionPane.showMessageDialog(client_gui, msg);
                break;
            }
            else {
                if (!str.split(" ")[0].equals("Penalty")) {
                    client_gui.guide_area.setText("");
                }
                client_gui.guide_area.append(str.trim()+"\n");
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    }
}