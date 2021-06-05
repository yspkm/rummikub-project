import javax.swing.*;
import java.awt.*;

public class ClientGUI extends JFrame
{
	JTextArea table_area;
	JTextArea pool_area;
	JTextArea selected_area;
	JTextArea player_area;
	JTextArea guide_area;
	JTextField text_field;
	JLabel table_label;
	JLabel selected_label;
	JLabel pool_label;
	JLabel player_label;
	JButton send_btn;
	String endmsg;
	//public static String user_name;

	public ClientGUI(String user_name)
	{
		super("Client Window (" + user_name +")");
		ImageIcon icon = new ImageIcon("send.jpg");
		//ImageIcon background = new ImageIcon("background.png");
		this.getContentPane().setBackground(new Color(202, 225, 255));
		this.setLayout(null);
		this.setResizable(false);
		table_label = new JLabel("TABLE");
		table_label.setFont(new Font("D2Coding", Font.PLAIN, 20));
		table_area = new JTextArea();
		table_area.setLineWrap(true);
		table_area.setEditable(false);

		pool_label = new JLabel("POOL NUM : ");
		pool_label.setFont(new Font("D2Coding", Font.PLAIN, 20));
		pool_area = new JTextArea();
		pool_area.setLineWrap(true);
		pool_area.setEditable(false);
		
		selected_label = new JLabel("YOUR SELECTION");
		selected_label.setFont(new Font("D2Coding", Font.PLAIN, 20));
		selected_area = new JTextArea();
		selected_area.setLineWrap(true);
		selected_area.setEditable(false);

		player_label = new JLabel("YOUR TILE");
		player_label.setFont(new Font("D2Coding", Font.PLAIN, 20));
		player_area = new JTextArea();
		player_area.setLineWrap(true);
		player_area.setEditable(false);

		guide_area = new JTextArea();
		guide_area.setLineWrap(true);
		guide_area.setEditable(false);

		JScrollPane table_scrPane = new JScrollPane(table_area);
		table_scrPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		JScrollPane pool_scrPane = new JScrollPane(pool_area);
		pool_scrPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		JScrollPane selected_scrPane = new JScrollPane(selected_area);
		selected_scrPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		JScrollPane player_scrPane = new JScrollPane(player_area);
		player_scrPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		JScrollPane guide_scrPane = new JScrollPane(guide_area);
		guide_scrPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		pool_label.setBounds(30, 20, 200, 50);
		add(pool_label);
		// pool_scrPane.setBounds(50, 50, 200, 50); // pool
		// add(pool_scrPane);

		table_label.setHorizontalAlignment(JLabel.CENTER);
		table_label.setBounds(300, 25, 200, 50);
		table_scrPane.setBounds(30, 70, 740, 150); // table
		add(table_label);
		add(table_scrPane);

		selected_label.setHorizontalAlignment(JLabel.CENTER);
		selected_label.setBounds(300, 230, 200, 50);
		selected_scrPane.setBounds(30, 270, 740, 80); // selected
		add(selected_label);
		add(selected_scrPane);

		player_label.setHorizontalAlignment(JLabel.CENTER);
		player_label.setBounds(300, 355, 200, 50);
		player_scrPane.setBounds(30, 390, 740, 100); // player
		add(player_label);
		add(player_scrPane);

		guide_scrPane.setBounds(30, 500, 270, 50); //guide
		add(guide_scrPane);

		text_field = new JTextField(null);
		text_field.setBounds(300, 500, 270, 50);
		text_field.setText("type message here"); //default message
		add(text_field);

		send_btn = new JButton(/*"Send"*/icon);
		send_btn.setBounds(570, 500, 200, 50);
		add(send_btn);

		setSize(800, 600);
		setLocation(100, 0);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	void showEndingMsg() {
		
	}


}