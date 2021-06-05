import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class PlayerDataBase {
    static FileOutputStream fos;
    static OutputStreamWriter osw;
    static BufferedWriter bw;
    static FileInputStream fis;
    static InputStreamReader isr;
    static BufferedReader br;
    static ArrayList<ArrayList<String>> list;

    static void saveInfo(Player[] players) {
        try {
            fis = new FileInputStream("PlayerDataBase.txt");
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            list = new ArrayList<ArrayList<String>>(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line = "";
        ArrayList<String> templist;
        ArrayList<String> user_in_db = new ArrayList<String>(0);
        try {
            while ((line = br.readLine()) != null) {
                templist = new ArrayList<String>(0);
                String[] arr_from_line = line.split(" ");
                user_in_db.add(arr_from_line[0]);
                for (String s : arr_from_line) {
                    templist.add(s);
                }
                list.add(templist);
            }
            for (Player p : players) {
                if (user_in_db.contains(p.name)) { // 디비에 정보가 있으면, 뒤에 어펜드
                    int idx = user_in_db.indexOf(p.name);
                    String point = String.valueOf(p.getPoint());
                    list.get(idx).add(point);
                } else {
                    ArrayList<String> temp = new ArrayList<String>(0);
                    String point = String.valueOf(p.getPoint());
                    temp.add(p.name);
                    temp.add(point);
                    list.add(temp);
                }
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos = new FileOutputStream("PlayerDataBase.txt");
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);
            for (ArrayList<String> line_list : list) {
                String line_str = "";
                int i;
                for (i = 0; i < line_list.size(); i++) {
                    line_str += line_list.get(i);
                    line_str += " ";
                }
                line_str.trim();
                line_str += "\n";
                bw.write(line_str);
                bw.flush();
            }
            bw.close();
            osw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}