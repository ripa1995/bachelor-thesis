import java.util.ArrayList;
import java.util.HashMap;

public class HeaderParser {
    private HashMap<String, Dependency> depSC;
    private ArrayList<String> lines;
    private HashMap<String, Integer> invokedServices;
    private HashMap<String, ArrayList<Header>> headers;
    public HeaderParser(ArrayList<String> _lines, HashMap<String, Integer> _invokedServices, HashMap<String, Dependency> _depSC){
        depSC = _depSC;
        lines = _lines;
        invokedServices = _invokedServices;
        headers = new HashMap<String, ArrayList<Header>>();
        identifyCallback();
        identifyVarAndEnc();

        printHeader();
    }

    private void printHeader() {

        for (String key : headers.keySet()) {
            System.out.println("\nNOME SERVIZIO: \n");
            System.out.println(key);
            System.out.println("\nNOMI VARIABILI + ENC\n");

            ArrayList<Header> headerVar = headers.get(key);
            for (Header h : headerVar) {
                System.out.println(h.getVarName());
                ArrayList<String> enc = h.getEnc();
                System.out.println(enc.toString());
            }

        }

    }

    private void identifyVarAndEnc() {
        boolean found = false;
        for (String s: invokedServices.keySet()) {
            if (Utils.isCallback(s)){
                int loc = invokedServices.get(s);
                s = Utils.extractCallbackName(s);
                if (headers.containsKey(s)) {
                    ArrayList<Header> arrayList = headers.get(s);
                    String st = lines.get(loc);
                    String[] token = st.trim().split("\\s");
                    ArrayList<String> param = Utils.extractParam(token);
                    for(String s2 : param) {
                        if (Utils.isType(s2)) {
                            continue;
                        }
                        String s3 = s2.replace("_","");
                        Dependency depV = depSC.get(s3);
                        if (depV == null) {
                            depV = depSC.get(s2);
                        }
                        Header header = new Header(s2);
                        for (Item exploit : depV.getExploit()) {
                            if (invokedServices.containsKey(exploit.getT())) {
                                header.addEnc(exploit.getT());
                            } else if (exploit.getT().equals("C")) {
                                found = false;
                                String enc = "";
                                for (Dependency dependency : depSC.values()) {
                                    for (Item init : dependency.getInit()) {
                                        if (init.getLoc() == exploit.getLoc()) {
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (found) {
                                        Item item = dependency.getExploit().get(0);
                                        enc = item.getT() + "Homomorphic";
                                        break;
                                    }
                                }
                                if (found) {
                                    header.addEnc(enc);
                                }
                            }
                        }
                        arrayList.add(header);
                    }
                    headers.replace(s, arrayList);
                }

            }
        }
    }

    private void identifyCallback() {
        for (String s : invokedServices.keySet()) {
            if (Utils.isCallback(s)) {
                headers.put(Utils.extractCallbackName(s), new ArrayList<Header>());
            }
        }
    }

    public HashMap<String, Dependency> getDepSC() {
        return depSC;
    }

    public void setDepSC(HashMap<String, Dependency> depSC) {
        this.depSC = depSC;
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
    }

    public HashMap<String, Integer> getInvokedServices() {
        return invokedServices;
    }

    public void setInvokedServices(HashMap<String, Integer> invokedServices) {
        this.invokedServices = invokedServices;
    }

    public HashMap<String, ArrayList<Header>> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, ArrayList<Header>> headers) {
        this.headers = headers;
    }
}
