package main;

import utils.DataBase;
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

        DataBase[] dataBase = generateDataBase.generateDataBase();


        int neuronsIntermediary = 6;
        double ni = 0.3;

       
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

    }
}
