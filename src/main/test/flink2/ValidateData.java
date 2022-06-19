package flink2;

import java.io.*;

/**
 * 验证flink2期数据
 * @Auther wuzebin
 * @Date 2022/5/23 14:54
 */
public class ValidateData {

    public static void main(String[] args) throws IOException {

        File file = new File("C:\\Users\\94695\\Desktop\\flink2_202205261210.csv");
        Reader reader = null;
        int lineNum = 0;

        try {

            int sum = 0;
            reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line = bufferedReader.readLine();
            while (line != null) {

                String[] resultArr = line.split(",");

                for (String s : resultArr) {
                    sum += Integer.valueOf(s);
                }

                line = bufferedReader.readLine();
            }

            System.out.println(sum);

        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println(lineNum);
        }
        finally {reader.close();}

    }

}
