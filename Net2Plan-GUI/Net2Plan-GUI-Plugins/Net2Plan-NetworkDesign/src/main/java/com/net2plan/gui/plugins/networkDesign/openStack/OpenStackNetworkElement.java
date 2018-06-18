package com.net2plan.gui.plugins.networkDesign.openStack;

import com.net2plan.interfaces.networkDesign.Net2PlanException;
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.interfaces.networkDesign.NetworkElement;

import javax.swing.*;
import java.util.List;

/**
 *
 * @author Manuel
 */
public abstract class OpenStackNetworkElement implements Comparable<OpenStackNetworkElement>
{
    protected final OpenStackNet osn;
    protected final NetPlan np;
    protected final NetworkElement npNe;
    protected final List<? extends OpenStackNetworkElement> indexedList;
    protected Integer osnIndex;
    public final String NEWLINE = String.format("%n");
    protected OpenStackNetworkElement (OpenStackNet osn , NetworkElement npNe , List<OpenStackNetworkElement> indexedList)
    {
        assert npNe != null;
        assert npNe.getNetPlan() != null;
        assert npNe.getNetPlan() == osn.getNetPlan();
        this.npNe = npNe;
        this.osn = osn;
        this.np = osn.getNetPlan();
        this.indexedList = indexedList;
        this.osnIndex = indexedList.size();
        indexedList.add(this);
    }


    /** Sets the description of this element
     * @param description
     */
    public final void setDescription (String description) { npNe.getNetPlan().setNetworkDescription(description); } // CHECK USE

    /**
     * <p>Returns the unique identifier</p>
     * @return The unique id
     * @since 0.4.0
     */
    final public long getInternalId () { return npNe.getId(); }

    /**
     * <p>Returns the index</p>
     * @return The index
     */
    public int getOpenStackIndex () { return this.osnIndex; }


    public final OpenStackNet getOpenStackNet () { return this.osn; }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((npNe == null) ? 0 : npNe.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        OpenStackNetworkElement other = (OpenStackNetworkElement) obj;
        if (npNe == null)
        {
            if (other.npNe != null) return false;
        } else if (!npNe.equals(other.npNe)) return false;
        return true;
    }

    public abstract String get50CharactersDescription ();

    public abstract String getId ();


    @Override
    public final int compareTo(OpenStackNetworkElement o)
    {
        if (this.equals(o)) return 0;
        if (o == null) throw new NullPointerException ();
        if (this.osn != o.osn) throw new Net2PlanException("Different OSN!");
        return Long.compare(this.getInternalId(), o.getInternalId());
    }

    public  void logPanel(){
        JOptionPane.showMessageDialog(null, "Ups! One problem ocurred. Show console");
    }
}

