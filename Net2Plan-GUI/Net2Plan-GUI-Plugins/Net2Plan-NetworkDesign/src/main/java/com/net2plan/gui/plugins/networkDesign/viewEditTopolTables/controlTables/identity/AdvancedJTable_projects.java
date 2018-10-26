package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackProject;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.gui.plugins.utils.GeneralForm;

import java.util.*;

public class AdvancedJTable_projects extends AdvancedJTable_networkElement<OpenStackProject>
{
    public AdvancedJTable_projects(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.PROJECTS , true, openStackClient);
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

        res.add(new AjtRcMenu("Add project", e -> addProject(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove project", e -> getSelectedElements().forEach(n -> {

            removeProject(n);

        }), (a, b) -> b == 1, null));

       /* res.add(new AjtRcMenu("Change project's name", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Name",n,"");

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change project's parent ID", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Parent ID",n,"Select");

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change project's domain", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Domain",n,"Select");

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change project's description", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Description",n,"");

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change project's parents", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Parents",n,"");

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change project's subtree", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Subtree",n,"");

        }), (a, b) -> b ==1, null));

*/
        res.add(new AjtRcMenu("Refresh", e ->updateTab(), (a, b) -> b >=0, null));

        return res;

    }

    public void addProject(){
        Map<String,String> newList = new HashMap<>();
        newList.put("Name","");
        newList.put("Domain ID","Select");
        newList.put("Enable","Boolean");

       GeneralForm generalTableForm= new GeneralForm("Add project",newList,this.ajtType,this.openStackClient,this);
    }
    public void removeProject(OpenStackProject project){

        openStackClient.getOpenStackNetDelete().deleteOpenStackProject(project.getId());
        updateTab();
    }

}
