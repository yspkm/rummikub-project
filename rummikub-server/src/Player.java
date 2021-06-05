import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class Player extends TileHolder implements Cloneable 
{
	private int player_id;
	private String player_name;
	private boolean registered;
	private int sum_of_num;
	int point;
	ArrayList<Tile> player_tiles;
	Scanner scn = new Scanner(System.in);
	DataOutputStream dos;
	DataInputStream dis;
	Table table;
	String name;
	boolean is_winner = false;

	Player(int player_num, DataOutputStream dos, DataInputStream dis, Table table, String name) 
	{
		this.name = name;
		this.registered = false;
		this.player_id = player_num;
		this.dos = dos;
		this.dis = dis;
		this.table = table;
		this.sum_of_num = 0;
		this.point = 0;
	}

	void pushTile(Tile tile) 
	{
		holder.put(tile.getSerial(), tile);
	}

	Tile manualTakeOutTile(String serial) 
	{
		Tile ret = this.holder.get(serial);
		this.holder.remove(serial);
		return ret;
	}

	public void printPlayerSimply() throws IOException 
	{
		player_tiles = super.getTileList();
		dos.writeUTF("PlayerHeader " + this.name + ": " + player_tiles.size() + " tiles\n");
		String msg = "";
		for (int i = 0; i < player_tiles.size(); i++) {
			Tile temp = player_tiles.get(i);
			msg += temp.getTileString() + " ";
		}
		msg = "Player " + msg;
		dos.writeUTF(msg);
	}

	public void printPlayer() throws IOException 
	{
		printPlayerSimply();
		while (true) {
			dos.writeUTF("View in Group(1) / Run(2), (-1: continue)");
			String input = dis.readUTF();
			if (input.equals("1")) {
				this.printPlayerByGroup();
			} else if (input.equals("2")) {
				this.printPlayerByRun();
			} else if (input.equals("-1")) {
				break;
			} else {
				continue;
			}
		}
	}

	public void printPlayerByGroup() throws IOException 
	{
		dos.writeUTF("PlayerHeader " + this.name + ": " + player_tiles.size() + " tiles\n");
		player_tiles = getTileListgrouped(player_tiles);
		String msg = "";
		for (int i = 0; i < player_tiles.size(); i++) {
			Tile temp = player_tiles.get(i);
			msg += temp.getTileString() + " ";
		}
		msg = "Player " + msg;
		dos.writeUTF(msg);
	}

	public void printPlayerByRun() throws IOException 
	{
		dos.writeUTF("PlayerHeader " + this.name + ": " + player_tiles.size() + " tiles\n");
		player_tiles = getTileListRun(player_tiles);
		String msg = "";
		for (int i = 0; i < player_tiles.size(); i++) {
			Tile temp = player_tiles.get(i);
			msg += temp.getTileString() + " ";
		}
		msg = "Player " + msg;
		dos.writeUTF(msg);
	}

	public ArrayList<Tile> selectTileList(Table table) throws IOException 
	{
		this.table = table;
		Tile temp_tile;
		int color, number;
		ArrayList<Tile> ret = new ArrayList<Tile>();

		dos.writeUTF("any integer to continue (-1: stop)");

		int condition = Integer.parseInt(dis.readUTF());

		if (condition != -1) { // 고르기 시작
			while (true) {
				while (true) {
					dos.writeUTF("Color (Black=1, Red=2, Yellow=3, Blue=4) : ");
					color = Integer.parseInt(dis.readUTF());
					if (BLACK <= color && color <= BLUE)
						break; // 정상입력
					dos.writeUTF("input (1~4)\n");
				}
				while (true) {
					dos.writeUTF("Number (1 ~ 13) : ");
					number = Integer.parseInt(dis.readUTF());
					if (1 <= number && number <= 13)
						break; // 정상입력
					dos.writeUTF("input (1~13)\n");
				}
				for (int i = 1; i <= 2; i++) {
					String serial = this.int2Serial(i, color, number);
					if (this.holder.containsKey(serial)) {
						temp_tile = this.manualTakeOutTile(serial);
						ret.add(temp_tile);
						break;
					}
				}
				int len = ret.size();
				String str = "";
				str += "Select \n";
				for (int i = 0; i < len; i++) {
					str += ret.get(i).getTileString() + " ";
				}
				dos.writeUTF(str);
				printPlayerSimply();
				str = "Type any integer to continue\n(-1: Stop, -2: View with Sort, -3: Cancel)";
				dos.writeUTF(str);
				// dos.writeUTF("Type any integer to continue (-1 to stop) : ");
				condition = Integer.parseInt(dis.readUTF());
				if (condition == -1) {
					break;
				} else if (condition == -2) {
					printPlayer();
				} else if (condition == -3) {
					for (int i = 0; i < len; i++) {
						Tile temp = ret.get(i);
						this.holder.put(temp.getSerial(), temp);
					}
					ret.clear();
					str = "Select ";
					dos.writeUTF(str);
					printPlayerSimply();
				}
			}
		}
		// 테이블 고르는 부분
		if (this.registered) {
			dos.writeUTF("Type any integer to select tile in table (-1: stop)");
			condition = Integer.parseInt(dis.readUTF());
			if (table.table_set.size() == 0) {
				dos.writeUTF("No TileSet in Table\n");
			} else if (condition != -1) {
				int set_index;
				int table_size = table.table_set.size();
				while (true) {
					while (true) {
						String msg_select_set = String.format("Type Set index(0 ~ %d)", table_size - 1);
						dos.writeUTF(msg_select_set);
						set_index = Integer.parseInt(dis.readUTF());
						if (0 <= set_index && set_index < table_size)
							break; // 정상입력
						msg_select_set = String.format("input (0 ~ %d)", table_size - 1);
						dos.writeUTF(msg_select_set);
					}
					while (true) {
						dos.writeUTF("Color (black=1, red=2, yello=3, blue=4)");
						color = Integer.parseInt(dis.readUTF());
						if (BLACK <= color && color <= BLUE)
							break; // 정상입력
						dos.writeUTF("input (1~4)\n");
					}
					while (true) {
						dos.writeUTF("Number (1 ~ 13)");
						number = Integer.parseInt(dis.readUTF());
						if (1 <= number && number <= 13)
							break; // 정상입력
						dos.writeUTF("input (1~13)");
					}
					for (int i = 1; i <= 2; i++) {
						String serial = this.int2Serial(i, color, number);
						temp_tile = null;
						if (this.table.table_set.get(set_index).tile_set.containsKey(serial)) {
							temp_tile = table.table_set.get(set_index).extractTile(serial);
							if (temp_tile != null) {
								ret.add(temp_tile);
								table.printTable();
								break;
							}
						}
						if (i == 2)
							dos.writeUTF("No such tile in the set");
					}
					int len = ret.size();
					String send_selections = "Select ";
					for (int i = 0; i < len; i++) {
						send_selections += ret.get(i).getTileString() + " ";
					}
					send_selections += "\n";
					dos.writeUTF(send_selections);
					send_selections = "Type any integer to continue\n(-1: Stop)";
					dos.writeUTF(send_selections);
					// dos.writeUTF("Type any integer to continue (-1 to stop) : ");
					condition = Integer.parseInt(dis.readUTF());
					if (condition == -1) {
						break;
					} 
				}
			}
		} // 고르기 종료
		return ret;
	}

	public boolean putTileList(ArrayList<Tile> selected_tiles_list, Table table) throws IOException 
	{
		int set_index = -1;
		if (this.registered) {
			dos.writeUTF("Type set index(-1: new set)");
			set_index = Integer.parseInt(dis.readUTF());
		}
		table.setTileSet(set_index, selected_tiles_list);
		if (!table.isRightTable())
			return false;
		return true;
	}

	private String int2Serial(int mark, int color, int number) 
	{
		String ret = "";
		ret += String.valueOf(mark) + String.valueOf(color) + String.valueOf(number);
		return ret;
	}

	@Override
	public Player clone() 
	{
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		Player ret = (Player) obj;
		ArrayList<Tile> tiles = ret.getTileList();
		ret.holder = new HashMap<String, Tile>();
		for (int i = 0; i < tiles.size(); i++) {
			Tile temp = tiles.get(i);
			String serial = temp.getSerial();
			ret.holder.put(serial, temp);
		}
		return ret;
	}

	// getter
	public int getPlayerId() 
	{
		return this.player_id;
	}

	public String getPlayerName() 
	{
		return this.player_name;
	}

	public boolean isRegistered() 
	{
		return this.registered;
	}

	public int getSum() 
	{
		return this.sum_of_num;
	}

	public int getPoint() 
	{
		return this.point;
	}

	// setter
	public void setPlayerName(String player_name) 
	{
		this.player_name = player_name;
	}

	public void setRegistered(boolean b) {
		this.registered = b;
	}

	public void setSum() 
	{
		for (Tile tile : getTileList()) {
			this.sum_of_num += tile.getNum();
		}
	}
}