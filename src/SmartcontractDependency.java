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
        /*
        for (String key: depSC.keySet()){
            String value = depSC.get(key).toString();
            System.out.println(key + " " + value);
        }
        for (String key: invokedServices.keySet()){
            String value = invokedServices.get(key).toString();
            System.out.println(key + " " + value);
        }*/
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
                depSC.putIfAbsent(token[1], new Dependency());
                continue;
            }
            if (Utils.isFunction(token[0])) {
                //prima parola è function -> definendo una function
                if (Utils.isQuery(token[1])) {
                    //query
                    //TODO: separare il nome del metodo dall'inizio dei parametri
                    invokedServices.putIfAbsent(token[1],i);
                    continue;
                } else if (Utils.isCallback(token[1])) {
                    //callback
                    //TODO: separare il nome del metodo dall'inizio dei parametri e vedere se tra i parametri vi sono nuove variabili da aggiungere a depSC
                    invokedServices.putIfAbsent(token[1],i);
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
