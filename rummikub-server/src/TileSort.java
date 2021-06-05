import java.util.ArrayList;
//import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class TileSort 
{
	final int COLOR = 0, NUMBER = 1;
	int[] count = new int[13]; // for countSort method
	boolean[] marker = new boolean[4]; //for isGroup method

	public boolean isRun(ArrayList<Tile> tile_list) 
	{
		if (tile_list.size() < 3)
			return false;
		int color = tile_list.get(0).getColor();
		for (Tile t : tile_list) {
			if (t.getColor() != color) {
				return false;
			}
		}
		countSort(tile_list, NUMBER);
		for (int i = 0; i < tile_list.size() - 1; i++)
			if ((tile_list.get(i + 1).getNum() - tile_list.get(i).getNum()) != 1) {
				return false;
			}
		return true;
	}

	public boolean isGroup(ArrayList<Tile> tile_list) 
	{
		if (!(tile_list.size() == 3 || tile_list.size() == 4))
			return false;
		int number = tile_list.get(0).getNum();
		boolean[] marker = new boolean[4];
		for (int i = 0; i < 4; i++) marker[i] = false;
		for (Tile t : tile_list) {
			if (marker[t.getColor()-1]) {
				return false;
			} else {
				marker[t.getColor()-1] = true;
			}
			if (t.getNum() != number) {
				return false;
			}
		}
		return true;
	}

	public ArrayList<Tile> getTileList(HashMap<String, Tile> holder) 
	{
		ArrayList<Tile> ret = new ArrayList<Tile>(holder.size());
		Set<String> key_set = holder.keySet();
		String[] key_arr = new String[key_set.size()];
		key_set.toArray(key_arr);
		for (int i = 0; i < key_arr.length; i++) {
			ret.add(holder.get(key_arr[i]));
		}
		return ret;
	}

	public ArrayList<Tile> getTileListgrouped(ArrayList<Tile> tile_list) 
	{
		this.countSort(tile_list, NUMBER);
		return tile_list;
	}

	public ArrayList<Tile> getTileListRun(ArrayList<Tile> tile_list) { // set tile_arr sorted by number;
		this.countSort(tile_list, NUMBER);
		this.countSort(tile_list, COLOR);
		return tile_list;
	}

	private void countSort(ArrayList<Tile> list, int type) 
	{
		int i;
		int len = list.size();
		Tile[] temp_arr = new Tile[len];
		int countlen = type == NUMBER ? 13 : 4;  
		int[] count = this.count;
		for (i = 0; i < countlen; i++) 
			count[i] = 0;
		for (i = 0; i < len ; i++) {
			int counter = this.getCounter(list.get(i), type); 
			count[counter]++; 
		}
		for (i=0; i<countlen-1; i++) 
			count[i+1] += count[i];
		for (i = len-1; i >= 0; i--) {
			int counter = this.getCounter(list.get(i), type); 
			temp_arr[count[counter]-1] = list.get(i);
			count[counter]--;
		}
		list.clear();
		for (i = 0; i <len; i++)
			list.add(temp_arr[i]);
	}
	private int getCounter(Tile tile, int type) {
		int ret = 0; 
		if (type == COLOR) 
			ret = tile.getColor()-1;
		else 
			ret = tile.getNum()-1;
		return ret;
	}
}
