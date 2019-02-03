package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackCredential;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.*;

public class AdvancedJTable_credentials extends AdvancedJTable_networkElement<OpenStackCredential>
{
    public AdvancedJTable_credentials(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.CREDENTIALS , true,openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackCredential>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackCredential>> res = new LinkedList<>();
        //res.add(new AjtColumnInfo<OpenStackCredential>(this, String.class, null, "ID", "Credential ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackCredential>(this, String.class, null, "User ID", "Credential user ID", null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getCredentialUserId()), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackCredential>(this, String.class, null, "Project ID", "Credential project ID", null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getCredentialProjectId()), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackCredential>(this, String.class, null, "Type", "Credential type", null, n -> n.getCredentialType(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackCredential>(this, String.class, null, "Blob", "Credential blob",
                null, n -> n.getCredentialBlob(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackCredential>(this, String.class, null, "Links", "Credentials links",
                null, n -> n.getCredentialLinks(), AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();

        return res;

    }


}
