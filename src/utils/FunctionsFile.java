package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FunctionsFile {

    public static void createFile(String path, List<String> data) {
        File file = new File(path);
        try {
            if(!file.exists()){
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (int i = 0; i < data.size(); i++) {
                bufferedWriter.write(data.get(i));
                bufferedWriter.newLine();
            }
            

            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException erro) {
            System.out.printf("Erro: %s", erro.getMessage());
        }
    }

    public static void readFile(String path) {
        File file = new File(path);
        try {
            file.createNewFile();

            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // enquanto houver mais linhas
            while (bufferedReader.ready()) {
                // lê a proxima linha
                String linha = bufferedReader.readLine();
                // faz algo com a linha
            }

            bufferedReader.close();
            fileReader.close();
        } catch (IOException  erro) {
            System.out.printf("Erro: %s", erro.getMessage());
        }
    }
}
