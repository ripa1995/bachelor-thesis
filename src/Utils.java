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
        if (s.contains("query")) {
            //è una query

            return true;
        };
        return false;
    }

    public static boolean isCallback(String s) {
        if (s.contains("callback")) {
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
}
