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

    public static OpenStackRegion createFromAddRegion (OpenStackNet osn , String regionId, String regionDescription, String regionParentRegionId)
    {
        final OpenStackRegion res = new OpenStackRegion(osn);
        res.regionId= regionId;
        res.regionDescription=regionDescription;
        res.regionParentRegionId=regionParentRegionId;
        return res;
    }

    private OpenStackRegion (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osRegions);
        if(this.regionId != null)
            this.osRegion = this.osn.getOs().identity().regions().get(this.regionId);
    }

    @Override
    public String getId () { return this.regionId; }
    public String getRegionDescription () { return this.regionDescription; }
    public String getRegionParentRegionId () { return this.regionParentRegionId; }

    public void setRegionDescription (String value) { this.osn.getOs().identity().regions().update(osRegion.toBuilder().description(value).build());  }
    public void setParentRegionId (String value) { this.osn.getOs().identity().regions().update(osRegion.toBuilder().parentRegionId(value).build());  }

    @Override
    public String get50CharactersDescription()
    {
        return "Region" + this.getId();
    }


}
