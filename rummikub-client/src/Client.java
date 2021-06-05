import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

class Client extends JFrame implements Runnable
{
	public static void main(String[] ars)
	{
    	Client info = new Client();
        Thread thread = new Thread(info);
        thread.start();
	}
	public Client()
	{
		super("Entrance");
	}
	private JPanel info = new JPanel();
	private JLabel ip_label = new JLabel("IP");
	private JLabel port_label = new JLabel("Port");
	private JLabel player_label = new JLabel("Nickname");
	private JTextField ip_text = new JTextField();
	private JTextField port_text = new JTextField();
	private JTextField player_text = new JTextField();
	private JButton save_button = new JButton("Game Start");

	public void run()
	{
		this.setSize(270, 180);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setContentPane(info);
		info.setLayout(null);

		ip_label.setHorizontalAlignment(SwingConstants.LEFT);
		ip_label.setBounds(10, 10, 80, 25);
		info.add(ip_label);

		port_label.setHorizontalAlignment(SwingConstants.LEFT);
		port_label.setBounds(10, 40, 80, 25);
		info.add(port_label);

		player_label.setHorizontalAlignment(SwingConstants.LEFT);
		player_label.setBounds(10, 70, 80, 25);
		info.add(player_label);

		ip_text.setBounds(100, 10, 160, 25);
		ip_text.setText("localhost"); //default string
		info.add(ip_text);

		port_text.setBounds(100, 40, 160, 25);
		port_text.setText("8000"); //default string
		info.add(port_text);

		player_text.setBounds(100, 70, 160, 25);
		info.add(player_text);

		save_button.setBounds(10, 105, 250, 25);
		info.add(save_button);

		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.getContentPane().setBackground(new Color(202, 225, 255));

		save_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!ip_text.getText().trim().equals("") && port_text.getText().trim().matches("-?\\d+")
						&& !player_text.getText().trim().equals("")) {
					setVisible(false);
					ClientBackGround.args[0] = ip_text.getText().trim();
					ClientBackGround.args[1] = port_text.getText().trim();
					ClientBackGround.args[2] = player_text.getText().trim();
					new ClientBackGround();
				}
			}
		});
	}
}