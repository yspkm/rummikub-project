import java.util.*;

public class TileHolder extends GameObject
{
	HashMap<String, Tile> holder = new HashMap<String, Tile>();
	Set<String> holder_key_set;
	TileSort tile_sort = new TileSort();

	public ArrayList<Tile> getTileList()
	{
		return tile_sort.getTileList(this.holder);
	}
	public ArrayList<Tile> getTileListgrouped(ArrayList<Tile> tile_list)
	{
		return tile_sort.getTileListgrouped(tile_list);
	}
	public ArrayList<Tile> getTileListRun(ArrayList<Tile> tile_list)
	{ // set tile_arr sorted by number;
		return tile_sort.getTileListRun(tile_list);
	}
}