package main;

import utils.DataBase;
import utils.GenerateDataBase;

public class MLPRunner {

    public static void main(String[] args) throws Exception {

        GenerateDataBase generateDataBase = new GenerateDataBase();

        int neuronsIntermediary = 4;
        double ni = 0.1;

        /*
         * Enter file name
         * Files are in 'Date'
         */
        DataBase[] dataBase = generateDataBase.generateDataBase("xor");

        MLP p = new MLP(dataBase[0].getX().length, dataBase[0].getY().length, neuronsIntermediary, ni);

        double erroEp = 0;
        double erroAm = 0;

        for (int e = 0; e < 10000; e++) {
            erroEp = 0;

            for (int a = 0; a < dataBase.length; a++) {
                Double[] x = dataBase[a].getX();
                Double[] y = dataBase[a].getY();
                double[] out = p.learn(x, y);

                for (int j = 0; j < out.length; j++) {
                    erroAm += Math.abs(y[j] - out[j]);
                }

                erroEp += erroAm;
                erroAm = 0;
            }

            System.out.println("Época: " + (e + 1) + " - erro: " + erroEp);
        }

        double[][] w = p.getWO();
        System.out.println("-----------------------------------------");
        for (int j = 0; j < w[0].length; j++) {
            System.out.printf("Neuronio: %d\n", j + 1);
            for (int i = 0; i < w.length; i++) {
                System.out.printf("Peso %d: %2f\n", i, w[i][j]);
            }
            System.out.println("-----------------------------------------");
        }
    }
}
