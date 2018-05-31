package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.identity.v3.Region;

import java.util.List;

public class OpenStackRegion  extends OpenStackNetworkElement
{

    private String regionId;
    private String regionDescription;
    private String regionParentRegionId;

    private Region osRegion;

    public static OpenStackRegion createFromAddRegion (OpenStackNet osn ,Region region)
    {
        final OpenStackRegion res = new OpenStackRegion(osn,region);
        res.regionId= region.getId();
        res.regionDescription=region.getDescription();
        res.regionParentRegionId=region.getParentRegionId();
        return res;
    }

    private OpenStackRegion (OpenStackNet osn,Region region )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.openStackRegions);
        this.osRegion = region;
    }

    @Override
    public String getId () { return this.regionId; }
    public String getRegionDescription () { return this.regionDescription; }
    public String getRegionParentRegionId () { return this.regionParentRegionId; }

    public void setRegionDescription (String value) { this.osn.getOSClientV3().identity().regions().update(osRegion.toBuilder().description(value).build());  }
    public void setParentRegionId (String value) { this.osn.getOSClientV3().identity().regions().update(osRegion.toBuilder().parentRegionId(value).build());  }

    @Override
    public String get50CharactersDescription()
    {
        return "Region" + this.getId();
    }


}
