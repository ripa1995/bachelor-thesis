import java.util.ArrayList;

public class Dependency {

    private class Item{
        String loc;
        String T;

        public String getLoc() {
            return loc;
        }

        public void setLoc(String loc) {
            this.loc = loc;
        }

        public String getT() {
            return T;
        }

        public void setT(String t) {
            T = t;
        }
    }

    private Item init;
    private ArrayList<Item> exploit;

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
}
