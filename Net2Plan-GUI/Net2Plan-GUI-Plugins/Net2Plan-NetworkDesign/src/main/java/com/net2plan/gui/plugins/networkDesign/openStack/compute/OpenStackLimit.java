package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.compute.AbsoluteLimit;
import org.openstack4j.model.compute.RateLimit;

import java.util.List;

public class OpenStackLimit  extends OpenStackNetworkElement
{

    private AbsoluteLimit limitAbsolute;
    private List<? extends RateLimit> limitRate;

    public static OpenStackLimit createFromAddLimit(OpenStackNet osn , AbsoluteLimit limitAbsolute, List<? extends RateLimit> limitRate)
    {
        final OpenStackLimit res = new OpenStackLimit(osn);
        res.limitAbsolute= limitAbsolute;
        res.limitRate=limitRate;
        return res;
    }

    private OpenStackLimit (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osLimits);
    }

    @Override
    public String getId () { return this.limitAbsolute.toString(); }
    public AbsoluteLimit getLimitAbsolute () { return this.limitAbsolute; }
    public List<? extends RateLimit> getLimitRate () { return this.limitRate; }


    @Override
    public String get50CharactersDescription()
    {
        return "Limit" + this.getId();
    }


}
