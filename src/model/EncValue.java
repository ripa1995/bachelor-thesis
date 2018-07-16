package model;

public class EncValue {
    private String varName;
    private String key;
    public EncValue(String varName, String key) {
        this.varName = varName;
        this.key = key;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "("+this.getVarName()+","+this.getKey()+")";
    }
}
