package com.net2plan.gui.plugins.utils;

import com.net2plan.gui.utils.FileChooserConfirmOverwrite;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class OpenStackFileChooser extends FileChooserConfirmOverwrite {

    public OpenStackFileChooser(){
        this(null);
    }
    public OpenStackFileChooser(File file){
        super(file);
        this.setAcceptAllFileFilterUsed(false);

        FileFilter shFilter = new FileNameExtensionFilter("Script RC de OpenStack v3 (*.sh)","sh");
        FileFilter netplanFilter = new FileNameExtensionFilter("Archivo Net2Plan (*.n2p)","n2p");
        this.addChoosableFileFilter(shFilter);
        this.addChoosableFileFilter(netplanFilter);
    }

    protected void onAccept(){
        this.getSelectedFile().delete();
    }
}
