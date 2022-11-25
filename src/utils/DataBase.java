package utils;

public class DataBase {
    private Double[] x;
    private Double[] y;
    private String id;

    public DataBase() {

    }

    public DataBase(Double[] x, Double[] y) {
        this.x = x;
        this.y = y;
    }

    public DataBase(Double[] x, Double[] y, String id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public Double[] getX() {
        return x;
    }

    public Double[] getY() {
        return y;
    }

    public String getId(){
        return id;
    }

    public void setX(Double[] newX){
        this.x = newX;
    }

    public void setXIndex(int index, Double value){
        this.x[index] = value;
    }
}
