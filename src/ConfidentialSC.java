import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ConfidentialSC {

    SmartcontractDependencyParser smartcontractDependencyParser;
    HeaderParser headerParser;
    EncryptedOutputParser encryptedOutputParser;
    ArrayList<String> lines;
    HashMap<String, Integer> invokedServices;
    HashMap<String, Dependency> dependencySC;
    HashMap<String, ArrayList<Header>> headers;
    HashMap<String, ArrayList<EncryptedOutput>> encryptedOutput;
    public void init(String fileName) {
        smartcontractDependencyParser = new SmartcontractDependencyParser(fileName);
        lines = smartcontractDependencyParser.getLines();
        invokedServices = smartcontractDependencyParser.getInvokedServices();
        dependencySC = smartcontractDependencyParser.getDepSC();
        headerParser = new HeaderParser(lines, invokedServices, dependencySC);
        headers = headerParser.getHeaders();
        encryptedOutputParser = new EncryptedOutputParser(headers);
        encryptedOutput = encryptedOutputParser.getCoutput();
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
    }

    public void computation(String exploit, Header header, int pos, Dependency dependency) {
        //1: switch (ei:T )
        //2: case name:
        //      3: headerv = headerv [ name
        //      4: CSC, line depv:init:line 􀀀 1: Insert an instruction to initialize a new
        //      variable encvpos with value stored in pos-th position of v parameter returned
        //      by depv:init:T:name
        //      5: pos + +
        //6: case CjT:
        //      7: Let v00 2 DepSC such as depv00 :init:line == ei:line
        //      8: servicecount = 0
        //      9: for all ek 2 depv00 :exploit do
        //          10: if ek:T 2 InvokedServices then
        //              11: headerv = headerv [ namehomomorfic
        //              12: CSC, line depv:init:line 􀀀 1: Insert an instruction to initialize
        //              a new variable encvpos with value stored in pos-th position of v
        //              parameter returned by service specified in depv:init:T
        //              13: pos + +
        //              14: servicecount + +
        //              15: if servicecount > 1 then
        //                  16: CSC, line depv:init:line􀀀1:Insert an instruction to initialize a
        //                  new variable vservice count with the value stored in pos-th position
        //                  of v parameter returned by service specified in depv:init:T .
        //                  17: CSC, replace parameter v00with vservice count
        //              18: end if
        //          19: else
        //              20: Computation(ek; headerv; c; depv00 )
        //          21: end if
        //      22: end for
        //23: end switch
    }

}
