package com.net2plan.gui.plugins.networkDesign.openStack.orchestration;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.heat.Event;

import java.util.List;

public class OpenStackEvent extends OpenStackNetworkElement
{

    private String eventId;
    private String eventResourceName ;
    private Event event ;


    public static OpenStackEvent createFromAddEvent (OpenStackNet osn , Event event)
    {
        final OpenStackEvent res = new OpenStackEvent(osn,event);
        res.eventId= event.getId();
        res.eventResourceName=event.getResourceName();


        return res;
    }

    private OpenStackEvent (OpenStackNet osn , Event event)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) osn.openStackEvents);
        this.event = event;


    }

    @Override
    public String getId () { return this.eventId; }
    public String getEventResourceName () { return this.eventResourceName; }



    @Override
    public String get50CharactersDescription()
    {
        return "Event: " + this.getId();
    }


}



