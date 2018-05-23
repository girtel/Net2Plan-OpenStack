package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackCredential;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_credentials extends AdvancedJTable_networkElement<OpenStackCredential>
{
    public AdvancedJTable_credentials(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.CREDENTIALS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackCredential>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackCredential>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackCredential>(this, String.class, null, "ID", "Credential ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackCredential>(this, String.class, null, "Project ID", "Credential project ID", null, n -> n.getCredentialProjectId(), AGTYPE.NOAGGREGATION, null, null));
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
    {final List<AjtRcMenu> res = new ArrayList<>();


        res.add(new AjtRcMenu("Change the user's description", e -> getSelectedElements().forEach(n -> {


        }), (a, b) -> b ==1, null));

        return res;

    }



}
