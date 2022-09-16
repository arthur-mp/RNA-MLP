package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public List<DataBase> generateDataBase(String path) {
        createHashReligions();
        createHashColors();
        readDataBase(path);
        return this.dataBase;
    }

    public void generateDataBasesDistributed() {
        List<List<String>> basesDistributed = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            basesDistributed.add(new ArrayList<>());
        }

        FileReader file = null;

        try {
            file = new FileReader("./src/data/flags/flags.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e);
        }

        BufferedReader readFile = new BufferedReader(file);

        try {

            String[] datas;
            String data;

            for (int i = 0; i < this.instances; i++) {
                data = readFile.readLine();
                datas = data.split(",");

                int index = Integer.parseInt(datas[colNeuron]);
                List<String> value = basesDistributed.get(index);
                value.add(data);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        createDataBases(basesDistributed);
    }

    private void createDataBases(List<List<String>> basesDistributed) {
        List<String> base75 = new ArrayList<>();
        List<String> base25 = new ArrayList<>();

        int limit;
        double valueLimit;
        List<String> base;

        for (int i = 0; i < basesDistributed.size(); i++) {
            base = basesDistributed.get(i);

            valueLimit = (base.size() * 0.75);

            limit = (int) Math.floor(valueLimit);

            for (int j = 0; j < limit; j++) {
                base75.add(base.get(j));
            }
            for (int k = limit; k < base.size(); k++) {
                base25.add(base.get(k));
            }
        }

        createFile(base75, "base75");
        createFile(base25, "base25");
    }

    private void createFile(List<String> base, String name) {
        String path = "./src/data/flags/bases/" + name + ".txt";
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();

                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                for (int i = 0; i < base.size(); i++) {
                    bufferedWriter.write(base.get(i));
                    bufferedWriter.newLine();
                }

                bufferedWriter.close();
                fileWriter.close();
            }

        } catch (IOException erro) {
            System.out.printf("Erro: %s", erro.getMessage());
        }
    }

    private void readDataBase(String path) {
        FileReader file = null;

        try {
            if(path != null){
                file = new FileReader(path);
            }else{
                file = new FileReader("./src/data/flags/flags.txt");
            }
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
            file = new FileReader("./src/data/flags/religions.txt");
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
            file = new FileReader("./src/data/flags/colors.txt");
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
