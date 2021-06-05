import java.util.*;

public class Pool extends TileHolder
{
	ServerSocketThread sst;

	Pool(ServerSocketThread sst)
	{
		this.sst = sst;
	}

	Tile randTakeOutTile()
	{
		Set<String> key_set = holder.keySet();
		String[] key_arr = new String[key_set.size()];
		key_set.toArray(key_arr);
		String rand_key = key_arr[((int) (Math.random() * 100000)) % key_arr.length];
		Tile ret = holder.get(rand_key);
		holder.remove(rand_key);
		return ret;
	}

	public void printPoolTileNum()
	{
		int PoolTileNum = holder.size();
		sst.sendToAll("Pool Num : "+PoolTileNum);
		printPool();
	}

	private void printPool()
	{
		System.out.println("Pool : ");
		for (String key : holder.keySet()) {
			System.out.print(holder.get(key).getTileString() + " ");
		}
		System.out.println();
	}
}