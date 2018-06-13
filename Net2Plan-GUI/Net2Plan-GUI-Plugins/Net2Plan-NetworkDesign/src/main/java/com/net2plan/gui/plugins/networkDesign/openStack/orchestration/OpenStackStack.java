package com.net2plan.gui.plugins.networkDesign.openStack.orchestration;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.heat.Stack;

import java.util.List;

public class OpenStackStack extends OpenStackNetworkElement
{

    private String stackId;
    private String stackName ;
    private Stack stack ;


    public static OpenStackStack createFromAddStack (OpenStackNet osn , Stack stack)
    {
        final OpenStackStack res = new OpenStackStack(osn,stack);
        res.stackId= stack.getId();
        res.stackName=stack.getName();


        return res;
    }

    private OpenStackStack (OpenStackNet osn , Stack stack)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) osn.openStackStacks);
        this.stack = stack;


    }

    @Override
    public String getId () { return this.stackId; }
    public String getStackName () { return this.stackName; }



    @Override
    public String get50CharactersDescription()
    {
        return "Stack: " + this.getId();
    }


}
