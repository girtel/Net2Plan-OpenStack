package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.compute.QuotaSet;
import org.openstack4j.model.compute.SimpleTenantUsage;

import java.math.BigDecimal;
import java.util.List;

public class OpenStackQuotasUsage extends OpenStackNetworkElement
{

    private String tenantId;
    private BigDecimal vcpusUsage;
    private BigDecimal ramUsage;
    private BigDecimal volumenGbUsage;

    private String project_id;
    private SimpleTenantUsage simpleTenantUsage;

    public static OpenStackQuotasUsage createFromAddQuotaUsage (OpenStackNet osn , SimpleTenantUsage simpleTenantUsage, OpenStackClient openStackClient, String project_id)
    {
        final OpenStackQuotasUsage res = new OpenStackQuotasUsage(osn,simpleTenantUsage,openStackClient,project_id);
        res.tenantId= simpleTenantUsage.toString();
        res.vcpusUsage=simpleTenantUsage.getTotalVcpusUsage();
        res.ramUsage=simpleTenantUsage.getTotalMemoryMbUsage();
        res.volumenGbUsage=simpleTenantUsage.getTotalLocalGbUsage();
        return res;
    }

    private OpenStackQuotasUsage(OpenStackNet osn , SimpleTenantUsage simpleTenantUsage, OpenStackClient openStackClient, String project_id)
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.openStackQuotasUsage,openStackClient);
        this.simpleTenantUsage = simpleTenantUsage;
        this.project_id = project_id;

    }

    @Override
    public String getId () { return this.tenantId; }
    public String getProject_id () { return this.project_id; }
    public BigDecimal getVcpusUsage () { return this.vcpusUsage; }
    public BigDecimal getRamUsage () { return this.ramUsage; }
    public BigDecimal getVolumenGbUsage () { return this.volumenGbUsage; }
    //public OpenStackClient getOpenStackClient(){return this.openStackClient;}

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

