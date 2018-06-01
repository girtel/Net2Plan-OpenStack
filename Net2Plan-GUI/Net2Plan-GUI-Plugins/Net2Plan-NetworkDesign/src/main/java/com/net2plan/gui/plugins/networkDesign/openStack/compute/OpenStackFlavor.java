package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.compute.Flavor;

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
    private Flavor osFlavor;

    public static OpenStackFlavor createFromAddFlavor (OpenStackNet osn , Flavor flavor)
    {
        final OpenStackFlavor res = new OpenStackFlavor(osn,flavor);
        res.flavorId= flavor.getId();
        res.flavorName=flavor.getName();
        res.flavorDisk=flavor.getDisk();
        res.flavorEphemeral=flavor.getEphemeral();
        res.flavorRam=flavor.getRam();
        res.flavorSwap=flavor.getSwap();
        res.flavorVcpus=flavor.getVcpus();
        res.flavorDisabled=flavor.isDisabled();
        res.flavorPublic=flavor.isPublic();
        res.flavorRxtxCap=flavor.getRxtxCap();
        res.flavorRxtxFactor=flavor.getRxtxFactor();
        res.flavorRxtxQuota=flavor.getRxtxQuota();
        return res;
    }

    private OpenStackFlavor (OpenStackNet osn ,Flavor flavor)
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.openStackFlavors);
        this.osFlavor = flavor;
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
        return "Flavor: " + this.getId();
    }


}
