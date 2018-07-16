package model;

public class Line {
    private int loc;
    private String code;

    public Line(int loc, String code) {
        this.loc = loc;
        this.code = code;
    }

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
