import java.util.ArrayList;

public class Header {

    private String varName;
    private ArrayList<String> enc;
    public Header(){
        varName = "";
        enc = new ArrayList<String>();
    }
    public Header(String name){
        varName = name;
        enc = new ArrayList<String>();
    }
    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public ArrayList<String> getEnc() {
        return enc;
    }

    public void setEnc(ArrayList<String> enc) {
        this.enc = enc;
    }

    public void addEnc(String _enc) {
        if (!enc.contains(_enc)) {
            enc.add(_enc);
        }
    }

}
