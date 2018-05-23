package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;

import java.util.List;

public class OpenStackQuota  extends OpenStackNetworkElement
{

    private String quotaSetId;
    private Integer quotaSetCores;
    private Integer quotaSetFloatingIps;
    private Integer quotaSetGigabytes;
    private Integer quotaSetKeyPairs;
    private Integer quotaSetRam;
    private Integer quotaSetInstances;
    private Integer quotaSetVolumes;
    private Integer quotaSetSecurityGroups;
    private Integer quotaSetSecurityGroupRules;
    private Integer quotaSetMetadataItems;
    private Integer quotaSetInjectedFileContentBytes;
    private Integer quotaSetInjectedFilePathBytes;
    private Integer quotaSetInjectedFiles;

    public static OpenStackQuota createFromAddQuota (OpenStackNet osn , String quotaSetId, Integer quotaSetCores, Integer quotaSetFloatingIps, Integer quotaSetGigabytes, Integer quotaSetKeyPairs, Integer quotaSetRam, Integer quotaSetInstances, Integer quotaSetVolumes, Integer quotaSetSecurityGroups, Integer quotaSetSecurityGroupRules, Integer quotaSetMetadataItems, Integer quotaSetInjectedFileContentBytes, Integer quotaSetInjectedFilePathBytes, Integer quotaSetInjectedFiles)
    {
        final OpenStackQuota res = new OpenStackQuota(osn);
        res.quotaSetId= quotaSetId;
        res.quotaSetCores=quotaSetCores;
        res.quotaSetFloatingIps=quotaSetFloatingIps;
        res.quotaSetGigabytes=quotaSetGigabytes;
        res.quotaSetKeyPairs=quotaSetKeyPairs;
        res.quotaSetRam=quotaSetRam;
        res.quotaSetInstances= quotaSetInstances;
        res.quotaSetVolumes=quotaSetVolumes;
        res.quotaSetSecurityGroups=quotaSetSecurityGroups;
        res.quotaSetSecurityGroupRules=quotaSetSecurityGroupRules;
        res.quotaSetMetadataItems=quotaSetMetadataItems;
        res.quotaSetInjectedFileContentBytes=quotaSetInjectedFileContentBytes;
        res.quotaSetInjectedFilePathBytes=quotaSetInjectedFilePathBytes;
        res.quotaSetInjectedFiles=quotaSetInjectedFiles;
        return res;
    }

    private OpenStackQuota (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osQuotas);
    }

    @Override
    public String getId () { return this.quotaSetId; }
    public Integer getQuotaSetCores () { return this.quotaSetCores; }
    public Integer getQuotaSetFloatingIps () { return this.quotaSetFloatingIps; }
    public Integer getQuotaSetGigabytes () { return this.quotaSetGigabytes; }
    public Integer getQuotaSetKeyPairs () { return this.quotaSetKeyPairs; }
    public Integer getQuotaSetRam () { return this.quotaSetRam; }

    public Integer getQuotaSetInstances () { return this.quotaSetInstances; }
    public Integer getQuotaSetVolumes () { return this.quotaSetVolumes; }
    public Integer getQuotaSetSecurityGroups () { return this.quotaSetSecurityGroups; }
    public Integer getQuotaSetSecurityGroupRules () { return this.quotaSetSecurityGroupRules; }
    public Integer getQuotaSetMetadataItems () { return this.quotaSetMetadataItems; }


    public Integer getQuotaSetInjectedFileContentBytes () { return this.quotaSetInjectedFileContentBytes; }
    public Integer getQuotaSetInjectedFilePathBytes () { return this.quotaSetInjectedFilePathBytes; }
    public Integer getQuotaSetInjectedFiles () { return this.quotaSetInjectedFiles; }


    @Override
    public String get50CharactersDescription()
    {
        return "Credentials" + this.getId();
    }


}
