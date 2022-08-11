package algorithms.search;

import java.io.Serializable;

/**
 * This class represents a state.(generalization of the Position class adapted to searching algo's)
 *  And practicing SOLID principles(we don't want this class to depend on Position class, so we use Object)
 */
public abstract class AState implements Serializable {
    protected AState predecessor;
    protected Object obj;
    protected int cost;

    public AState(Object o, int cost)
    {
        this.predecessor=null;
        this.obj=o;
        this.cost=cost;
    }

    public Object getData()
    {
        return  this.obj;
    }
    public void setData(Object data)
    {
        this.obj=data;
    }
    public int getCost(){return this.cost;}
    public void setCost(int cost){this.cost=cost;}
    public AState getPredecessor(){return this.predecessor;}
    public void setPredecessor(AState s){this.predecessor=s;}

    public String toString()
    {
        return this.obj.toString();
    }
}
