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

        int neuronsIntermediary = 16;
        double ni = 0.001;

        MLP p = new MLP(dataBaseTreino.get(0).getX().length, dataBaseTreino.get(0).getY().length, neuronsIntermediary, ni);

        /*
         * Abreviações:
         * 
         * Ep: Epoca
         * Am: Amostra
         * Cl: Classificação
         */
        double erroEpTreino = 0;
        double erroAmTreino = 0;

        double erroEpTeste = 0;
        double erroAmTeste = 0;

        double erroClAmTreino = 0;
        double erroClEpTreino = 0;

        double erroClAmTeste = 0;
        double erroClEpTeste = 0;

        List<String> dataTreino = new ArrayList<>();
        List<String> dataTeste = new ArrayList<>();

        List<String> dataClTreino = new ArrayList<>();
        List<String> dataClTeste = new ArrayList<>();

        for (int e = 0; e < 10000; e++) {
            erroEpTreino = 0;
            erroEpTeste = 0;

            erroClEpTreino = 0;
            erroClEpTeste = 0;

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

                erroClAmTreino = erroCl(y, out);
                erroClEpTreino += erroClAmTreino;
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

                erroClAmTeste = erroCl(y, out);
                erroClEpTeste += erroClAmTeste;
            }

            System.out.println(e +"\t"+"ErroEpTreino: "+ erroEpTreino +"\t"+"ErroClEpTreino: "+ erroClEpTreino+"\t"+"ErroEpTeste: "+ erroEpTeste+"\t"+"ErroClEpTeste: "+ erroClEpTeste);

            dataTreino.add("" + (e + 1) + " "+erroEpTreino + "");
            dataTeste.add("" + (e + 1) + " "+erroEpTeste + "");

            erroClEpTreino = (erroClEpTreino / dataBaseTreino.size());
            erroClEpTeste = (erroClEpTeste / dataBaseTeste.size());

            dataClTreino.add("" + (e + 1) + " " + erroClEpTreino + "");
            dataClTeste.add("" + (e + 1) + " " + erroClEpTeste + "");
        }

        FunctionsFile.createFile("./src/data/flags/bases/erroAproximacaoTreino.dat", dataTreino);
        FunctionsFile.createFile("./src/data/flags/bases/erroAproximacaoTeste.dat", dataTeste);

        FunctionsFile.createFile("./src/data/flags/bases/erroClassificacaoTreino.dat", dataClTreino);
        FunctionsFile.createFile("./src/data/flags/bases/erroClassificacaoTeste.dat", dataClTeste);
    }

    public static int erroCl(Double[] y, double[] out){

        double[] outThreshold = new double[out.length];

        double threshold = 0.5;
        
        for (int i = 0; i < out.length; i++) {
            if(out[i] >= threshold){
                outThreshold[i] = 1;
            }else{
                outThreshold[i] = 0;
            }
        }

        double sumOutY = 0;

        for (int j = 0; j < out.length; j++) {
            sumOutY += Math.abs(outThreshold[j] - y[j]);
        }

        if(sumOutY > 0) return 1;
        else return 0;
    }

    public static int erroCl2(Double[] y, double[] out){

        double[] outThreshold = new double[out.length];

        int threshold = 0;
        double valueThreshold = outThreshold[0];
        
        for (int i = 1; i < outThreshold.length; i++) {
            if(outThreshold[i] > valueThreshold){
                threshold = i;
            }
        }

        for (int i = 0; i < outThreshold.length; i++) {
            if(i == threshold){
                outThreshold[i] = 1.0;
            }else{
                outThreshold[i] = 0.0;
            }
        }

        double sumOutY = 0;

        for (int j = 0; j < out.length; j++) {
            sumOutY += Math.abs(outThreshold[j] - y[j]);
        }

        if(sumOutY > 0) return 1;
        else return 0;
    }
}
