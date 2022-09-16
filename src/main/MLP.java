package main;

import java.util.Random;

import models.FunctionActivation;

public class MLP {

    private double ni;
    private int amountInput;
    // O
    private int amountOutput;
    // H
    private int amountNeuronsIntermediary;
    private double[][] wO;
    private double[][] wH;

    private double[] x;
    private double[] H;

    private final static double Range_Max = 0.03;
    private final static double Range_Min = -0.03;

    public MLP(int amountInput, int amountOutput, int amountNeuronsIntermediary, double ni) {
        this.ni = ni;
        this.amountInput = amountInput;
        this.amountOutput = amountOutput;
        this.amountNeuronsIntermediary = amountNeuronsIntermediary;
        wH = new double[amountInput + 1][amountNeuronsIntermediary];
        wO = new double[amountNeuronsIntermediary + 1][amountOutput];
        generateRandoWO();
        generateRandoWH();
    }

    private void generateRandoWO() {
        Random random = new Random();
        for (int i = 0; i < amountNeuronsIntermediary + 1; i++) {
            for (int j = 0; j < amountOutput; j++) {
                this.wO[i][j] = random.nextDouble() * Range_Max * 2 + Range_Min;
            }
        }

    }

    private void generateRandoWH() {
        Random random = new Random();
        for (int i = 0; i < amountInput + 1; i++) {
            for (int j = 0; j < amountNeuronsIntermediary; j++) {
                this.wH[i][j] = random.nextDouble() * Range_Max * 2 + Range_Min;
            }
        }

    }

    public double[] learn(Double[] xIn, Double[] y) {
        
        double[] out = execute(xIn);

        /*
         * Calcula os deltas
         */
        double[] deltaOut = new double[amountOutput];

        for(int j = 0; j < amountOutput; j++){
            deltaOut[j] = (out[j] * (1 - out[j]) * (y[j] - out[j]));
        }

        double[] deltaH = new double[amountNeuronsIntermediary];

        for(int h = 0; h < amountNeuronsIntermediary; h++){
            double sum = 0;

            for(int j = 0; j < amountOutput; j++){
                sum += deltaOut[j] * wO[h][j];
            }

            deltaH[h] = H[h] * ( 1 - H[h]) * sum;
        }

        /*
         * Ajuste dos pesos da camada intermediária
         */
        for(int i = 0; i < amountInput; i++){
            for(int h = 0; h < amountNeuronsIntermediary; h++){
                wH[i][h] += (ni * deltaH[h] * x[i]);
            }
        }


        /*
         * Ajuste dos pesos da saída
         */
        for(int h = 0; h < amountNeuronsIntermediary; h++){
            for(int o = 0; o < amountOutput; o++){
                wO[h][o] += (ni * deltaOut[o] * H[h]);
            }
        }

        return out;
    }


    public double[] execute(Double[] xIn){
        /*
         * Copia do xIn para o x
         */
        x = new double[xIn.length + 1];

        for (int i = 0; i < xIn.length; i++) {
            x[i] = xIn[i];
        }
        x[x.length - 1] = 1;

        /*
         * Calcula a saída da camada intermediária
         * Representa a saída da camada intermediária
         */
        H = new double[amountNeuronsIntermediary + 1];

        for (int j = 0; j < amountNeuronsIntermediary; j++) {
            for (int i = 0; i < x.length; i++) {
                H[j] += (x[i] * wH[i][j]);
            }

            H[j] = FunctionActivation.sigmoidal(H[j]);
        }

        H[H.length - 1] = 1;


        /*
         * Calcula a saída obtida
         */
        double[] out = new double[amountOutput];

        for (int j = 0; j < out.length; j++) {
            for (int i = 0; i < H.length; i++) {
                out[j] += H[i] * wO[i][j];
            }

            out[j] = FunctionActivation.sigmoidal(out[j]);
        }

        return out;
    }

    public double[][] getWO() {
        return wO;
    }
}
