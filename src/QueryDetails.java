import java.util.ArrayList;

public class QueryDetails {
    private int decLine;
    private ArrayList<Integer> invocations;
    private int callbackLine;

    public QueryDetails() {
        this.decLine = 0;
        this.invocations = new ArrayList<Integer>();
        this.callbackLine = 0;
    }

    public QueryDetails(int decLine, ArrayList<Integer> invocations, int callbackLine) {
        this.decLine = decLine;
        this.invocations = invocations;
        this.callbackLine = callbackLine;
    }

    public void addInvocationLine(int invocationLine) {
        this.invocations.add(invocationLine);
    }

    public int getDecLine() {
        return decLine;
    }

    public void setDecLine(int decLine) {
        this.decLine = decLine;
    }

    public ArrayList<Integer> getInvocations() {
        return invocations;
    }

    public void setInvocations(ArrayList<Integer> invocations) {
        this.invocations = invocations;
    }

    public int getCallbackLine() {
        return callbackLine;
    }

    public void setCallbackLine(int callbackLine) {
        this.callbackLine = callbackLine;
    }

    @Override
    public String toString() {
        String s = "DecLine: " + decLine + "\nInvocations: ";
        for(int i:invocations) {
            s = s + i + " ";
        }
        s = s + "\nCallBackLine: " + callbackLine;
        return s;
    }
}
