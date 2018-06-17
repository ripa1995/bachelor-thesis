import java.util.ArrayList;

public class EncryptedOutput {
    private String varName;
    private ArrayList<String> encValue;

    public EncryptedOutput() {
        this.varName = "";
        this.encValue = new ArrayList<String>();
    }

    public EncryptedOutput(String varName, ArrayList<String> encValue) {
        this.varName = varName;
        this.encValue = encValue;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public ArrayList<String> getEncValue() {
        return encValue;
    }

    public void setEncValue(ArrayList<String> encValue) {
        this.encValue = encValue;
    }

    public void addEncValue(String _enc) {
        if (!encValue.contains(_enc)) {
            encValue.add(_enc);
        }
    }
}
