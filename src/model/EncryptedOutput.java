package model;

import java.util.ArrayList;

public class EncryptedOutput {
    private String varName;
    private ArrayList<EncValue> encValue;

    public EncryptedOutput() {
        this.varName = "";
        this.encValue = new ArrayList<EncValue>();
    }

    public EncryptedOutput(String varName, ArrayList<EncValue> encValue) {
        this.varName = varName;
        this.encValue = encValue;
    }

    public EncryptedOutput(String varName) {
        this.varName = varName;
        this.encValue = new ArrayList<EncValue>();
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public ArrayList<EncValue> getEncValue() {
        return encValue;
    }

    public void setEncValue(ArrayList<EncValue> encValue) {
        this.encValue = encValue;
    }

    public void addEncValue(EncValue _enc) {
        if (!encValue.contains(_enc)) {
            encValue.add(_enc);
        }
    }
}
