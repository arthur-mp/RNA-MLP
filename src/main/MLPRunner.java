package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utils.DataBase;
import utils.FunctionsFile;
import utils.GenerateDataBase;
import utils.GenerateDataBaseFlags;

public class MLPRunner {

    public static void main(String[] args) throws Exception {

        /*
         *
         * Default Bases
         */

        // GenerateDataBase generateDataBase = new GenerateDataBase();

        // DataBase[] dataBase = generateDataBase.generateDataBase("xor");

        /*
         * 
         * Flags Bases
         */

        GenerateDataBaseFlags generateDataBase = new GenerateDataBaseFlags();

        // generateDataBase.generateDataBasesDistributed();

        // DataBase[] dataBase = generateDataBase.generateDataBase(null);

        List<DataBase> dataBaseTreino = generateDataBase.generateDataBase("./src/data/flags/bases/base75.txt");
        List<DataBase> dataBaseTeste = generateDataBase.generateDataBase("./src/data/flags/bases/base25.txt");

        int neuronsIntermediary = 6;
        double ni = 0.3;

        MLP p = new MLP(dataBaseTreino.get(0).getX().length, dataBaseTreino.get(0).getY().length, neuronsIntermediary, ni);

        double erroEpTreino = 0;
        double erroAmTreino = 0;

        double erroEpTeste = 0;
        double erroAmTeste = 0;

        List<String> dataTreino = new ArrayList<>();
        List<String> dataTeste = new ArrayList<>();

        for (int e = 0; e < 10000; e++) {
            erroEpTreino = 0;
            erroEpTeste = 0;

            // Treino
            for (int a = 0; a < dataBaseTreino.size(); a++) {
                Double[] x = dataBaseTreino.get(a).getX();
                Double[] y = dataBaseTreino.get(a).getY();
                double[] out = p.learn(x, y);

                for (int j = 0; j < out.length; j++) {
                    erroAmTreino += Math.abs(y[j] - out[j]);
                }

                erroEpTreino += erroAmTreino;
                erroAmTreino = 0;
            }

            // Teste
            for (int a = 0; a < dataBaseTeste.size(); a++) {
                Double[] x = dataBaseTeste.get(a).getX();
                Double[] y = dataBaseTeste.get(a).getY();
                double[] out = p.execute(x);

                for (int j = 0; j < out.length; j++) {
                    erroAmTeste += Math.abs(y[j] - out[j]);
                }

                erroEpTeste += erroAmTeste;
                erroAmTeste = 0;
            }

            dataTreino.add("" + (e + 1) + " "+erroEpTreino + "");
            dataTeste.add("" + (e + 1) + " "+erroEpTeste + "");
        }

        FunctionsFile.createFile("./src/data/flags/bases/erroAproximacaoTreino.dat", dataTreino);
        FunctionsFile.createFile("./src/data/flags/bases/erroAproximacaoTeste.dat", dataTeste);
    }
}
