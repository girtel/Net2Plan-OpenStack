package com.net2plan.gui.plugins.networkDesign.api;

import com.sun.codemodel.internal.JJavaName;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.Project;
import org.openstack4j.model.identity.v3.builder.ProjectBuilder;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeAttachment;
import org.openstack4j.model.storage.block.VolumeSnapshot;
import org.openstack4j.model.storage.block.builder.VolumeBuilder;
import org.openstack4j.model.storage.block.builder.VolumeSnapshotBuilder;

import java.util.*;

public class Cinder  extends Api
{

    private final OSClient.OSClientV3 osClientV3;
    public final String url;

    public enum CinderOption
    {
        VOLUMES("/volumes/detail"),
        SNAPSHOTS("/snapshots/detail")
        ;

        private final String tabName;
        private CinderOption(String tabName)
        {
            this.tabName = tabName;
        }
    }

    public Cinder(String url, OSClient.OSClientV3 osClientV3)
    {
        this.url=url.replace("%(tenant_id)s","");
        this.osClientV3=osClientV3;
    }

    public List<Volume> volumeList(){

        final List<Volume> volumes = new ArrayList<>();

        Object responseObject = this.Get(url+osClientV3.getToken().getProject().getId()+CinderOption.VOLUMES.tabName,osClientV3.getToken().getId());

        JSONArray jsonArray = new JSONObject(responseObject.toString()).getJSONArray("volumes");
        //System.out.println("Projects  "+jsonArray);
        for(Object object: jsonArray){

            JSONObject jsonObject = (JSONObject) object;

            System.out.println("VOLUME" + jsonObject);
            Volume volume = new Volume() {
                @Override
                public VolumeBuilder toBuilder() {
                    return null;
                }

                @Override
                public String getId() {
                    return jsonObject.getString("id");
                }

                @Override
                public String getName() {
                    return jsonObject.getString("name");
                }

                @Override
                public String getDisplayName() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return null;
                }

                @Override
                public String getDisplayDescription() {
                    return null;
                }

                @Override
                public Status getStatus() {
                    return null;
                }

                @Override
                public int getSize() {
                    return 0;
                }

                @Override
                public String getZone() {
                    return null;
                }

                @Override
                public Date getCreated() {
                    return null;
                }

                @Override
                public String getVolumeType() {
                    return jsonObject.getString("volume_type");
                }

                @Override
                public String getSnapshotId() {
                    return null;
                }

                @Override
                public String getImageRef() {
                    return null;
                }

                @Override
                public Boolean multiattach() {
                    return null;
                }

                @Override
                public String getSourceVolid() {
                    return null;
                }

                @Override
                public Map<String, String> getMetaData() {
                    return null;
                }

                @Override
                public List<? extends VolumeAttachment> getAttachments() {
                    return null;
                }

                @Override
                public MigrationStatus getMigrateStatus() {
                    return null;
                }

                @Override
                public String getTenantId() {
                    return null;
                }

                @Override
                public boolean bootable() {
                    return false;
                }

                @Override
                public boolean encrypted() {
                    return false;
                }

                @Override
                public String host() {
                    return null;
                }
            };


            volumes.add(volume);
        }
        return volumes;
    }
    public List<VolumeSnapshot> snapshotsList(){

        final List<VolumeSnapshot> volumes = new ArrayList<>();

        Object responseObject = this.Get(url+osClientV3.getToken().getProject().getId()+CinderOption.SNAPSHOTS.tabName,osClientV3.getToken().getId());

        JSONArray jsonArray = new JSONObject(responseObject.toString()).getJSONArray("snapshots");
        //System.out.println("Projects  "+jsonArray);
        for(Object object: jsonArray){

            JSONObject jsonObject = (JSONObject) object;

            System.out.println("SNAPSHOT" + jsonObject);
            VolumeSnapshot volume = new VolumeSnapshot() {
                @Override
                public VolumeSnapshotBuilder toBuilder() {
                    return null;
                }

                @Override
                public String getId() {
                    return null;
                }

                @Override
                public String getName() {
                    return null;
                }

                @Override
                public String getDisplayName() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return null;
                }

                @Override
                public String getDisplayDescription() {
                    return null;
                }

                @Override
                public String getVolumeId() {
                    return null;
                }

                @Override
                public Volume.Status getStatus() {
                    return null;
                }

                @Override
                public int getSize() {
                    return 0;
                }

                @Override
                public Date getCreated() {
                    return null;
                }

                @Override
                public Map<String, String> getMetaData() {
                    return null;
                }
            };


            volumes.add(volume);
        }
        return volumes;
    }


}

