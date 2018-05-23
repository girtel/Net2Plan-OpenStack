package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackProject;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_projects extends AdvancedJTable_networkElement<OpenStackProject>
{
    public AdvancedJTable_projects(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.PROJECTS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackProject>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackProject>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackProject>(this, String.class, null, "ID", "Project ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, String.class, null, "Name", "Project Name", null, n -> n.getProjectName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, String.class, null, "Description", "Project description", null, n -> n.getProjectDescription(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, String.class, null, "Domain", "Project domain", null, n -> n.getProjectDomain(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, String.class, null, "Domain ID", "Project domain id", null, n -> n.getProjectDomainId(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, String.class, null, "Parents", "Project parents", null, n -> n.getProjectParents(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, String.class, null, "Parents ID", "Project parents id", null, n -> n.getProjectParentId(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, String.class, null, "Subtree", "Project Subtree", null, n -> n.getProjectSubtree(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, List.class, null, "Links", "Project links",
                null, n -> n.getProjectLinks(), AGTYPE.NOAGGREGATION, null, null));


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

