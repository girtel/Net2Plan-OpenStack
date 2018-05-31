package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackUser;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_users extends AdvancedJTable_networkElement<OpenStackUser>
{
    public AdvancedJTable_users(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.USERS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackUser>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackUser>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackUser>(this, Object.class, null, "ID", "User ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackUser>(this, String.class, null, "Name", "User Name", null, n -> n.getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackUser>(this, String.class, null, "Domain ID", "Domain ID", null, n -> n.getDomainId(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackUser>(this, String.class, null, "Email", "User email",
                null, n -> n.getEmail(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackUser>(this, String.class, null, "Description", "User description",
                null, n -> n.getDescription(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackUser>(this, Boolean.class, null, "Enable", "User enable", null, n -> n.isUserEnable(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackUser>(this, List.class, null, "Links", "User links", null, n -> n.getUserLinks(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add new user", e->addUser(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove user", e -> getSelectedElements().forEach(n -> {

            removeUser(n);


        }), (a, b) -> b==b, null));

        res.add(new AjtRcMenu("Change the user's name", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Name",n);

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change the user's email", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Email",n);

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change the user's description", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Description",n);

        }), (a, b) -> b ==1, null));

        return res;

    }

    public void addUser() {
        List<String> newList = new ArrayList<>();
        newList.add("Name");
        generalTableForm("Add user",newList);
    }

    public void removeUser(OpenStackUser user){

        callback.getOpenStackNet().getOpenStackNetDelete().deleteOpenStackUser(user.getId());
        updateTab();
    }



}
