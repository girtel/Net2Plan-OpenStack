package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.common.Link;
import org.openstack4j.model.compute.Image;
import org.openstack4j.model.compute.Server;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class OpenStackImage  extends OpenStackNetworkElement
{

    private String imageId;
    private String imageName;
    private long imageSize;
    private org.openstack4j.model.compute.Image.Status imageStatus;
    private Date imageCreated;
    private Date imageUpdated;
    private Map<String,Object> imageMetaData;
    private Integer imageProgress;
    private Integer imageMinDisk;
    private Integer imageMinRam;
    private boolean imageSnapshot;
    private List<? extends Link>imageLinks;

    public static OpenStackImage createFromAddImage (OpenStackNet osn , String imageId, String imageName, long imageSize, org.openstack4j.model.compute.Image.Status imageStatus, Date imageCreated, Date imageUpdated, Map<String,Object> imageMetaData, Integer imageProgress, Integer imageMinDisk, Integer imageMinRam, boolean imageSnapshot, List<? extends Link> imageLinks)
    {
        final OpenStackImage res = new OpenStackImage(osn);
        res.imageId= imageId;
        res.imageName=imageName;
        res.imageSize=imageSize;
        res.imageStatus=imageStatus;
        res.imageCreated=imageCreated;
        res.imageUpdated=imageUpdated;
        res.imageMetaData=imageMetaData;
        res.imageProgress=imageProgress;
        res.imageMinDisk=imageMinDisk;
        res.imageMinRam=imageMinRam;
        res.imageSnapshot=imageSnapshot;
        res.imageLinks=imageLinks;
        return res;
    }

    private OpenStackImage (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osImages);
    }

    @Override
    public String getId () { return this.imageId; }
    public String getImageName () { return this.imageName; }
    public long getImageSize () { return this.imageSize; }
    public Image.Status getImageStatus () { return this.imageStatus; }
    public Date getImageCreated () { return this.imageCreated; }
    public Date getImageUpdated () { return this.imageUpdated; }

    public Map<String,Object> getImageMetaData () { return this.imageMetaData; }
    public Integer getImageProgress () { return this.imageProgress; }
    public Integer getImageMinDisk () { return this.imageMinDisk; }
    public Integer getImageMinRam () { return this.imageMinRam; }
    public boolean isImageSnapshot () { return this.imageSnapshot; }

    public List<? extends Link> getImageLinks () { return this.imageLinks; }


    @Override
    public String get50CharactersDescription()
    {
        return "Image" + this.getId();
    }


}
