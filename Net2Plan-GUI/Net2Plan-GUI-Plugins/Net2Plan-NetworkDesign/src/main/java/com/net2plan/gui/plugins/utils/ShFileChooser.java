package com.net2plan.gui.plugins.utils;

import com.net2plan.gui.utils.FileChooserConfirmOverwrite;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class ShFileChooser extends FileChooserConfirmOverwrite {

    public ShFileChooser(){
        this(null);
    }
    public ShFileChooser(File file){
        super(file);
        this.setAcceptAllFileFilterUsed(false);

        FileFilter shfilter = new FileNameExtensionFilter("Script RC (*.sh)","sh");
        this.addChoosableFileFilter(shfilter);
    }

    protected void onAccept(){
        this.getSelectedFile().delete();
    }
}
