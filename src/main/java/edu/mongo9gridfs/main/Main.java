package edu.mongo9gridfs.main;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Main {
    public static void main(String[] args) {
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
