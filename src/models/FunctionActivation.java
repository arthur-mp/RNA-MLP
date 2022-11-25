package models;

public class FunctionActivation {

    public static Double sigmoidal(Double u) {
        return (1 / (1 + (Math.exp(-u))));
    }

    public static Double normalization(Double value, Double max, Double min){
        return (value - min)/(max - min);
    }

}