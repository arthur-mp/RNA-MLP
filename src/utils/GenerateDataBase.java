package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class GenerateDataBase {
    private List<DataBase> dataBase;

    public GenerateDataBase() {
        this.dataBase = new ArrayList<>();
    }

    public DataBase[] generateDataBase(String data) {
        readDataBase(data);
        return this.dataBase.toArray(new DataBase[this.dataBase.size()]);
    }

    private void readDataBase(String data) {
        FileReader file = null;

        try {
            file = new FileReader("./src/data/" + data + ".txt");
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e);
        }

        BufferedReader readFile = new BufferedReader(file);

        try {
            String[] descriptions = readFile.readLine().split(" ");

            int x = Integer.parseInt(descriptions[0]);
            int dataLength = Integer.parseInt(descriptions[2]);

            String[] datas;
            List<Double> in;
            List<Double> out;
            DataBase newDataBase;

            for (int i = 0; i < dataLength; i++) {
                datas = readFile.readLine().split(" ");

                in = new ArrayList<>();
                out = new ArrayList<>();

                for (int j = 0; j < datas.length; j++) {
                    if (j < x) {
                        in.add(Double.parseDouble(datas[j]));
                    } else {
                        out.add(Double.parseDouble(datas[j]));
                    }
                }

                newDataBase = new DataBase(
                        in.toArray(new Double[in.size()]),
                        out.toArray(new Double[out.size()]));

                this.dataBase.add(newDataBase);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

}
