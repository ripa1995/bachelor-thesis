package model;

public class Item{
    private int loc;
    private String T;

    public Item(){}

    public Item(int _loc, String _T) {
        loc = _loc;
        T = _T;
    }

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public String getT() {
        return T;
    }

    public void setT(String t) {
        T = t;
    }
}