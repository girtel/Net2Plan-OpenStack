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
    private Image.Status imageStatus;
    private Date imageCreated;
    private Date imageUpdated;
    private Map<String,Object> imageMetaData;
    private Integer imageProgress;
    private Integer imageMinDisk;
    private Integer imageMinRam;
    private boolean imageSnapshot;
    private List<? extends Link>imageLinks;
    private Image osImage;

    public static OpenStackImage createFromAddImage (OpenStackNet osn , Image image)
    {
        final OpenStackImage res = new OpenStackImage(osn,image);
        res.imageId= image.getId();
        res.imageName=image.getName();
        res.imageSize=image.getSize();
        res.imageStatus=image.getStatus();
        res.imageCreated=image.getCreated();
        res.imageUpdated=image.getUpdated();
        res.imageMetaData=image.getMetaData();
        res.imageProgress=image.getProgress();
        res.imageMinDisk=image.getMinDisk();
        res.imageMinRam=image.getMinRam();
        res.imageSnapshot=image.isSnapshot();
        res.imageLinks=image.getLinks();
        return res;
    }

    private OpenStackImage (OpenStackNet osn,Image image )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.openStackImages);
        this.osImage = image;
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
        String description = "Image: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Name " + this.getImageName() +
                this.NEWLINE + "Created at " + this.getImageCreated() +
                this.NEWLINE + "Min Disk " + this.getImageMinDisk() +
                this.NEWLINE + "Min RAM " + this.getImageMinRam() +
                this.NEWLINE + "Progress " + this.getImageProgress() +
                this.NEWLINE + "Size " + this.getImageSize() +
                this.NEWLINE + "Status " + this.getImageStatus() +
                this.NEWLINE + "Update at " + this.getImageUpdated() +
                this.NEWLINE + "Snapshot " + this.isImageSnapshot();
               description += this.NEWLINE + "Links" + this.NEWLINE;
        for(String key : this.getImageMetaData().keySet()) {
            description += key + " " + this.getImageMetaData().get(key) + NEWLINE;
        }
        description +=this.NEWLINE + "Links" + this.NEWLINE;
        for(Link link : this.getImageLinks()) {
            description += link + " " +  NEWLINE;
        }
        return description;
    }


}
