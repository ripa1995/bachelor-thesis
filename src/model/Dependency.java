package model;

import java.util.ArrayList;

public class Dependency {

    private ArrayList<Item> init;
    private ArrayList<Item> exploit;

    public Dependency(){
        init = new ArrayList<Item>();
        exploit = new ArrayList<Item>();
    }

    public ArrayList<Item> getInit() {
        return init;
    }

    public void setInit(ArrayList<Item> init) {
        this.init = init;
    }

    public ArrayList<Item> getExploit() {
        return exploit;
    }

    public void setExploit(ArrayList<Item> exploit) {
        this.exploit = exploit;
    }

    public void addExploit(int _loc, String _T) {
        exploit.add(new Item(_loc, _T));
    }

    public void addInit(int _loc, String _T) {
        init.add(new Item(_loc, _T));
    }
}
