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
    private Map<String,String>projectLinks;

    private Project osProject;

    public static OpenStackProject createFromAddProject (OpenStackNet osn , String projectId, String projectName, String projectParentId, String projectDomainId, Domain projectDomain, String projectDescription, String projectParents, String projectSubtree, boolean projectEnabled, Map<String,String> projectLinks)
    {
        final OpenStackProject res = new OpenStackProject(osn);
        res.projectId= projectId;
        res.projectName=projectName;
        res.projectParentId=projectParentId;
        res.projectDomainId=projectDomainId;
        res.projectDomain=projectDomain;
        res.projectDescription=projectDescription;
        res.projectParents=projectParents;
        res.projectSubtree=projectSubtree;
        res.projectEnabled=projectEnabled;
        res.projectLinks=projectLinks;
        return res;
    }

    private OpenStackProject (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osProjects);
        if(this.projectId != null)
            this.osProject = this.osn.getOs().identity().projects().get(this.projectId);
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

    public void setProjectName (String value) { this.osn.getOs().identity().projects().update(osProject.toBuilder().name(value).build());  }
    public void setProjectParentId (String value) { this.osn.getOs().identity().projects().update(osProject.toBuilder().parentId(value).build());  }
    public void setProjectDomainId (String value) { this.osn.getOs().identity().projects().update(osProject.toBuilder().domainId(value).build());  }
    public void setProjectDomain (Domain value) { this.osn.getOs().identity().projects().update(osProject.toBuilder().domain(value).build());  }
    public void setProjectDescription (String value) { this.osn.getOs().identity().projects().update(osProject.toBuilder().description(value).build());  }
    public void setProjectParents (String value) { this.osn.getOs().identity().projects().update(osProject.toBuilder().parents(value).build());  }
    public void setProjectSubtree (String value) { this.osn.getOs().identity().projects().update(osProject.toBuilder().subtree(value).build());  }
    public void setProjectEnabled (boolean value) { this.osn.getOs().identity().projects().update(osProject.toBuilder().enabled(value).build());  }



    @Override
    public String get50CharactersDescription()
    {
        return "Project" + this.getId();
    }


}

