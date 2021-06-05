import java.util.*;

public class Table extends TileHolder implements Cloneable
{
    ArrayList<TileSet> table_set = new ArrayList<TileSet>(0);
    ServerSocketThread sst;

    Table(ServerSocketThread sst)
    {
        this.sst = sst;
    }

    public void printTable()
    {
        for (TileSet ts : table_set) { // 0개인 set 제거
            if (ts.tile_set.size() == 0)
                table_set.remove(ts);
        }
        if (table_set.size() == 0) {
            sst.sendToAll("Table Empty table\n");
        }
        else {
            String set_info_str = "Table ";
            for (int i = 0; i < table_set.size(); i++) {
                set_info_str += String.format("TileSet[%d]: ", i);
                set_info_str += table_set.get(i).printTileSet();
            }
            sst.sendToAll(set_info_str);
        }
    }
    // set 객체 선택
    // group or run 판단
    public void setTileSet(int index, ArrayList<Tile> input_list)
    {
        TileSet tile_set;
        if (index == -1) {
            tile_set = initTileSet();
            tile_set.insertTile(input_list);
            this.table_set.add(tile_set);
        } else {
            tile_set = getTileSet(index);
            tile_set.insertTile(input_list);
            //this.table_set.add(tile_set);
        }
    }

    private TileSet initTileSet()
    {
        TileSet ret = new TileSet('i');
        return ret;
    }

    private TileSet getTileSet(int index)
    {
        return this.table_set.get(index);
    }

    public boolean isRightTable()
    {
        for (TileSet ts : table_set) // 0개인 set 제거
            if (ts.tile_set.size() == 0) {
                table_set.remove(ts);
            }
        if (this.table_set.size() == 0) // 테이블 위의 셋이 0개 인 경우
            return true;
        TileSort checker = new TileSort();
        boolean ret = false; // 혹시몰라서 넣어둠
        for (TileSet ts : this.table_set) {
            if (checker.isGroup(ts.tile_list)) {
                ts.set_type = 'g';
                ret = true;
            } else if (checker.isRun(ts.tile_list)) {
                ts.set_type = 'r';
                ret = true;
            } else {
                return false;
            }
        }
        return ret;
    }

    @Override
    public Table clone()
    {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Table ret = (Table) obj;
        ArrayList<TileSet> temp_table_set = new ArrayList<TileSet>(0);
        for (int i = 0; i < ret.table_set.size(); i++) {
            TileSet old = ret.table_set.get(i);
            TileSet temp = new TileSet(old.set_type);
            for (int j = 0; j < old.tile_list.size(); j++) {
                Tile temp_tile = old.tile_list.get(j);
                temp.insertTile(temp_tile);
            }
            temp_table_set.add(temp);
        }
        ret.table_set = temp_table_set;
        return ret;
    }
}
