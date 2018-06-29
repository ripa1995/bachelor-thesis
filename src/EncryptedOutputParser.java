import java.util.ArrayList;
import java.util.HashMap;

public class EncryptedOutputParser {
    private HashMap<String, ArrayList<Header>> headers;
    private HashMap<String, ArrayList<EncryptedOutput>> coutput;
    private ArrayList<Header> headerArrayList;
    private ArrayList<EncryptedOutput> encryptedOutputArrayList;
    private ArrayList<String> encArray;
    private ArrayList<EncValue> encValueArray;

    public EncryptedOutputParser(HashMap<String, ArrayList<Header>> _headers){
        headers = _headers;
        coutput = new HashMap<String, ArrayList<EncryptedOutput>>();
        initKeys();
        printEncyptedOutput();
    }

    private void initKeys() {
        //Individuo i nomi dei servizi
        for (String key: headers.keySet()) {
            headerArrayList = headers.get(key);
            encryptedOutputArrayList = new ArrayList<EncryptedOutput>();
            //raccolgo la variabili e come erano gestite nell'header
            for (Header h : headerArrayList) {
                encArray = h.getEnc();
                encValueArray = new ArrayList<EncValue>();
                //verifico le modalità di crittografia
                for (String enc : encArray) {
                    if (enc.contains("Homomorphic")) {
                        encValueArray.add(new DoubleEncValue(h.getVarName(), "key1", "keyHomomorphic"));
                    } else {
                        encValueArray.add(new EncValue(h.getVarName(), "key1"));
                    }
                }
                //aggiungo la variabile e gli encValue alla lista
                encryptedOutputArrayList.add(new EncryptedOutput(h.getVarName(), encValueArray));
            }
            //aggiungo il servizio e la lista di encrypted output alla mappa
            coutput.put(key, encryptedOutputArrayList);
        }
    }

    private void printEncyptedOutput() {
        System.out.println("\nEncrypted output \n");
        for (String key : coutput.keySet()) {
            System.out.println("\nNOME SERVIZIO: \n");
            System.out.println(key);
            System.out.println("\nNOMI VARIABILI + ENC VALUE\n");

            ArrayList<EncryptedOutput> encryptedOutputs = coutput.get(key);
            for (EncryptedOutput encryptedOutput : encryptedOutputs) {
                System.out.println(encryptedOutput.getVarName());
                ArrayList<EncValue> enc = encryptedOutput.getEncValue();
                System.out.println(enc.toString());
            }

        }

    }

}
