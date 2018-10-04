package com.net2plan.gui.plugins.networkDesign.openStack.image;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.image.v2.Image;

import java.util.List;

public class OpenStackImageV2 extends OpenStackNetworkElement
{

    private String imageId;
    private String imageName ;

    private Image image ;


    public static OpenStackImageV2 createFromAddImageV2 (OpenStackNet osn , Image image,OpenStackClient openStackClient)
    {
        final OpenStackImageV2 res = new OpenStackImageV2(osn,image,openStackClient);
        res.imageId= image.getId();
        res.imageName=image.getName();
        image.getArchitecture();
        image.getChecksum();
        image.getContainerFormat();
        image.getCreatedAt();
        image.getDirectUrl();
        image.getDiskFormat();
        image.getFile();
        image.getInstanceUuid();
        image.getIsProtected();
        image.getKernelId();
        image.getLocations();
        image.getMinDisk();
        image.getMinRam();
        image.getOsDistro();
        image.getOsVersion();
        image.getOwner();
        image.getRamdiskId();
        image.getSchema();
        image.getSelf();
        image.getSize();
        image.getStatus();
        image.getTags();
        image.getUpdatedAt();
        image.getVirtualSize();
        image.getVisibility();

        return res;
    }

    private OpenStackImageV2 (OpenStackNet osn , Image image,OpenStackClient openStackClient)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackImages,openStackClient);
        this.image = image;


    }

    @Override
    public String getId () { return this.imageId; }
    public String getName () { return this.imageName; }


    @Override
    public String get50CharactersDescription()
    {
        return "ImageV2: " + this.getId();
    }


}


