import java.util.ArrayList;

public class Dependency {

    private Item init;
    private ArrayList<Item> exploit;

    public Dependency(){
        init = new Item();
        exploit = new ArrayList<Item>();
    }

    public Item getInit() {
        return init;
    }

    public void setInit(Item init) {
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
        init = new Item(_loc, _T);
    }
}
