package model;

public class DoubleEncValue extends EncValue{
    String homomorphicKey;

    public DoubleEncValue(String varName, String key, String homomorphicKey) {
        super(varName, key);
        this.homomorphicKey = homomorphicKey;
    }

    public String getHomomorphicKey() {
        return homomorphicKey;
    }

    public void setHomomorphicKey(String homomorphicKey) {
        this.homomorphicKey = homomorphicKey;
    }

    @Override
    public String toString() {
        return "(("+this.getVarName()+","+this.getHomomorphicKey()+"),"+this.getKey()+")";
    }
}
