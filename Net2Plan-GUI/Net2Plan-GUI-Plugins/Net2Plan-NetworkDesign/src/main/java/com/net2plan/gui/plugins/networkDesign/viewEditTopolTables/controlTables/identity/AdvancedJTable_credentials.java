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
        res.add(new AjtColumnInfo<OpenStackCredential>(this, String.class, null, "ID", "Credential ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackCredential>(this, String.class, null, "User ID", "Credential user ID", null, n -> n.getCredentialUserId(), AGTYPE.NOAGGREGATION, null, null));
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
    {
        final List<AjtRcMenu> res = new ArrayList<>();

      /*  res.add(new AjtRcMenu("Add credential", e -> addCredential(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove credential", e -> getSelectedElements().forEach(n -> {

            removeCredential(n);

        }), (a, b) -> b == 1, null));


        res.add(new AjtRcMenu("Change credential's user id", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("User ID",n,"Select");

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change credential's tenant/project id", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Tenant ID",n,"Select");

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change credential's type", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Type",n,"");

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change credential's blob", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Blob",n,"");

        }), (a, b) -> b ==1, null));

        */
        res.add(new AjtRcMenu("Refresh", e ->updateTab(), (a, b) -> b >=0, null));

        return res;

    }



    public void addCredential(){

        Map<String,String> newList = new HashMap<>();
        newList.put("User ID","Select");
        newList.put("Tenant ID","Select");
        newList.put("Blob","Select");
        newList.put("Type","Select");
        //generalTableForm("Add user",newList);

    }
    public void removeCredential(OpenStackCredential credential){

        openStackClient.getOpenStackNetDelete().deleteOpenStackCredential(credential.getId());
        updateTab();
    }

}
