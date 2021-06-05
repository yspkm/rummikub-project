import java.util.*;

public class TileSet
{
    ArrayList<Tile> tile_list;
    HashMap<String, Tile> tile_set;
    TileSort tile_sort = new TileSort();
    char set_type; // type = 'r' or 'g'

    TileSet(char type)
    {
        this.set_type = type;
        this.tile_list = new ArrayList<Tile>(0);
        this.tile_set = new HashMap<String, Tile>();
    }

    public ArrayList<Tile> getList()
    {
        return this.tile_list;
    }

    public void insertTile(Tile tile)
    {
        this.tile_list.add(tile);
        this.tile_set.put(tile.getSerial(), tile);
    }

    public void insertTile(ArrayList<Tile> list)
    {
        for (int i = 0; i < list.size(); i++) {
            this.tile_list.add(list.get(i));
            this.tile_set.put(list.get(i).getSerial(), list.get(i));
        }
    }

    public Tile extractTile(String serial)
    {
        ArrayList<Tile> list = this.tile_list;
        Tile ret;
        if (this.tile_set.containsKey(serial)) {
            ret = this.tile_set.get(serial);
            this.tile_set.remove(serial);
            list.remove(ret);
        } else
            ret = null; // fali to remove;
        return ret;
    }

    public String printTileSet()
    {
        ArrayList<Tile> list = this.tile_list;
        String ret = "";
        if (list.size() == 0) {
            ret += "\n";
            return ret;
        }
        int len = list.size();
        for (int i = 0; i < len; i++)
            ret += String.format("%2s ", list.get(i).getTileString());
        ret += "\n";
        return ret;
    }
}
