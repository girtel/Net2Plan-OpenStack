package com.net2plan.gui.plugins.networkDesign.openStack.telemetry;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.telemetry.Meter;

import java.util.List;

public class OpenStackMeter  extends OpenStackNetworkElement
{

    private String meter_id;
    private String meter_name ;
    private String meter_user_id ;
    private String meter_project_id ;
    private String meter_resource_id ;
    private String meter_unit;
    private Meter.Type meter_type;

    private Meter meter ;


    public static OpenStackMeter createFromAddMeter (OpenStackNet osn , Meter meter,OpenStackClient openStackClient)
    {
        final OpenStackMeter res = new OpenStackMeter(osn,meter,openStackClient);
        res.meter_id= meter.getId();
        res.meter_name=meter.getName();
        res.meter_user_id = meter.getUserId();
        res.meter_project_id=meter.getProjectId();
        res.meter_resource_id=meter.getResourceId();
        res.meter_unit=meter.getUnit();
        res.meter_type = meter.getType();

        return res;
    }

    private OpenStackMeter (OpenStackNet osn , Meter meter,OpenStackClient openStackClient)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackMeters,openStackClient);
        this.meter = meter;


    }

    @Override
    public String getId () { return this.meter_id; }
    public String getName () { return this.meter_name; }
    public String getMeter_user_id() {return this.meter_user_id;}
    public String getMeter_project_id () { return this.meter_project_id; }
    public String getMeter_resource_id () { return this.meter_resource_id; }
    public String getMeter_unit () { return this.meter_unit; }
    public Meter.Type getMeter_type(){return  this.meter_type;}

    @Override
    public String get50CharactersDescription()
    {
    /*    String description = "User: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Name " + this.getName() +
                this.NEWLINE + "Domain ID " + this.getDomainId() +
                this.NEWLINE + "Email " + this.getEmail() +
                this.NEWLINE + "Description " + this.getDescription() +
                this.NEWLINE + "Enable " + this.isUserEnable() +
                this.NEWLINE + "Links" + this.NEWLINE;
        for(String key : this.getUserLinks().keySet()) {
            description += key + " " + this.getUserLinks().get(key) + NEWLINE;
        }
        return description;*/
    return "Meter";
    }


}


