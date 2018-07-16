package model;

public class Constant {
    private int loc;
    private int value;

    public Constant() {
    }

    public Constant(int loc, int value) {
        this.loc = loc;
        this.value = value;
    }

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
