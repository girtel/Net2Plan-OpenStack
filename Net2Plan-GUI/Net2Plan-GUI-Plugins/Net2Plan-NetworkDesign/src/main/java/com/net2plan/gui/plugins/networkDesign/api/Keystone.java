package com.net2plan.gui.plugins.networkDesign.api;

import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackGnocchiMeasure;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.Project;
import org.openstack4j.model.identity.v3.builder.ProjectBuilder;
import org.openstack4j.model.telemetry.Meter;
import org.openstack4j.model.telemetry.Resource;

import java.util.*;

public class Keystone extends Api {

    private final OSClient.OSClientV3 osClientV3;
    public final String url;

    public enum KeystoneOption {
        PROJECT("/projects")
        ;

        private final String tabName;
        private KeystoneOption(String tabName)
        {
            this.tabName = tabName;
        }
    }

    public Keystone(String url, OSClient.OSClientV3 osClientV3){
        this.url=url;
        this.osClientV3=osClientV3;
    }

    public List<Project> projectList(){

        final List<Project> projects = new ArrayList<>();

        Object responseObject = this.Get(url+KeystoneOption.PROJECT.tabName,osClientV3.getToken().getId());

        JSONArray jsonArray = new JSONObject(responseObject.toString()).getJSONArray("projects");
        //System.out.println("Projects  "+jsonArray);
        for(Object object: jsonArray){

            JSONObject jsonObject = (JSONObject) object;

            Project project = new Project() {
                @Override
                public String getId() {
                    return jsonObject.get("id").toString();
                }

                @Override
                public String getDomainId() {
                    return jsonObject.get("domain_id").toString();
                }

                @Override
                public Domain getDomain() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return jsonObject.get("description").toString();
                }

                @Override
                public String getName() {
                    return jsonObject.get("name").toString();
                }

                @Override
                public Map<String, String> getLinks() {
                    Map<String,String> links = new HashMap<>();
                    JSONObject jsonObject1 = (JSONObject)jsonObject.get("links");
                    links.put("self",jsonObject1.getString("self"));

                    return links;
                }

                @Override
                public String getParentId() {
                    return jsonObject.get("parent_id").toString();
                }

                @Override
                public String getSubtree() {
                    return jsonObject.get("domain_id").toString();
                }

                @Override
                public String getParents() {
                    return jsonObject.get("domain_id").toString();
                }

                @Override
                public boolean isEnabled() {
                    return jsonObject.getBoolean("enabled");
                }

                @Override
                public String getExtra(String s) {
                    return jsonObject.get("name").toString();
                }

                @Override
                public ProjectBuilder toBuilder() {
                    return null;
                }
            };


            projects.add(project);
        }
        return projects;
    }
    public Object createProject(String projectName,String projectDomainId, Boolean enabled){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",projectName);
        jsonObject.put("domain_id",projectDomainId);
        jsonObject.put("enabled",enabled);
        JSONObject project = new JSONObject();
        project.put("project",jsonObject);
        System.out.println("Project !!!!!!!!!!!!!!!!!!!!!!!!!!!1"+project);
        Object responseObject = this.Post(url+KeystoneOption.PROJECT.tabName,osClientV3.getToken().getId(),project);
System.out.println("OBJECCTTTT       "+ responseObject);
        return (Object)responseObject;
    }

}

