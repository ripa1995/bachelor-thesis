import model.Constant;
import model.Dependency;
import model.Item;
import model.QueryDetails;
import util.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SmartcontractDependencyParser {
    private HashMap<String, Dependency> depSC;
    private ArrayList<String> lines;
    private HashMap<String, Integer> invokedServices;
    private HashMap<String, QueryDetails> queryList;
    private HashMap<String, Constant> constantHashMap;
    public SmartcontractDependencyParser(String fileName){
        depSC = new HashMap<String, Dependency>();
        invokedServices = new HashMap<String, Integer>();
        queryList = new HashMap<String, QueryDetails>();
        constantHashMap = new HashMap<String, Constant>();
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
        // stampa lo sc
        for(String st: lines)
            System.out.println(st);
        //Crea gli insiemi Var (come key di depSC) e InvokedServices
        identifyVariablesAndInvokedServices();
        identifyVariablesInitAndExploit();
        printVarDepAndServices();
        printQueryList();
    }

    public HashMap<String, QueryDetails> getQueryList() {
        return queryList;
    }

    public void setQueryList(HashMap<String, QueryDetails> queryList) {
        this.queryList = queryList;
    }

    private void printQueryList() {
        System.out.println("\nQUERY LIST\n");
        for (String key: queryList.keySet()){
            String value = queryList.get(key).toString();
            System.out.println(key + "\n" + value +"\n");
        }
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
                    depSC.putIfAbsent(token[2].replace(";",""), new Dependency());
                } else {
                    depSC.putIfAbsent(token[1].replace(";",""), new Dependency());
                }
                continue;
            }
            if (Utils.isFunction(token[0])) {
                //prima parola è function -> definendo una function
                if (Utils.isQuery(token[1])) {
                    //query

                } else if (Utils.isCallback(token[1])) {
                    //callback
                    String methodName = Utils.extractMethodName(token[1]);
                    String queryName = methodName.substring(9);
                    if (queryList.containsKey(queryName)) {
                        QueryDetails queryDetails = queryList.get(queryName);
                        queryDetails.setCallbackLine(i);
                        queryList.replace(queryName, queryDetails);
                    }
                    invokedServices.putIfAbsent(methodName, i);
                    ArrayList<String> param = Utils.extractParam(token);
                    for(String s: param){
                        if (Utils.isType(s)) {
                            continue;
                        }
                        String s2 = s.replace("_","");
                        Object o = depSC.get(s2);
                        if (o == null) {
                            //aggiungo le variabili restituite dalle callback (e quindi inizializzate ora) a depSC
                            Dependency dependency = new Dependency();
                            dependency.addInit(i, methodName);
                            depSC.putIfAbsent(s, dependency);
                        }
                    }
                    continue;
                }
            }
            if (Utils.isEvent(token[0])) {
                String methodName = Utils.extractMethodName(token[1]);
                invokedServices.putIfAbsent(methodName,i);
                QueryDetails queryDetails = new QueryDetails();
                queryDetails.setDecLine(i);
                queryList.putIfAbsent(methodName, queryDetails);
                continue;
            }
        }
    }

    private void identifyVariablesInitAndExploit() {
        String s = "";
        for(int i=0; i<lines.size(); i++) {
            Dependency dependency = new Dependency();
            String st = lines.get(i);
            String[] token = st.trim().split("\\s");
            if (Utils.canSkip(token[0])) {
                continue;
            }
            if (Utils.isFunction(token[0])) {
                String s1 = Utils.extractMethodName(token[1]);
                if (invokedServices.getOrDefault(s1, 0).equals(i)) {
                    //siamo nella loc di un servizio invocato
                    s = s1;
                    continue;
                }
            }

            if (Utils.isVariableInit(depSC.keySet(), token)) {
                if (Utils.isInitAsConstant(token)) {
                    String t = token[2];
                    if (t.endsWith(";")) {
                        t = Utils.removeLastChar(t);
                    }
                    Integer integer = Integer.parseInt(t);
                    Constant constant = new Constant(i, integer);
                    constantHashMap.putIfAbsent(token[0], constant);
                } else if (Utils.isInitByComputation(token)) {
                    dependency = depSC.get(token[0]);
                    dependency.addInit(i, "C");
                    depSC.replace(token[0], dependency);
                    //identifica le variabili utilizzate per la computazione
                    ArrayList<String> arrayList = Utils.extractVariablesExploited(depSC.keySet(), token);
                    for(String s1: arrayList){
                        dependency = depSC.get(s1);
                        dependency.addExploit(i, "C");
                        depSC.replace(s1, dependency);
                    }
                } else {
                    dependency = depSC.get(token[0]);
                    dependency.addInit(i, s);
                    depSC.replace(token[0], dependency);
                }
                continue;
            }

            if (Utils.isQueryCall(invokedServices, token)) {
                String queryName = (Utils.extractMethodName(token[0]));
                if (queryList.containsKey(queryName)) {
                    QueryDetails queryDetails = queryList.get(queryName);
                    queryDetails.addInvocationLine(i);
                    queryList.replace(queryName, queryDetails);
                }
                ArrayList<String> param = Utils.extractParam(token);
                for (String s1 : param) {
                    dependency = depSC.get(s1);
                    dependency.addExploit(i, Utils.extractMethodName(token[0]));
                    depSC.replace(s1, dependency);
                }
                continue;
            }

            if (Utils.isTestCondition(token)) {
                //TODO: identificare exploit variabili condizionali
                ArrayList<String> testVar = Utils.extractTestVar(depSC.keySet(), token);
                for (String s1 : testVar) {
                    dependency = depSC.get(s1);
                    dependency.addExploit(i,"Test");
                    depSC.replace(s1, dependency);
                }
                continue;
            }




        }

    }

    private void printVarDepAndServices(){
        System.out.println("\nVARIABILI\n");
        for (String key: depSC.keySet()){
            String value = depSC.get(key).toString();
            System.out.println(key + " " + value);
        }
        System.out.println("\nSERVIZI INVOCATI\n");
        for (String key: invokedServices.keySet()){
            String value = invokedServices.get(key).toString();
            System.out.println(key + " " + value);
        }
        for (String key: depSC.keySet()){
            ArrayList<Item> item = depSC.get(key).getInit();
            System.out.println("\nINIT DI " + key +"\n");
            for (Item i2: item) {
                System.out.println(i2.getLoc() +" "+ i2.getT());
            }
            item = depSC.get(key).getExploit();
            System.out.println("\nEXPLOIT DI " + key +"\n");
            for (Item i3: item) {
                System.out.println(i3.getLoc() + " "+i3.getT());
            }
        }
    }

    public HashMap<String, Constant> getConstantHashMap() {
        return constantHashMap;
    }

    public void setConstantHashMap(HashMap<String, Constant> constantHashMap) {
        this.constantHashMap = constantHashMap;
    }
}
