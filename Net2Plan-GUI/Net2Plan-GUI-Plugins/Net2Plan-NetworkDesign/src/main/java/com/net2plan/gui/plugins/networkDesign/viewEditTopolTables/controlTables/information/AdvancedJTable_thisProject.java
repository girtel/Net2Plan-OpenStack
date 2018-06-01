package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.information;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.information.OpenStackInformationProject;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_thisProject extends AdvancedJTable_networkElement<OpenStackInformationProject>
{
    public AdvancedJTable_thisProject(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.THISPROJECT , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackInformationProject>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackInformationProject>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackInformationProject>(this, String.class, null, "ID", "Project ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackInformationProject>(this, String.class, null, "Name", "Project Name", null, n -> n.getProjectName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackInformationProject>(this, String.class, null, "Description", "Project description", null, n -> n.getProjectDescription(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackInformationProject>(this, String.class, null, "Domain", "Project domain", null, n -> n.getProjectDomain(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackInformationProject>(this, String.class, null, "Domain ID", "Project domain id", null, n -> n.getProjectDomainId(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackInformationProject>(this, String.class, null, "Parents", "Project parents", null, n -> n.getProjectParents(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackInformationProject>(this, String.class, null, "Parents ID", "Project parents id", null, n -> n.getProjectParentId(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackInformationProject>(this, String.class, null, "Subtree", "Project Subtree", null, n -> n.getProjectSubtree(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackInformationProject>(this, List.class, null, "Links", "Project links",
                null, n -> n.getProjectLinks(), AGTYPE.NOAGGREGATION, null, null));



        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add project", e -> addProject(), (a, b) -> true, null));


        res.add(new AjtRcMenu("Change project's name", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Name",n);

        }), (a, b) -> b ==1, null));


        return res;

    }

    public void addProject(){

        List<String> newList = new ArrayList<>();
        newList.add("Name");
        newList.add("Domain ID");
        generalTableForm("Add project",newList);
    }


}
