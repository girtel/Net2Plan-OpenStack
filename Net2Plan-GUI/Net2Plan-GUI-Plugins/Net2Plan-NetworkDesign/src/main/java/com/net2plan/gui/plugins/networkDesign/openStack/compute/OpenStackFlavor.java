package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;

import java.util.List;

public class OpenStackFlavor  extends OpenStackNetworkElement
{

    private String flavorId;
    private String flavorName;
    private Integer flavorDisk;
    private Integer flavorEphemeral;
    private Integer flavorRam;
    private Integer flavorSwap;
    private Integer flavorVcpus;
    private boolean flavorDisabled;
    private boolean flavorPublic;
    private Integer flavorRxtxCap;
    private float flavorRxtxFactor;
    private Integer flavorRxtxQuota;

    public static OpenStackFlavor createFromAddFlavor (OpenStackNet osn , String flavorId, String flavorName, Integer flavorDisk, Integer flavorEphemeral, Integer flavorRam, Integer flavorSwap, Integer flavorVcpus, boolean flavorDisabled, boolean flavorPublic, Integer flavorRxtxCap, float flavorRxtxFactor, Integer flavorRxtxQuota)
    {
        final OpenStackFlavor res = new OpenStackFlavor(osn);
        res.flavorId= flavorId;
        res.flavorName=flavorName;
        res.flavorDisk=flavorDisk;
        res.flavorEphemeral=flavorEphemeral;
        res.flavorRam=flavorRam;
        res.flavorSwap=flavorSwap;
        res.flavorVcpus=flavorVcpus;
        res.flavorDisabled=flavorDisabled;
        res.flavorPublic=flavorPublic;
        res.flavorRxtxCap=flavorRxtxCap;
        res.flavorRxtxFactor=flavorRxtxFactor;
        res.flavorRxtxQuota=flavorRxtxQuota;
        return res;
    }

    private OpenStackFlavor (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osFlavours);
    }

    @Override
    public String getId () { return this.flavorId; }
    public String getFlavorName () { return this.flavorName; }
    public Integer getFlavorDisk () { return this.flavorDisk; }
    public Integer getFlavorEphemeral () { return this.flavorEphemeral; }
    public Integer getFlavorRam () { return this.flavorRam; }
    public Integer getFlavorSwap () { return this.flavorSwap; }
    public Integer getFlavorVcpus () { return this.flavorVcpus; }
    public boolean isFlavorDisabled () { return this.flavorDisabled; }
    public boolean isFlavorPublic () { return this.flavorPublic; }
    public Integer getFlavorRxtxCap () { return this.flavorRxtxCap; }
    public float getFlavorRxtxFactor () { return this.flavorRxtxFactor; }
    public Integer getFlavorRxtxQuota () { return this.flavorRxtxQuota; }


    @Override
    public String get50CharactersDescription()
    {
        return "Credentials" + this.getId();
    }


}
