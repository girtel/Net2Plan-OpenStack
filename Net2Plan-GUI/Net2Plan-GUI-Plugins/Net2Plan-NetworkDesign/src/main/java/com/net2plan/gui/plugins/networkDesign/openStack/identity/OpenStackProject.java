package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.json.JSONObject;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.Project;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class OpenStackProject  extends OpenStackNetworkElement
{

    private String projectId;
    private String projectName;
    private String projectParentId;
    private String projectDomainId;
    private Domain projectDomain;
    private String projectDescription;
    private String projectParents;
    private String projectSubtree;
    private boolean projectEnabled;
    private Map<String,String> projectLinks;
    private Color color = Color.WHITE;
    private Project osProject;

    public static OpenStackProject createFromAddProject (OpenStackNet osn ,Project project , OpenStackClient openStackClient)
    {
        final OpenStackProject res = new OpenStackProject(osn,project,openStackClient);
        res.projectId= project.getId();
        res.projectName=project.getName();
        res.projectParentId=project.getParentId();
        res.projectDomainId=project.getDomainId();
        res.projectDomain=project.getDomain();
        res.projectDescription=project.getDescription();
        res.projectParents=project.getParents();
        res.projectSubtree=project.getSubtree();
        res.projectEnabled=project.isEnabled();
        res.projectLinks=project.getLinks();

        return res;
    }

    private OpenStackProject (OpenStackNet osn,Project project,OpenStackClient openStackClient )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackProjects,openStackClient);
        if(this.projectId != null)
            this.osProject = this.openStackClient.getClient().identity().projects().get(this.projectId);
    }

    @Override
    public String getId () { return this.projectId; }
    public String getProjectName () { return this.projectName; }
    public String getProjectParentId () { return this.projectParentId; }
    public String getProjectDomainId () { return this.projectDomainId; }
    public Domain getProjectDomain() { return this.projectDomain; }
    public String getProjectDescription () { return this.projectDescription; }
    public String getProjectParents () { return this.projectParents; }
    public String getProjectSubtree () { return this.projectSubtree; }
    public Color getColor () {return this.color;}
    public boolean isProjectEnabled () { return this.projectEnabled; }
    public Map<String,String> getProjectLinks () { return this.projectLinks; }

    public void setColor(Color color){
        this.color= color;
    }
    public void setProjectName (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().projects().update(osProject.toBuilder().name(jsonObject.getString("Name")).build());

    }catch(Exception ex){

    logPanel();
    System.out.println(ex.toString());

}
    }
    public void setProjectParentId (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().projects().update(osProject.toBuilder().parentId(jsonObject.getString("Parent ID")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void setProjectDomainId (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().projects().update(osProject.toBuilder().domainId(jsonObject.getString("Domain ID")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void setProjectDomain (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().projects().update(osProject.toBuilder().domain((Domain) jsonObject.get("Name")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }}
    public void setProjectDescription (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().projects().update(osProject.toBuilder().description(jsonObject.getString("Description")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void setProjectParents (JSONObject jsonObject) {

        try{
        this.openStackClient.getClient().identity().projects().update(osProject.toBuilder().parents(jsonObject.getString("Parents")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void setProjectSubtree (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().projects().update(osProject.toBuilder().subtree(jsonObject.getString("Subtree")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void setProjectEnabled (boolean value) {
        try{
        this.openStackClient.getClient().identity().projects().update(osProject.toBuilder().enabled(value).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }

    }



    @Override
    public String get50CharactersDescription()
    {
        String description = "Project/Tenant: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Project name " + this.getProjectName() +
                this.NEWLINE + "Parent ID " + this.getProjectParentId() +
                this.NEWLINE + "Domain ID " + this.getProjectDomainId() +
                this.NEWLINE + "Description " + this.getProjectDescription() +
                this.NEWLINE + "Subtree " + this.getProjectSubtree() +
                this.NEWLINE + "Enable " + this.isProjectEnabled() +
                this.NEWLINE + "Links" + this.NEWLINE;
        for(String key : this.getProjectLinks().keySet()) {
            description += key + " " + this.getProjectLinks().get(key) + NEWLINE;
        }
        return description;
    }


}

