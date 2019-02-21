package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.compute.AbsoluteLimit;

import java.util.List;

public class OpenStackLimits extends OpenStackNetworkElement
{

    private String limitId;
    private Integer limitCores;
    private Integer limitCoresUsed;
    private Integer limitInstances;
    private Integer limitInstancesUsed;
    private Integer limitFloatingIp;
    private Integer limitFloatingIpUsed;
    private Integer limitVolumes;
    private Integer limitVolumesUsed;
    private Integer limitRam;
    private Integer limitRamUsed;
    private Integer limitKeypairs;
    private Integer limitKeypairsUsed;
    private Integer limitSecGroup;
    private Integer limitSecGroupUsed;

    private AbsoluteLimit absoluteLimit;

    public static OpenStackLimits createFromAddLimit (OpenStackNet osn , AbsoluteLimit absoluteLimit, OpenStackClient openStackClient)
    {
        final OpenStackLimits res = new OpenStackLimits(osn,absoluteLimit,openStackClient);
        res.limitId= absoluteLimit.toString();

        res.limitCores=absoluteLimit.getMaxTotalCores();
        res.limitCoresUsed=absoluteLimit.getTotalCoresUsed();

        res.limitFloatingIp=absoluteLimit.getMaxTotalFloatingIps();
        res.limitFloatingIpUsed=absoluteLimit.getTotalFloatingIpsUsed();


        res.limitVolumes=absoluteLimit.getMaxTotalVolumes();
        res.limitVolumesUsed=absoluteLimit.getTotalVolumesUsed();


        res.limitRam=absoluteLimit.getMaxTotalRAMSize();
        res.limitRamUsed=absoluteLimit.getTotalRAMUsed();

        res.limitInstances=absoluteLimit.getMaxTotalInstances();
        res.limitInstancesUsed=absoluteLimit.getTotalInstancesUsed();

        res.limitKeypairs=absoluteLimit.getMaxTotalKeypairs();
        res.limitKeypairsUsed=absoluteLimit.getTotalKeyPairsUsed();

        res.limitSecGroup=absoluteLimit.getMaxSecurityGroups();
        res.limitSecGroupUsed=absoluteLimit.getTotalSecurityGroupsUsed();



        return res;
    }

    private OpenStackLimits (OpenStackNet osn ,AbsoluteLimit absoluteLimit,OpenStackClient openStackClient)
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.openStackLimits,openStackClient);
        this.absoluteLimit = absoluteLimit;

    }

    @Override
    public String getId () { return this.limitId; }
    public String getName () { return this.limitId; }
    public Integer getLimitCores () { return this.limitCores; }
    public Integer getLimitCoresUsed () { return this.limitCoresUsed; }
    public Integer getLimitRam () { return this.limitRam; }
    public Integer getLimitRamUsed () { return this.limitRamUsed; }
    public Integer getLimitInstances () { return this.limitInstances; }
    public Integer getLimitInstancesUsed () { return this.limitInstancesUsed; }
    public String getUrl(){
        return this.openStackClient.os_auth_url;
    }
    public Integer getLimitVolumes () { return this.limitVolumes; }
    public Integer getLimitVolumesUsed () { return this.limitVolumesUsed; }
  //  public OpenStackClient getOpenStackClient(){return this.openStackClient;}
    @Override
    public String get50CharactersDescription()
    {
        /*String description = "Flavor: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Name " + this.getFlavorName() +
                this.NEWLINE + "Disk " + this.getFlavorDisk() +
                this.NEWLINE + "Ephemeral " + this.getFlavorEphemeral() +
                this.NEWLINE + "RAM " + this.getFlavorRam() +
                this.NEWLINE + "RxTx Capacity " + this.getFlavorRxtxCap() +
                this.NEWLINE + "RxTx Factor " + this.getFlavorRxtxFactor() +
                this.NEWLINE + "RxTx Quota " + this.getFlavorRxtxQuota() +
                this.NEWLINE + "Swap " + this.getFlavorSwap() +
                this.NEWLINE + "VCPUs " + this.getFlavorVcpus() +
                this.NEWLINE + "Disabled " + this.isFlavorDisabled() +
                this.NEWLINE + "Public " + this.isFlavorPublic() ;*/
        return "limit";
    }


}
