import model.*;
import util.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import jpaillier.*;

public class ConfidentialSC {

    SmartcontractDependencyParser smartcontractDependencyParser;
    HeaderParser headerParser;
    EncryptedOutputParser encryptedOutputParser;
    ArrayList<String> linesSC;
    HashMap<String, Integer> invokedServices;
    HashMap<String, Dependency> dependencySC;
    HashMap<String, ArrayList<Header>> headers;
    HashMap<String, ArrayList<EncryptedOutput>> encryptedOutput;
    ArrayList<Line> newLines;
    int pos;
    HashMap<String, QueryDetails> queryList;
    Dependency v3;
    String varName;
    HashMap<String, Constant> constantHashMap;
    ArrayList<String> newVarNames;
    ArrayList<String> oldVarNames;
    HashMap<Integer, String> duplicatedLines;
    HashMap<Integer, String> lines;
    ArrayList<Integer> linesToRemove;
    ArrayList<String> csc;

    public void init(String fileName) {
        smartcontractDependencyParser = new SmartcontractDependencyParser(fileName);
        linesSC = smartcontractDependencyParser.getLines();
        invokedServices = smartcontractDependencyParser.getInvokedServices();
        dependencySC = smartcontractDependencyParser.getDepSC();
        headerParser = new HeaderParser(linesSC, invokedServices, dependencySC);
        headers = headerParser.getHeaders();
        encryptedOutputParser = new EncryptedOutputParser(headers);
        encryptedOutput = encryptedOutputParser.getCoutput();
        newLines = new ArrayList<Line>();
        queryList = smartcontractDependencyParser.getQueryList();
        constantHashMap = smartcontractDependencyParser.getConstantHashMap();
        newVarNames = new ArrayList<String>();
        oldVarNames = new ArrayList<String>();
        linesToRemove = new ArrayList<Integer>();
        lines = new HashMap<Integer, String>();
        duplicatedLines = new HashMap<Integer, String>();
        encrypter();
        addFunctionToSC();
        rewriteCSC();
    }

