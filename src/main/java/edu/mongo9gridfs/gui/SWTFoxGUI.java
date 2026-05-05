package edu.mongo9gridfs.gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SWTFoxGUI {

    public void start(){
        Display display = new Display();
        Shell mainWindow = new Shell(display);


        mainWindow.open();
        while(!mainWindow.isDisposed()){
            if(!display.readAndDispatch()){
                display.sleep();
            }
        }
    }
}
