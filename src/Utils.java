import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Utils {
    public static boolean isType(String s){
        if (s.contains("fixed")) {
            //è un fixed point number

            return true;
        };
        if (s.contains("int")) {
            //è un int number

            return true;
        };
        if (s.contains("address")) {
            //è un address

            return true;
        };
        if (s.contains("byte")) {
            //è un array byte

            return true;
        };
        if (s.contains("string")) {
            //è un string

            return true;
        };
        return false;
    }

    public static boolean isEvent(String s) {
        if (s.contains("event")) {
            //è un event

            return true;
        };
        return false;
    }

    public static boolean isFunction(String s) {
        if (s.contains("function")) {
            //è una funzione

            return true;
        };
        return false;
    }

    public static boolean isQuery(String s) {
        if (s.startsWith("query")) {
            //è una query

            return true;
        };
        return false;
    }

    public static boolean isCallback(String s) {
        if (s.startsWith("_callback")) {
            //è una callback

            return true;
        };
        return false;
    }

    public static boolean isStoreKeyword(String s) {
        if (s.equalsIgnoreCase("memory")||s.equalsIgnoreCase("storage")) {
            //è una keyword

            return true;
        };
        return false;
    }

    public static String removeLastChar(String s) {
        return s.substring(0, s.length()-1);
    }

    public static String extractMethodName(String s) {
        String[] token = s.trim().split("\\(");
        return token[0];
    }

    public static ArrayList<String> extractParam(String[] s) {
        ArrayList<String> arrayList = new ArrayList<String>();
        boolean flag = true;
        for(String string: s) {
            String[] token = string.trim().split("\\(");
            for (String string2 : token) {
                if (string2.equals("function")) {
                    continue;
                }
                if (string2.startsWith("_callback")) {
                    continue;
                }
                if (string2.startsWith("query")) {
                    continue;
                }
                if (string2.endsWith(",")) {
                    string2 = removeLastChar(string2);
                } else if (string2.endsWith(")")) {
                    arrayList.add(removeLastChar(string2));
                    flag = false;
                    break;
                } else if (string2.endsWith("{")) {
                    arrayList.add(string2.substring(0, string2.length()-2));
                    flag = false;
                    break;
                }
                arrayList.add(string2);
            }
            if (!flag) {
                break;
            }
        }
        System.out.println(arrayList.toString());
        return arrayList;
    }
}
