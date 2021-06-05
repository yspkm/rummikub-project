public class Tile extends GameObject
{
	private int color; // 1234
	private int num;
	private int duo; // 1 or 2, since each color-num has two tiles
	private String serial; // e.g.
	private String last_pos; // table or player
	private final int BLACK = 1, RED = 2, YELLOW = 3, BLUE = 4;

	Tile(int i_duo, int j_color, int k_num) 
	{
		this.duo = i_duo;
		this.color = j_color;
		this.num = k_num;
		this.serial = getSerial();
	}

	public String getTileString() 
	{
		String ret = "";
		String num_str;

		num_str = String.format("%d", this.getNum());
		switch (this.getColor()) {
			case BLACK:
				ret += "Black[" + num_str + "]";
				break;
			case RED:
				ret += "Red[" + num_str + "]";
				break;
			case YELLOW:
				ret += "Yellow[" + num_str + "]";
				break;
			case BLUE:
				ret += "Blue[" + num_str + "]";
				break;
		}
		return ret;
	}

	int getColor() 
	{
		return this.color;
	}

	int getNum() 
	{
		return this.num;
	}

	int getDuo() 
	{
		return this.duo;
	}

	String getLastPos() 
	{
		return this.last_pos;
	}

	void setLastPos(String pos) 
	{
		this.last_pos = pos;
	}

	String getSerial() 
	{
		this.serial = "";
		this.serial += String.valueOf(this.duo);
		this.serial += String.valueOf(this.color);
		this.serial += String.valueOf(this.num);
		return this.serial;
	}
}
