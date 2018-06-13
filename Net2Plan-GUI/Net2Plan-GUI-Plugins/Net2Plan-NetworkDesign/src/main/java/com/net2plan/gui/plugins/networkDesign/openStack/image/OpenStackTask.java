package com.net2plan.gui.plugins.networkDesign.openStack.image;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.image.v2.Task;

import java.util.List;

public class OpenStackTask extends OpenStackNetworkElement
{

    private String taskId;
    private String taskMessage;
    private Task task ;


    public static OpenStackTask createFromAddTask(OpenStackNet osn , Task task)
    {
        final OpenStackTask res = new OpenStackTask(osn,task);
        res.taskId= task.getId();
        res.taskMessage=task.getMessage();
        task.getCreatedAt();
        task.getExpiresAt();
        task.getInput();
        task.getOwner();
        task.getResult();
        task.getSchema();
        task.getSelf();
        task.getStatus();
        task.getType();
        task.getUpdatedAt();

        return res;
    }

    private OpenStackTask (OpenStackNet osn , Task task)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) osn.openStackTasks);
        this.task = task;


    }

    @Override
    public String getId () { return this.taskId; }
    public String getTaskMessage () { return this.taskMessage; }



    @Override
    public String get50CharactersDescription()
    {
        return "Task: " + this.getId();
    }


}


