package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.QuotaSet;

import java.util.List;

public class OpenStackQuotas extends OpenStackNetworkElement
{

    private String quotaId;
    private Integer quotaCores;
    private Integer quotaFloatingIps;
    private Integer quotaGigabytes;
    private Integer quotaRam;
    private Integer quotaInjectedFileContentBytes;
    private Integer quotaInjectedFilePathBytes;
    private Integer quotaInjectedFiles;
    private Integer quotaInstances;
    private Integer quotaKeypairs;
    private Integer quotaMetadataItems;
    private Integer quotaSecurityGroupRules;
    private Integer quotaSecurityGroup;
    private Integer quotaVolumes;

    private String project_id;
    private QuotaSet quotaSet;

    public static OpenStackQuotas createFromAddQuota (OpenStackNet osn , QuotaSet quotaSet, OpenStackClient openStackClient,String project_id)
    {
        final OpenStackQuotas res = new OpenStackQuotas(osn,quotaSet,openStackClient,project_id);
        res.quotaId= quotaSet.getId()+"quota";
        res.quotaCores=quotaSet.getCores();
        res.quotaFloatingIps=quotaSet.getFloatingIps();
        res.quotaGigabytes=quotaSet.getGigabytes();
        res.quotaRam=quotaSet.getRam();
        res.quotaInjectedFileContentBytes=quotaSet.getInjectedFileContentBytes();
        res.quotaInjectedFilePathBytes=quotaSet.getInjectedFilePathBytes();
        res.quotaInjectedFiles=quotaSet.getInjectedFiles();
        res.quotaInstances=quotaSet.getInstances();
        res.quotaKeypairs=quotaSet.getKeyPairs();
        res.quotaMetadataItems=quotaSet.getMetadataItems();
        res.quotaSecurityGroupRules=quotaSet.getSecurityGroupRules();
        res.quotaSecurityGroup=quotaSet.getSecurityGroups();
        res.quotaVolumes=quotaSet.getVolumes();
        return res;
    }

    private OpenStackQuotas (OpenStackNet osn ,QuotaSet quotaSet,OpenStackClient openStackClient,String project_id)
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.openStackQuotas,openStackClient);
        this.quotaSet = quotaSet;
        this.project_id = project_id;

    }

    @Override
    public String getId () { return this.quotaId; }
    public String getProject_id () { return this.project_id; }
    public Integer getQuotaCores () { return this.quotaCores; }
    public Integer getQuotaFloatingIps () { return this.quotaFloatingIps; }
    public Integer getQuotaRam () { return this.quotaRam; }
    public Integer getQuotaInstances () { return this.quotaInstances; }
    public Integer getQuotaKeypairs () { return this.quotaKeypairs; }
    public Integer getQuotaSecurityGroup () { return this.quotaSecurityGroup; }
    public Integer getQuotaVolumes () { return this.quotaVolumes; }
   // public OpenStackClient getOpenStackClient(){return this.openStackClient;}
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
        return "quota";
    }


}

