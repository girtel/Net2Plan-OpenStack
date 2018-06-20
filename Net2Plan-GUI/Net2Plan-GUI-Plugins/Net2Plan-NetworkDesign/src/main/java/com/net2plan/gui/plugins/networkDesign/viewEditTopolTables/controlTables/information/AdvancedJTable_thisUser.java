package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.information;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.information.OpenStackInformationProject;
import com.net2plan.gui.plugins.networkDesign.openStack.information.OpenStackInformationUser;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_thisUser extends AdvancedJTable_networkElement<OpenStackInformationUser>
{
    public AdvancedJTable_thisUser(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.THISUSER , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackInformationUser>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackInformationUser>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackInformationUser>(this, Object.class, null, "ID", "User ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackInformationUser>(this, String.class, null, "Name", "User Name", null, n -> n.getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackInformationUser>(this, String.class, null, "Domain ID", "Domain ID", null, n -> n.getDomainId(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackInformationUser>(this, String.class, null, "Email", "User email",
                null, n -> n.getEmail(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackInformationUser>(this, String.class, null, "Description", "User description",
                null, n -> n.getDescription(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackInformationUser>(this, Boolean.class, null, "Enable", "User enable", null, n -> n.isUserEnable(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackInformationUser>(this, List.class, null, "Links", "User links", null, n -> n.getUserLinks(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add new user", e->addUser(), (a, b) -> true, null));



        res.add(new AjtRcMenu("Change the user's name", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Name",n,"");

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change the user's email", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Email",n,"");

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change the user's description", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Description",n,"");

        }), (a, b) -> b ==1, null));

        return res;

    }

    public void addUser() {
        List<String> newList = new ArrayList<>();
        newList.add("Name");
        //generalTableForm("Add user",newList);
    }





}
