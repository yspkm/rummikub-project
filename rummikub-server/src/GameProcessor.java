import java.io.IOException;
import java.util.*;
import java.net.*;
import java.io.*;

public class GameProcessor extends GameManager
{
	final int port = 8000; // default
	Table table;
	Pool pool;
	Player[] player_arr;
	Tile[][][] tile_arr = new Tile[2][4][13];
	Random rand = new Random();
	ArrayList<Tile> selected_tiles_list;
	int[] scores = new int[4];
	int[] user_select_tile = new int[2];
	ServerSocketThread sst;
	Scanner scn = new Scanner(System.in);
	Table old_table;
	Player old_player;
	boolean penalty_registor; // 등록 관련 패널티
	ServerSocket server_socket;
	ArrayList<Socket> client_socket_list = new ArrayList<Socket>(0);
	ArrayList<String> client_name_list = new ArrayList<String>(0);

	GameProcessor() throws IOException 
	{
		System.out.println("initGame");
		server_socket = new ServerSocket(this.port);
		sst = new ServerSocketThread(this.server_socket, this.client_socket_list);
		System.out.println("Server is listening on port " + this.server_socket.getLocalPort());
		System.out.println("-- wating for [ " + 0 + " ] client");
		Socket cur_sk = this.server_socket.accept();
		System.out.println("[ " + cur_sk.getInetAddress() + " ] client connected");
		this.client_socket_list.add(cur_sk);

		OutputStream cur_dout = cur_sk.getOutputStream();
		DataOutputStream out = new DataOutputStream(cur_dout);
		InputStream cur_din = cur_sk.getInputStream();
		DataInputStream in = new DataInputStream(cur_din);
		while (true) {
			String temp_name = "wrong";
			try {
				temp_name = in.readUTF();
				client_name_list.add(temp_name);
			} catch (IOException e) {}
			if (!temp_name.equals("wrong")) {
				break;
			}
		}

		while (true) { // get user input - player number
			out.writeUTF("Number of Player (2~4)");
			num_of_players = Integer.parseInt(in.readUTF().trim());
			if (2 <= num_of_players && num_of_players <= 4) {// right input
				sst.limit = num_of_players;
				break;
			}
		}
		sst.acceptPlayers(this.client_name_list); // accept rest of players

		pool = new Pool(sst);
		for (int i = 0; i < 2; i++) { // make tile
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 13; k++) {
					this.tile_arr[i][j][k] = new Tile(i + 1, j + 1, k + 1);
					String serial = tile_arr[i][j][k].getSerial();
					pool.holder.put(serial, tile_arr[i][j][k]);
				}
			}
		}

		this.player_arr = new Player[num_of_players];
		table = new Table(sst);
		for (int i = 0; i < num_of_players; i++) { // make player & init player
			this.player_arr[i] = new Player(i, sst.sender_thread_list.get(i).output,
					sst.receiver_thread_list.get(i).input, table, this.client_name_list.get(i));
			tileDistributer(i);
			player_arr[i].printPlayerSimply();
			player_arr[i].setPlayerName(client_name_list.get(i));
		}
		table.printTable();
	}

	private void tileDistributer(int player_num) 
	{ // initially distribute tiles
		System.out.println("tileDistributer");
		Tile temp;
		for (int i = 0; i < 14; i++) {
			temp = pool.randTakeOutTile();
			temp.setLastPos("player");
			this.player_arr[player_num].pushTile(temp);
		}
	}

	void printGameState() throws IOException 
	{
		sst.sender_thread_list.get(whose_turn).output.writeUTF("\nIt's your turn!\n");
		table.printTable();
		pool.printPoolTileNum();
		player_arr[whose_turn].printPlayer();
	}

	public void moveTile() throws IOException 
	{
		this.old_table = table.clone();
		this.old_player = player_arr[whose_turn].clone();
		ArrayList<Tile> total_selected_list = new ArrayList<Tile>();
		boolean penalty = false;
		int more = 0;
		while (true) {
			if (more == -1) {
				if (!player_arr[whose_turn].isRegistered()) { // 등록 안했을 경우
					int total_tile_num = 0;
					for (Tile tile : total_selected_list) {
						total_tile_num += tile.getNum();
					}
					if (total_tile_num < 30) {
						sst.sender_thread_list.get(whose_turn).output.writeUTF("less than 30, fail to register");
						penalty = true;
					} else {
						if (!table.isRightTable()) {
							sst.sender_thread_list.get(whose_turn).output.writeUTF("wrong table, fail to register");
							penalty = true;
						} else {
							player_arr[whose_turn].setRegistered(true);
							sst.sender_thread_list.get(whose_turn).output.writeUTF("success to register");
							penalty = false;
						}
					}
				} else { // 등록한 경우
					if (!table.isRightTable()) {
						sst.sender_thread_list.get(whose_turn).output.writeUTF("wrong table");
						penalty = true;
					}
					if (total_selected_list.size() == 0) {
						sst.sender_thread_list.get(whose_turn).output.writeUTF("you did not move any tile");
						penalty = true;
					}
				}
				if (penalty) {
					// 원상복구
					player_arr[whose_turn] = this.old_player;
					this.table = this.old_table;
					sst.sender_thread_list.get(whose_turn).output.writeUTF("Penalty : get one tile from pool");
					Tile temp_tile = this.pool.randTakeOutTile(); // 풀에서 하나 꺼내서
					player_arr[whose_turn].pushTile(temp_tile); // 플레이어한테 줌
					if (selected_tiles_list != null) {
						selected_tiles_list.clear(); // clear list
					}
					total_selected_list.clear();
					table.printTable(); // 테이블 상태 출력 후
					player_arr[whose_turn].printPlayerSimply(); // 플레이어 상태 출력
				}
				break;
			} else {
				selectTile(); // 테이블의 타일셋 고르기 or 아예새로운 셋
				putTile();
				total_selected_list.addAll(selected_tiles_list);
				selected_tiles_list.clear();// clear list
				table.printTable(); // 테이블 상태 출력 후
				player_arr[whose_turn].printPlayerSimply(); // 플레이어 상태 출력
			}
			sst.sender_thread_list.get(whose_turn).output.writeUTF("Type any integer more tiles (-1: stop)");
			more = Integer.parseInt(sst.receiver_thread_list.get(whose_turn).input.readUTF());
		} // while
	}

	public void selectTile() throws IOException 
	{
		System.out.println("in selectTile\n");
		try {
			selected_tiles_list = player_arr[whose_turn].selectTileList(this.table);
		} catch (IOException e) {}
	}

	public void putTile() throws IOException 
	{
		System.out.println("in putTile\n");
		if (selected_tiles_list.size() != 0) {
			// selected_tiles_list를 바로 TileSet으로 만들어서 table에 올리기
			player_arr[whose_turn].putTileList(selected_tiles_list, table);
		}
	}

	boolean isFinish() {
		boolean ret = false;
		boolean determined_winner = false;
		if (player_arr[whose_turn].holder.size() == 0) {
			// 모든 타일을 다 내린 사람이 우승자, 나머지 사람들 점수 = 자신이 가진 타일의 숫자를 더한 숫자의 마이너스(-) 점수, 2등, 3등,
			// 4등의 숫자의 합 = 우승자에게는 플러스(+) 점수
			player_arr[whose_turn].is_winner = true;
			ret = true;
			determined_winner = true;
		} else if (this.pool.holder.size() == 0) {
			// 우승자 = 남은 타일 숫자합이 가장 적은 게임자, 우승자 점수 = 나머지 게임자의 타일숫자의 합 - 우승자의 점수
			ret = true;
			determined_winner = false;
		//} else if (this.pool.holder.size() == 74) {
			//ret = true;
		} else {
			super.whose_turn = (whose_turn + 1) % num_of_players;
		}
		if (ret) {
			setScores(determined_winner);
			PlayerDataBase.saveInfo(this.player_arr);
		}
		//for (Player player: player_arr) {
		//}
		return ret;
	}

	private void setScores(Boolean determined_winner) {
		// only when game is over, calculate scores of each player and save them in
		// scores
		Player winner = null;
		if (determined_winner) {
			winner = player_arr[whose_turn];
			// 모든 타일을 다 내린 사람이 우승자, 나머지 사람들 점수 = 자신이 가진 타일의 숫자를 더한 숫자의 마이너스(-) 점수, 2등, 3등,
			// 4등의 숫자의 합 = 우승자에게는 플러스(+) 점수
			for (Player player : player_arr) {
				player.setSum();
			}
			for (Player player : player_arr) {
				if (player_arr[whose_turn].getPlayerId() != player.getPlayerId()) {
					(player.point) -= player.getSum();
					(player_arr[whose_turn].point) += player.getSum();
				}
			}
		} else {
			// 우승자 = 남은 타일 숫자합이 가장 적은 게임자, 우승자 점수 = 나머지 게임자의 타일숫자의 합 - 우승자의 점수
			winner = player_arr[0];
			for (Player player : player_arr) {
				player.setSum();
			}
			for (int i = 0; i < player_arr.length; i++) {
				if (player_arr[i].getSum() < winner.getSum()) {
					winner = player_arr[i];
				}
			}
			for (Player player : player_arr) {
				if (winner.getPlayerId() != player.getPlayerId()) {
					(player.point) -= player.getSum();
					(winner.point) += player.getSum();
				}
			}
		}
		for (int i = 0; i < num_of_players; i++) {
			scores[i] = player_arr[i].point;
		}
		String msg_loser = "";
		String msg_winner = "";
		String msg = "@popup GAME IS OVER\n" + "<score board>\n";
		int winner_idx = winner.getPlayerId();
		msg += printScores();
		msg_winner = msg + "You are the winner !";
		msg_loser = msg + "Winner is player " + this.client_name_list.get(winner_idx) + " !" ;
		for (int i = 0; i < sst.sender_thread_list.size(); i++) {
			try {
				if (winner.getPlayerId() == i) {
					sst.sender_thread_list.get(i).output.writeUTF(msg_winner);
				} else {
					sst.sender_thread_list.get(i).output.writeUTF(msg_loser);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			sst.receiver_thread_list.get(i).flag = false;
			sst.sender_thread_list.get(i).flag = false;
		}
	}

	private String printScores() {
		// print the scores of players
		String ret = "";
		for (int i = 0; i < num_of_players; i++)
			ret += this.client_name_list.get(i) + ": " + Integer.toString(scores[i]) + "\n";
		return ret;
	}
}