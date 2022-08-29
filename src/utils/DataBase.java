package utils;

public class DataBase {
    private Double[] x;
    private Double[] y;

    public DataBase() {

    }

    public DataBase(int x, int y) {
        this.x = new Double[x];
        this.y = new Double[y];
    }

    public DataBase(Double[] x, Double[] y) {
        this.x = x;
        this.y = y;
    }

    public Double[] getX() {
        return x;
    }

    public Double[] getY() {
        return y;
    }
}
