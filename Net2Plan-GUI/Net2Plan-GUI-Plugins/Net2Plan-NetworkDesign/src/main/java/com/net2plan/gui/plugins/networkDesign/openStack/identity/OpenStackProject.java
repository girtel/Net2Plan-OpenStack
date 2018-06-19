package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.Project;

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

    private Project osProject;

    public static OpenStackProject createFromAddProject (OpenStackNet osn ,Project project)
    {
        final OpenStackProject res = new OpenStackProject(osn,project);
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

    private OpenStackProject (OpenStackNet osn,Project project )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.openStackProjects);
        if(this.projectId != null)
            this.osProject = this.osn.getOSClientV3().identity().projects().get(this.projectId);
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
    public boolean isProjectEnabled () { return this.projectEnabled; }
    public Map<String,String> getProjectLinks () { return this.projectLinks; }

    public void setProjectName (String value) { this.osn.getOSClientV3().identity().projects().update(osProject.toBuilder().name(value).build());  }
    public void setProjectParentId (String value) { this.osn.getOSClientV3().identity().projects().update(osProject.toBuilder().parentId(value).build());  }
    public void setProjectDomainId (String value) { this.osn.getOSClientV3().identity().projects().update(osProject.toBuilder().domainId(value).build());  }
    public void setProjectDomain (Domain value) { this.osn.getOSClientV3().identity().projects().update(osProject.toBuilder().domain(value).build());  }
    public void setProjectDescription (String value) { this.osn.getOSClientV3().identity().projects().update(osProject.toBuilder().description(value).build());  }
    public void setProjectParents (String value) { this.osn.getOSClientV3().identity().projects().update(osProject.toBuilder().parents(value).build());  }
    public void setProjectSubtree (String value) { this.osn.getOSClientV3().identity().projects().update(osProject.toBuilder().subtree(value).build());  }
    public void setProjectEnabled (boolean value) { this.osn.getOSClientV3().identity().projects().update(osProject.toBuilder().enabled(value).build());  }



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

