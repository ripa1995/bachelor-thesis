import model.*;
import util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ConfidentialSC {

    SmartcontractDependencyParser smartcontractDependencyParser;
    HeaderParser headerParser;
    EncryptedOutputParser encryptedOutputParser;
    ArrayList<String> lines;
    HashMap<String, Integer> invokedServices;
    HashMap<String, Dependency> dependencySC;
    HashMap<String, ArrayList<Header>> headers;
    HashMap<String, ArrayList<EncryptedOutput>> encryptedOutput;
    ArrayList<Line> newLines;
    int pos;
    HashMap<String, QueryDetails> queryList;
    Dependency v3;

    public void init(String fileName) {
        smartcontractDependencyParser = new SmartcontractDependencyParser(fileName);
        lines = smartcontractDependencyParser.getLines();
        invokedServices = smartcontractDependencyParser.getInvokedServices();
        dependencySC = smartcontractDependencyParser.getDepSC();
        headerParser = new HeaderParser(lines, invokedServices, dependencySC);
        headers = headerParser.getHeaders();
        encryptedOutputParser = new EncryptedOutputParser(headers);
        encryptedOutput = encryptedOutputParser.getCoutput();
        newLines = new ArrayList<Line>();
        queryList = smartcontractDependencyParser.getQueryList();
        encrypter();
    }

    public void encrypter() {
        //Let CSC be a copy of SC -> lines
        //2: headerlist = ""  -> headers
        //3: for all depv appartenenti depSC do
        //      4: switch (depv:init:T )
        //      5: case name appartenente InvokedServices: -> già fatto??
        //          6: headerv = ""
        //          7: pos = 0
        //          8: for all ei 2 depv:exploit do
        //              9: computation(ei, headerv, pos, depv) -> manca questa parte
        //          10: end for
        //      11: case C: -> da fare
        //          12: Let X be the operation into depv:init:line
        //          13: Let Operator be the set of variables and constant used by .
        //          14: switch (x)
        //              15: CSC, line depv:init:line: Substitute x with corresponding encryption
        //              scheme operation
        //          16: end switch
        //          17: for all oi 2 Operator do
        //              18: if oi =2 depSC then
        //              19: encrypt oi with Encryption scheme Pk
        //              20: end if
        //          21: end for
        //      22: end switch
        //      23: headers = headers U headerv
        //24: end for
        //25: Let querylist be the set of query declared in SC -> event?
        //26: Headers = ””
        //27: for all Q appartenenti querylist do
        //      28: Q:declarationline:parameters = Q:declarationline:parameters U parameterHeader ->aggiungere l'header ai parametri
        //      29: for all li 2 Q:invocations do
        //          30: for all v 2 Q:callbackline do
        //              31: Let headerv 2 headerlist be the header associated with v
        //              32: Headers = Headers U headerv
        //          33: end for
        //      34: modify Q declaration in line Q:invocations:li - 1 to insert the ->aggiungere i parametri di header alle chiamate
        //      additional parameter of Headers
        //      35: li:parameters = li:parameters U headers
        //36: end for
        //37: end for
        for(String dependencyVarName:dependencySC.keySet()) {
            //linea 3-24
            Dependency dependency = dependencySC.get(dependencyVarName);
            for(Item init: dependency.getInit()) {
                switch (init.getT()) {
                    case "C":
                        //linea 12-21
                        String operation = Utils.extractOperation(lines.get(init.getLoc()));
                        ArrayList<String> operators = Utils.extractOperators(lines.get(init.getLoc()));
                        if (operation!=null) {
                            switch (operation) {
                                //modificare l'operazione con la corrispondente
                                //TODO: generare il codice da inserire
                                case "+":
                                    break;
                                case "-":
                                    break;
                                case "*":
                                    break;
                                case "/":
                                    break;
                            }
                            if (operators!=null) {
                                for (String operator: operators) {
                                    if (!dependencySC.containsKey(operator)) {
                                        //Crittografare l'operatore con le schema Pk

                                    }
                                }
                            }
                        }
                        break;
                    default:
                        //linea 6-10
                        pos = 0;
                        for(Item exploit: dependency.getExploit()) {
                            computation(exploit, pos, init, dependency);
                        }
                        break;
                }
            }
        }
        //linee 23-37
        for(String queryName:queryList.keySet()){
            QueryDetails queryDetails = queryList.get(queryName);
            String headerVarInit = getHeaderVarInit(queryName);
            if (headerVarInit != null) {
                //modifico la linea in cui viene dichiarata aggiungendo il parametro Header
                int decLine = queryDetails.getDecLine();
                String declaration = lines.get(decLine);
                declaration = Utils.addHeaderParameter(declaration, ", string header");
                lines.set(decLine, declaration);
                for (int line : queryDetails.getInvocations()) {
                    //alla riga - 1 crea una variabile header
                    //modfica l'invocazione aggiungendo il parametro header
                    Line newLine = new Line(line - 1, getHeaderVarInit(queryName));
                    newLines.add(newLine);
                    String invocation = lines.get(line);
                    invocation = Utils.addHeaderParameter(invocation, ", header");
                    lines.set(line, invocation);
                }
            }
        }
    }

    public void computation(Item exploit, int pos, Item init, Dependency dependency) {
        //1: switch (ei:T )
        //2: case name:
        //      3: headerv = headerv U name
        //      4: CSC, line depv:init:line - 1: Insert an instruction to initialize a new
        //      variable encvpos with value stored in pos-th position of v parameter returned
        //      by depv:init:T:name
        //      5: pos + +
        //6: case C|T:
        //      7: Let v'' appartenente DepSC such as depv'' :init:line == ei:line
        //      8: servicecount = 0
        //      9: for all ek appartenente depv'' :exploit do
        //          10: if ek:T appartenente InvokedServices then
        //              11: headerv = headerv U namehomomorfic
        //              12: CSC, line depv:init:line - 1: Insert an instruction to initialize
        //              a new variable encvpos with value stored in pos-th position of v
        //              parameter returned by service specified in depv:init:T
        //              13: pos + +
        //              14: servicecount + +
        //              15: if servicecount > 1 then
        //                  16: CSC, line depv:init:line-1:Insert an instruction to initialize a
        //                  new variable vservice count with the value stored in pos-th position
        //                  of v parameter returned by service specified in depv:init:T .
        //                  17: CSC, replace parameter v'' with vservice count
        //              18: end if
        //          19: else
        //              20: Computation(ek; headerv; c; depv00 )
        //          21: end if
        //      22: end for
        //23: end switch
        Dependency v2 = null;
        switch (exploit.getT()) {
            case "C":
            case "T":
                //linee 6-22
                for (Dependency dep : dependencySC.values()) {
                    for (Item init2 : dep.getInit()) {
                        if (init2.getLoc() == exploit.getLoc()) {
                            v2 = dep;
                            break;
                        }
                    }
                }
                int serviceCount = 0;
                for (Item exp : v2.getExploit()) {
                    if (invokedServices.containsKey(exp.getT())) {
                        //linea 12
                        //TODO: generare il codice da inserire
                        int line = init.getLoc()-1;
                        String code = "";
                        Line newLine = new Line(line, code);
                        newLines.add(newLine);
                        pos++;
                        serviceCount++;
                        if (serviceCount>1) {
                            //linee 16-17
                            //TODO: generare il codice da inserire
                            line = init.getLoc()-1;
                            code = "";
                            newLine = new Line(line, code);
                            newLines.add(newLine);
                        }
                    } else {
                        if (v2 != dependency) {
                            computation(exp, pos, init, v2);
                        }
                    }
                }
                break;
            default:
                //linea 4
                //TODO: generare il codice da inserire
                int line = init.getLoc()-1;
                String code = "";
                Line newLine = new Line(line, code);
                newLines.add(newLine);
                pos++;
                break;
        }
    }

    private String getHeaderVarInit(String queryName) {
        Header header;
        ArrayList<String> encHeader;
        String newVar = "string header = ";
        ArrayList<Header> headerVar = headers.get(queryName);
        if (headerVar == null) {
            return null;
        }
        int last = headerVar.size()-1;
        for (int i = 0; i< last; i++) {
            header = headerVar.get(i);
            newVar = newVar + header.getVarName() + ", ";
            encHeader = header.getEnc();
            for (String anEncHeader : encHeader) {
                newVar = newVar + anEncHeader + ", ";
            }
        }
        header = headerVar.get(last);
        newVar = newVar + header.getVarName();
        encHeader = header.getEnc();
        if (encHeader.size()==0) {
            newVar = newVar + ";";
        } else {
            newVar = newVar + ", ";
            last = encHeader.size() - 1;
            for (int j = 0; j < last; j++) {
                newVar = newVar + encHeader.get(j) + ", ";
            }
            newVar = newVar + encHeader.get(last) + ";";
        }
        return newVar;
    }
}