    public void encrypter() {
        //Let CSC be a copy of SC -> linesSC
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
        for(String dependencyVarName : dependencySC.keySet()) {
            //linea 3-24
            varName = dependencyVarName;
            Dependency dependency = dependencySC.get(dependencyVarName);
            for(Item init: dependency.getInit()) {
                switch (init.getT()) {
                    case "C":
                        //linea 12-21
                        int op = -1;
                        String linea = linesSC.get(init.getLoc());
                        String operation = Utils.extractOperation(linea);
                        ArrayList<String> operators = Utils.extractOperators(linea);
                        if (operation!=null) {
                            switch (operation) {
                                //modificare l'operazione con la corrispondente
                                case "+":
                                    //diventa * con paillier e con elGamal
                                    if (true||true) {
                                        linea = linea.replace("+", "*");
                                    }
                                    op = 1;
                                    break;
                                case "-":
                                    break;
                                case "*":
                                    //diventa ^ con paillier
                                    if (true) {
                                        linea = linea.replace("*", "^");
                                    }
                                    //rimane * con elGamal
                                    op = 3;
                                    break;
                                case "/":
                                    break;
                            }
                            if (operators!=null) {
                                for (String operator: operators) {

                                    //Crittografare l'operatore con le schema Pk
                                    //pallier
                                    if (constantHashMap.containsKey(operator)) {
                                        Constant c = constantHashMap.get(operator);
                                        String s = linesSC.get(c.getLoc());
                                        KeyPairBuilder keygen = new KeyPairBuilder();
                                        KeyPair keyPair = keygen.generateKeyPair();
                                        PublicKey publicKey = keyPair.getPublicKey();
                                        BigInteger ciphertext = publicKey.encrypt(BigInteger.valueOf((long) c.getValue()));
                                        int lenght = s.length();
                                        String toBeReplaced = c.getValue() + ";";
                                        s = s.replace(toBeReplaced, ciphertext.toString()+";");
                                        if (lenght==s.length()) {
                                            toBeReplaced = " " + c.getValue() + " ";
                                            s = s.replace(toBeReplaced, ciphertext.toString());
                                        }
                                        linesSC.set(c.getLoc(), s);
                                    } else if ((!dependencySC.containsKey(operator))&&(!newVarNames.contains(operator))) {
                                        KeyPairBuilder keygen = new KeyPairBuilder();
                                        KeyPair keyPair = keygen.generateKeyPair();
                                        PublicKey publicKey = keyPair.getPublicKey();
                                        BigInteger ciphertext = publicKey.encrypt(BigInteger.valueOf(Long.valueOf(operator)));

                                        int lenght = linea.length();
                                        String toBeReplaced = operator + ";";
                                        linea = linea.replace(toBeReplaced, ciphertext.toString()+";");
                                        if (lenght==linea.length()) {
                                            toBeReplaced = " " + operator + " ";
                                            linea = linea.replace(toBeReplaced, ciphertext.toString());
                                        }
                                    }
                                }
                            }
                            linesSC.set(init.getLoc(), linea);
                        }
                        break;
                    case "Test":
                        break;
                    case "":
                        break;
                    default:
                        //linea 6-10
                        pos = 0;
                        for(Item exploits: dependency.getExploit()) {
                            computation(exploits, init, dependency);
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
                String declaration = linesSC.get(decLine);
                declaration = Utils.addHeaderParameter(declaration, ", string header");
                linesSC.set(decLine, declaration);
                for (int line : queryDetails.getInvocations()) {
                    //alla riga - 1 crea una variabile header
                    //modfica l'invocazione aggiungendo il parametro header
                    Line newLine = new Line(line - 1, getHeaderVarInit(queryName));
                    newLines.add(newLine);
                    String invocation = linesSC.get(line);
                    invocation = Utils.addHeaderParameter(invocation, ", header");
                    linesSC.set(line, invocation);
                }
            }
        }
    }

    public void computation(Item exploit, Item init, Dependency dependency) {
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
        Item initV2 = null;
        switch (exploit.getT()) {
            case "C":
            case "Test":
                //linee 6-22
                for (String var : dependencySC.keySet()) {
                    Dependency dep = dependencySC.get(var);
                    for (Item init2 : dep.getInit()) {
                        if (init2.getLoc() == exploit.getLoc()) {
                            v2 = dep;
                            initV2 = init2;
                            break;
                        }
                    }
                }
                if (v2==null) break;
                int serviceCount = 0;
                for (Item exp : v2.getExploit()) {
                    if (invokedServices.containsKey(exp.getT())) {
                        //linea 12
                        int line;
                        if (invokedServices.containsValue(init.getLoc())) {
                            line = exploit.getLoc() - 1;
                        } else {
                            line = init.getLoc()-1;
                            linesToRemove.add(init.getLoc());
                        }
                        //recupero il nome del parametro di output
                        String paramName = getParamNameOfCurrentVar(init);
                        if (paramName.isEmpty()) {
                            //qualcosa è andato storto
                            break;
                        }
                        //recupero l'enc schema della variabile nella callback di buyergui
                        ArrayList<String> encOfVar = getEncOfCurrentVar(init);
                        if (encOfVar == null) {
                            //qualcosa è andato storto
                            break;
                        }
                        //recupero la posizione all'interno del enc schema in base al metodo su cui ricadrà la variabile
                        boolean found = false;
                        String enc = "";
                        for (Dependency dep : dependencySC.values()) {
                            for (Item init2 : dep.getInit()) {
                                if (init2.getLoc() == exploit.getLoc()) {
                                    found = true;
                                    break;
                                }
                            }
                            if (found) {
                                Item item = dep.getExploit().get(0);
                                enc = item.getT() + "Homomorphic";
                                break;
                            }
                        }
                        int encPos;
                        if (found) {
                            if (encOfVar.size() == 1) {
                                encPos = 0;
                            } else {
                                encPos = encOfVar.indexOf(enc);
                            }
                        } else {
                            break;
                        }
                        //converto il parametro di ritorno in stringa
                        String code = "string memory " + varName + enc + serviceCount + "string = toString(" + paramName + "); "+"\n";
                        //estrapolo dalla stringa ottenuta la parte di mio interesse (ipotizzo che ogni sottostringa di interesse
                        //sia lunga 77, quindi modificando la posizione di partenza in base alla posizione encPos (77*0 -> prima posizione, 77*1, ecc.)
                        code = code + "string memory " + varName + enc + serviceCount + "substring = substring(" + varName + enc + serviceCount +"string, " + 77 + ", " + 77*encPos + "); "+"\n";
                        //converto nuovamente la sottostringa a uint
                        String newVarName =varName + enc + serviceCount;
                        code = code + newVarName + " = parseInt(" + varName + enc + serviceCount + "substring);";
                        Line newLine = new Line(line, code);
                        newLines.add(newLine);
                        newVarNames.add(newVarName);
                        oldVarNames.add(varName);
                        String linea;
                        if (lines.containsKey(exploit.getLoc())){
                            linea = lines.get(exploit.getLoc());
                        } else {
                            linea = linesSC.get(exploit.getLoc());
                        }
                        int lenght = linea.length();
                        String toBeReplaced = varName + " ";
                        linea = linea.replace(toBeReplaced, newVarName + " ");
                        if (lenght==linea.length()) {
                            toBeReplaced = varName + ";";
                            linea = linea.replace(toBeReplaced, newVarName+";");
                        }
                        lines.put(exploit.getLoc(), linea);

                        pos++;
                        serviceCount++;
                        if (serviceCount>1) {
                            //linee 16-17

                            int i = initV2.getLoc();
                            String duplicatedLine;

                            if (duplicatedLines.containsKey(i)) {
                                duplicatedLine = duplicatedLines.get(i);
                            } else {
                                duplicatedLine = linesSC.get(i);
                                String[] token = duplicatedLine.trim().split("\\s");
                                String initVar;
                                if (!Utils.isType(token[0])) {
                                    initVar = token[0];
                                } else if(!Utils.isStoreKeyword(token[1])) {
                                    initVar = token[1];
                                } else {
                                    initVar = token[2];
                                }
                                duplicatedLine = duplicatedLine.replaceFirst(initVar, initVar+serviceCount);
                                //sostituisco il nome della variabile nella lista dei parametri relativi all'exploit considerato
                                String s = linesSC.get(exp.getLoc());
                                s = s.replace(initVar, initVar+serviceCount);
                                linesSC.set(exp.getLoc(), s);
                                newVarNames.add(initVar+serviceCount);
                            }
                            duplicatedLine = duplicatedLine.replace(varName, newVarName);

                            duplicatedLines.put(i,duplicatedLine);




                        }
                    } else {
                        if (v2 != dependency) {
                            if ((!initV2.getT().equals("C"))&&(!initV2.getT().equals("Test"))) {
                                computation(exp, initV2, v2);
                            }
                        }
                    }
                }
                break;
            default:
                //linea 4
                int line;
                if (invokedServices.containsValue(init.getLoc())) {
                    line = exploit.getLoc() - 1;
                } else {
                    line = init.getLoc()-1;
                    linesToRemove.add(init.getLoc());
                }
                //recupero il nome del parametro di output
                String paramName = getParamNameOfCurrentVar(init);
                if (paramName.isEmpty()) {
                    //qualcosa è andato storto
                    break;
                }
                //recupero l'enc schema della variabile nella callback di buyergui
                ArrayList<String> encOfVar = getEncOfCurrentVar(init);
                if (encOfVar == null) {
                    //qualcosa è andato storto
                    break;
                }
                //recupero la posizione all'interno del enc schema in base al metodo su cui ricadrà la variabile
                int encPos;
                if (encOfVar.size()==1) {
                    encPos = 0;
                } else {
                    encPos = encOfVar.indexOf(exploit.getT());
                }
                //converto il parametro di ritorno in stringa
                String code = "string memory " + varName + exploit.getT() + "string = toString(" + paramName + "); "+"\n";
                //estrapolo dalla stringa ottenuta la parte di mio interesse (ipotizzo che ogni sottostringa di interesse
                //sia lunga 77, quindi modificando la posizione di partenza in base alla posizione encPos (77*0 -> prima posizione, 77*1, ecc.)
                code = code + "string memory " + varName + exploit.getT() + "substring = substring(" + varName + exploit.getT() +"string, " + 77 + ", " + 77*encPos + "); "+"\n";
                //converto nuovamente la sottostringa a uint
                String newVarName = varName + exploit.getT();
                newVarNames.add(newVarName);
                oldVarNames.add(varName);
                code = code + newVarName + " = parseInt(" + varName + exploit.getT() + "substring);";
                Line newLine = new Line(line, code);
                newLines.add(newLine);
                String linea = linesSC.get(exploit.getLoc());
                linea = linea.replace(varName,newVarName);
                linesSC.set(exploit.getLoc(), linea);

                pos++;
                break;
        }
    }

    private String getParamNameOfCurrentVar(Item init) {
        String paramName="";
        ArrayList<String> param = Utils.extractParam(linesSC.get(invokedServices.get(init.getT())).split("\\s"));
        for (int i=0; i<param.size(); i++) {
            paramName = param.get(i);
            if (paramName.endsWith(varName)){
                break;
            } else {
                paramName = varName;
            }
        }
        return paramName;
    }

    private ArrayList<String> getEncOfCurrentVar(Item init) {
        String serviceName = Utils.serviceName(init.getT());
        ArrayList<Header> headerArrayList = headers.get(serviceName);
        ArrayList<String> encOfVar = null;
        for(Header header: headerArrayList) {
            if ((header.getVarName().endsWith("_"+varName))||(header.getVarName().equals(varName))) {
                encOfVar = header.getEnc();
                break;
            } else {
                encOfVar = null;
            }
        }
        return encOfVar;
    }

    private String getHeaderVarInit(String queryName) {
        Header header;
        ArrayList<String> encHeader;
        String newVar = "string header = \"";
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
            newVar = newVar + "\";";
        } else {
            newVar = newVar + ", ";
            last = encHeader.size() - 1;
            for (int j = 0; j < last; j++) {
                newVar = newVar + encHeader.get(j) + ", ";
            }
            newVar = newVar + encHeader.get(last) + "\";";
        }
        return newVar;
    }

    private void addFunctionToSC(){
        int i = linesSC.lastIndexOf("}");
        String function = "function parseInt(string _value) public returns (uint _ret) {\n" +
                "bytes memory _bytesValue = bytes(_value);\n" +
                "uint j = 1;\n" +
                "for (uint i = _bytesValue.length -1; i>=0 && i<_bytesValue.length; i--){\n" +
                "assert(_bytesValue[i] >= 48 && _bytesValue[i]<=57);\n" +
                "_ret +=(uint(_bytesValue[i])-48)*j;\n" +
                "j*=10;\n" +
                "}\n" +
                "}\n" +
                "\n" +
                "function substring(string _base, uint _length, uint _offset) internal returns (string) {\n" +
                "    bytes memory _baseBytes = bytes(_base);\n" +
                "assert(uint(_offset+_length)<=_baseBytes.length);\n" +
                "string memory _tmp = new string(uint(_length));\n" +
                "    bytes memory _tmpBytes = bytes(_tmp);\n" +
                "uint j=0;\n" +
                "    for(uint i = uint(_offset); i < uint(_offset+_length); i++) {\n" +
                "        _tmpBytes[j++] = _baseBytes[i];\n" +
                "    }\n" +
                "    return string(_tmpBytes);\n" +
                "}\n" +
                "\n" +
                "function toString(uint _base) internal returns (string) {\n" +
                "bytes memory _tmp = new bytes(32);\n" +
                "uint i;\n" +
                "for (i=0;_base>0;i++) {\n" +
                "_tmp[i] = byte((_base%10)+48);\n" +
                "_base /= 10;\n" +
                "}\n" +
                "bytes memory _real = new bytes(i--);\n" +
                "for (uint j = 0; j<_real.length; j++) {\n" +
                "_real[j] = _tmp[i--];\n" +
                "}\n" +
                "return string(_real);\n" +
                "}";
        String[] functionArray = function.split("\\n");
        for (String s : functionArray) {
            linesSC.add(i, s);
            i++;
        }
    }

    public void rewriteCSC() {
        csc = linesSC;
        //Sostituisce le inizializzazioni da computazione
        for(int i:lines.keySet()) {
            csc.set(i,lines.get(i));
        }
        //"rimuove" le linee in eccesso
        for(int i: linesToRemove){
            csc.set(i, "");
        }
        //aggiunge alla lista di nuove linee quelle riguardanti le variabili duplicate
        for(int i: duplicatedLines.keySet()) {
            newLines.add(new Line(i,duplicatedLines.get(i)));
        }
        //rimuove duplicati
        ArrayList<Line> noDuplicates = new ArrayList<Line>();
        for (Line line:newLines) {
            boolean isFound = false;
            //controllo che non sia presente in noDup
            for (Line l:noDuplicates) {
                if (l.getCode().equals(line.getCode())) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) noDuplicates.add(line);
        }
        //rimuove duplicati
        ArrayList<String> noDuplicatesVar = new ArrayList<String>();
        for (String s:oldVarNames) {
            boolean isFound = false;
            //controllo che non sia presente in noDup
            for (String l:noDuplicatesVar) {
                if (l.equals(s)) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) noDuplicatesVar.add(s);
        }
        //Ordinamento decrescente in base alla loc
        noDuplicates.sort(new Comparator<Line>() {
            @Override
            public int compare(Line o1, Line o2) {
                return Integer.compare(o2.getLoc(), o1.getLoc());
            }
        });
        //aggiunge le nuove linee al csc
        for(Line line: noDuplicates) {
            csc.add(line.getLoc()+1,line.getCode());
        }
        boolean declaration = true;
        int i = 0;
        linesToRemove.clear();
        for(String s2: csc) {
            String[] token = s2.trim().split("\\s");
            if (Utils.isType(token[0])) {
                if (Utils.isStoreKeyword(token[1])) {
                    if(oldVarNames.contains(Utils.removeLastChar(token[2]))) {
                        i = csc.indexOf(s2);
                        linesToRemove.add(i);
                    }
                } else {
                    if(oldVarNames.contains(Utils.removeLastChar(token[1]))) {
                        i = csc.indexOf(s2);
                        linesToRemove.add(i);
                    }
                }
            }
            if (Utils.isFunction(s2)||Utils.isCallback(s2)||Utils.isQuery(s2)) {
                break;
            }
        }
        //"rimuove" le linee in eccesso
        Collections.sort(linesToRemove,Collections.reverseOrder());
        for(int j: linesToRemove){
            csc.remove(j);
        }
        noDuplicatesVar.clear();
        for (String s:newVarNames) {
            boolean isFound = false;
            //controllo che non sia presente in noDup
            for (String l:noDuplicatesVar) {
                if (l.equals(s)) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) noDuplicatesVar.add(s);
        }
        if (i>0) {
            for (String s : noDuplicatesVar) {
                csc.add(i, "uint " + s +";");
            }
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter("confidentialSC.sol");

        for(String str: csc) {
            writer.write(str+"\n");
        }
        writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
