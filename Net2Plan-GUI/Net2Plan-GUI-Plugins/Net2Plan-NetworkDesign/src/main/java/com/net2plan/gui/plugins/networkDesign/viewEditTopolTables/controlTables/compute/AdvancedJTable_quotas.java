package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackQuota;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackCredential;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_quotas extends AdvancedJTable_networkElement<OpenStackQuota>
{
    public AdvancedJTable_quotas(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.QUOTAS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackQuota>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackQuota>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackQuota>(this, String.class, null, "ID", "Credential ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuota>(this, Integer.class, null, "Project ID", "Credential project ID", null, n -> n.getQuotaSetCores(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuota>(this, Integer.class, null, "Project ID", "Credential project ID", null, n -> n.getQuotaSetFloatingIps(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuota>(this, Integer.class, null, "Project ID", "Credential project ID", null, n -> n.getQuotaSetFloatingIps(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuota>(this, Integer.class, null, "Project ID", "Credential project ID", null, n -> n.getQuotaSetGigabytes(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuota>(this, Integer.class, null, "Project ID", "Credential project ID", null, n -> n.getQuotaSetInjectedFileContentBytes(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuota>(this, Integer.class, null, "Project ID", "Credential project ID", null, n -> n.getQuotaSetInjectedFilePathBytes(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuota>(this, Integer.class, null, "Project ID", "Credential project ID", null, n -> n.getQuotaSetInjectedFiles(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuota>(this, Integer.class, null, "Project ID", "Credential project ID", null, n -> n.getQuotaSetInstances(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuota>(this, Integer.class, null, "Project ID", "Credential project ID", null, n -> n.getQuotaSetKeyPairs(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuota>(this, Integer.class, null, "Project ID", "Credential project ID", null, n -> n.getQuotaSetMetadataItems(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuota>(this, Integer.class, null, "Project ID", "Credential project ID", null, n -> n.getQuotaSetRam(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuota>(this, Integer.class, null, "Project ID", "Credential project ID", null, n -> n.getQuotaSetSecurityGroupRules(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuota>(this, Integer.class, null, "Project ID", "Credential project ID", null, n -> n.getQuotaSetSecurityGroups(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuota>(this, Integer.class, null, "Project ID", "Credential project ID", null, n -> n.getQuotaSetVolumes(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuota>(this, Object.class, null, " ", "", null, n -> n, AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();


        res.add(new AjtRcMenu("Change the user's description", e -> getSelectedElements().forEach(n -> {


        }), (a, b) -> b ==1, null));

        return res;

    }



}