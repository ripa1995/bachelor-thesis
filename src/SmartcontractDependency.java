import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class SmartcontractDependency {
    private HashMap<String, Dependency> depSC;
    private ArrayList<String> lines;
    private HashMap<String, Integer> invokedServices;
    public SmartcontractDependency(String fileName){
        depSC = new HashMap<String, Dependency>();
        invokedServices = new HashMap<String, Integer>();
        File file = new File(fileName);
        lines = new ArrayList<String>();
        try {
            FileReader reader = new FileReader(file);
            BufferedReader buffReader = new BufferedReader(reader);
            String s;
            while((s = buffReader.readLine()) != null){
                lines.add(s);
            }
        } catch(IOException e){
            //handle exception
            System.out.println("Exception");
        }
        // And just to prove we have the lines right where we want them..
        for(String st: lines)
            System.out.println(st);
        identifyVariablesAndInvokedServices();

        for (String key: depSC.keySet()){
            String value = depSC.get(key).toString();
            System.out.println(key + " " + value);
        }
        for (String key: invokedServices.keySet()){
            String value = invokedServices.get(key).toString();
            System.out.println(key + " " + value);
        }
    }

    public HashMap<String, Dependency> getDepSC() {
        return depSC;
    }

    public void setDepSC(HashMap<String, Dependency> depSC) {
        this.depSC = depSC;
    }

    private void identifyVariablesAndInvokedServices() {
        for(int i=0; i<lines.size(); i++){
            String st = lines.get(i);
            String[] token = st.trim().split("\\s");

            if (Utils.isType(token[0])) {
                //la prima parola è un tipo di variabile -> definendo una variabile
                if (Utils.isStoreKeyword(token[1])) {
                    depSC.putIfAbsent(Utils.removeLastChar(token[2]), new Dependency());
                } else {
                    depSC.putIfAbsent(Utils.removeLastChar(token[1]), new Dependency());
                }
                continue;
            }
            if (Utils.isFunction(token[0])) {
                //prima parola è function -> definendo una function
                if (Utils.isQuery(token[1])) {
                    //query
                    invokedServices.putIfAbsent(Utils.extractMethodName(token[1]),i);
                    continue;
                } else if (Utils.isCallback(token[1])) {
                    //callback
                    invokedServices.putIfAbsent(Utils.extractMethodName(token[1]),i);
                    ArrayList<String> param = Utils.extractParam(token);
                    for(String s: param){
                        if (Utils.isType(s)) {
                            continue;
                        }
                        String s2 = s.replace("_","");
                        Object o = depSC.get(s2);
                        if (o == null) {
                            //aggiungo le variabili restituite dalle callback (e quindi inizializzate ora) a depSC
                            depSC.putIfAbsent(s, new Dependency());
                        }
                    }
                    continue;
                }
            }
            if (Utils.isEvent(token[0])) {
                //prima parola è event -> definendo un event
                //serve?
            }
        }
    }
}
