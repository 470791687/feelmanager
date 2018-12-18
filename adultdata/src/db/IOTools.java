package db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IOTools {
	public static void main(String[] args) {
		File file = new File("/Users/shengsheng/work/mypaper_code/temp/cluster/cluster_train.txt");
		System.out.println("begin");
		double time = System.currentTimeMillis();
		writeTools(file);
		System.out.println("end,时间：" + (System.currentTimeMillis() - time));
		// a- z : 97 -122

	}

	public static void writeTools(File file) {

		FileWriter fw = null;
		BufferedWriter bw = null;
		try {

			// patients = new HashMap<>();

			// fr = new FileReader(file);
			// br = new BufferedReader(fr);

			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			char[] arr = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
					's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'k', 'L',
					'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

			List<String[]> list = new ArrayList<>();
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 43; j++) {
					String[] strings = new String[3 + i];
					for (int k = 0; k < strings.length; k++) {
						Random random = new Random();
						int index = random.nextInt(arr.length);
						strings[k] = arr[index] + "";
						// System.out.println(strings[i]);
					}
					list.add(strings);
				}
			}
			for (int i = 0; i < list.size(); i++) {
				String[] str = list.get(i);
				String string = "";
				for (int j = 0; j < str.length; j++) {
					if (j == str.length - 1) {
						string += str[j];
					} else {
						string += str[j] + ",";
					}
				}
				bw.write("t" + i + ":<" + string + ">\n");
				fw.flush();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block

		} finally {

			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}
}
