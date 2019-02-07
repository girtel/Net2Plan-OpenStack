package com.net2plan.gui.plugins.networkDesign.openStack.blockstorage;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackUser;
import org.json.JSONObject;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.User;
import org.openstack4j.model.storage.block.Volume;

import java.util.List;
import java.util.Map;

public class OpenStackVolume extends OpenStackNetworkElement
{

    private String volumeId;
    private String volumeName ;
    private String volumeHost ;
    private String volumeSourceId ;
    private String volumeSnapShotId ;
    private String volumeProjectId ;
    private String volumeType;
    private Volume volume ;

    public static OpenStackVolume createFromAddVolume (OpenStackNet osn , Volume volume, OpenStackClient openStackClient)
    {
        final OpenStackVolume res = new OpenStackVolume(osn,volume,openStackClient);
        res.volumeId= volume.getId();
        res.volumeName=volume.getName();
        res.volumeType=volume.getVolumeType();

        return res;
    }

    private OpenStackVolume (OpenStackNet osn , Volume volume, OpenStackClient openStackClient)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackVolumes,openStackClient);
        this.volume = volume;


    }

    @Override
    public String getId () { return this.volumeId; }
    public String getName () { return this.volumeName; }
    public String getVolumeHost() {return this.volumeHost;}
    public String getVolumeSourceId () { return this.volumeSourceId; }
    public String getVolumeSnapShotId () { return this.volumeSnapShotId; }
    public String getVolumeProjectId () { return this.volumeProjectId; }
    public String getVolumeType () { return this.volumeType; }



    @Override
    public String get50CharactersDescription()
    {
                String description = "Volume: " ;

        return description;
    }
}


