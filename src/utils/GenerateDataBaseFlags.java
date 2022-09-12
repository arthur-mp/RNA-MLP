package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateDataBaseFlags {
    private List<DataBase> dataBase;
    private Map<String, Double[]> hashReligions;
    private Map<String, Double> hashColors;
    private final int lengthY = 8;
    private final int instances = 194;
    private final int colNeuron = 6;

    public GenerateDataBaseFlags() {
        this.dataBase = new ArrayList<>();
    }

    public DataBase[] generateDataBase() {
        createHashReligions();
        createHashColors();
        readDataBase();
        return this.dataBase.toArray(new DataBase[this.dataBase.size()]);
    }

    private void readDataBase() {
        FileReader file = null;

        try {
            file = new FileReader("./src/data/flags.bases/flags.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e);
        }

        BufferedReader readFile = new BufferedReader(file);

        try {

            String[] datas;
            List<Double> in;
            Double[] out;
            DataBase newDataBase;

            for (int i = 0; i < this.instances; i++) {
                datas = readFile.readLine().split(",");

                in = new ArrayList<>();
                out = new Double[this.lengthY];

                for (int j = 1; j < datas.length; j++) {

                    if (j == this.colNeuron) {
                        out = (this.hashReligions.get(datas[j]));
                    } else {

                        if (this.hashColors.containsKey(datas[j])) {
                            in.add(this.hashColors.get(datas[j]));
                        } else {
                            in.add(Double.parseDouble(datas[j]));
                        }
                    }
                }

                newDataBase = new DataBase(
                        in.toArray(new Double[in.size()]),
                        out);

                this.dataBase.add(newDataBase);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private void createHashReligions() {
        this.hashReligions = new HashMap<>();

        FileReader file = null;

        try {
            file = new FileReader("./src/data/flags.bases/religions.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e);
        }

        BufferedReader readFile = new BufferedReader(file);

        try {
            String description = readFile.readLine();
            int numberLine = Integer.parseInt(description);

            String[] datas;
            String key;
            String[] valueStrings;
            Double[] values;

            for (int i = 0; i < numberLine; i++) {
                datas = readFile.readLine().split(":");

                key = datas[0];

                valueStrings = new String[this.lengthY];
                valueStrings = datas[1].split(" ");

                values = new Double[this.lengthY];

                for (int j = 0; j < this.lengthY; j++) {
                    values[j] = Double.parseDouble(valueStrings[j]);
                }

                this.hashReligions.put(key, values);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private void createHashColors() {
        this.hashColors = new HashMap<>();

        FileReader file = null;

        try {
            file = new FileReader("./src/data/flags.bases/colors.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e);
        }

        BufferedReader readFile = new BufferedReader(file);

        try {
            String description = readFile.readLine();
            int numberLine = Integer.parseInt(description);

            String[] datas;
            String key;
            Double value;

            for (int i = 0; i < numberLine; i++) {
                datas = readFile.readLine().split(":");

                key = datas[0];
                value = Double.parseDouble(datas[1]);

                this.hashColors.put(key, value);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
