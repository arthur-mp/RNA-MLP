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
import java.util.Collections;
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
    private final boolean balancing;

    public GenerateDataBaseFlags(boolean balancing) {
        this.dataBase = new ArrayList<>();
        this.balancing = balancing;
    }

    public List<DataBase> generateDataBase(String path) {
        createHashReligions();
        createHashColors();
        List<DataBase> listDataBase = readDataBase(path);
        if (listDataBase != null) {
            return listDataBase;
        }
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
                basesDistributed.get(index).add(data);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        createDataBases(basesDistributed);
    }

    private void createDataBases(List<List<String>> basesDistributed) {
        List<String> base75 = new ArrayList<>();
        List<String> base25 = new ArrayList<>();

        List<List<String>> baseDistributed75 = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            baseDistributed75.add(new ArrayList<>());
        }

        int limit;
        double valueLimit;
        List<String> base;

        for (int i = 0; i < basesDistributed.size(); i++) {
            base = basesDistributed.get(i);

            valueLimit = (base.size() * 0.75);

            limit = (int) Math.floor(valueLimit);

            if (this.balancing) {
                for (int k = 0; k < limit; k++) {
                    baseDistributed75.get(i).add(base.get(k));
                }
            } else {
                for (int j = 0; j < limit; j++) {
                    base75.add(base.get(j));
                }
            }

            for (int k = limit; k < base.size(); k++) {
                base25.add(base.get(k));
            }
        }

        if (this.balancing) {
            base75 = generateBalancing(baseDistributed75);
        }

        Collections.shuffle(base25);
        Collections.shuffle(base75);
        createFile(base75, "base75");
        createFile(base25, "base25");
    }

    // Gerar base 75 Balanceada
    private List<String> generateBalancing(List<List<String>> baseDistributed75) {
        List<String> base75 = new ArrayList<>();

        // Identificar qual a categoria que possui mais amostra
        int biggerCategory = 0;

        for (int i = 0; i < baseDistributed75.size(); i++) {
            if (baseDistributed75.get(i).size() > baseDistributed75.get(biggerCategory).size()) {
                biggerCategory = i;
            }
        }

        // Balancear a Base
        for (int i = 0; i < baseDistributed75.size(); i++) {
            if (i != biggerCategory) {
                int index = 0;
                final int indexFinal = baseDistributed75.get(i).size();
                for (int j = baseDistributed75.get(i).size(); j < baseDistributed75.get(biggerCategory).size(); j++) {
                    if(index < indexFinal){
                        baseDistributed75.get(i).add(baseDistributed75.get(i).get(index));
                        index++;
                    }else{
                        index = 0;
                        baseDistributed75.get(i).add(baseDistributed75.get(i).get(index));
                        index++;
                    }
                }
            }
        }

        for(int i = 0; i < baseDistributed75.size(); i++){
            for (int j = 0; j < baseDistributed75.get(i).size(); j++) {
                base75.add(baseDistributed75.get(i).get(j));
            }
        }

        return base75;
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

    private List<DataBase> readDataBase(String path) {
        FileReader file = null;
        List<DataBase> newListDataBases = new ArrayList<>();

        try {
            if (path != null) {
                file = new FileReader(path);
            } else {
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

            while (readFile.ready()) {
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

                newListDataBases.add(newDataBase);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        if (path != null) {
            return newListDataBases;
        } else {
            this.dataBase = newListDataBases;
            return null;
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
