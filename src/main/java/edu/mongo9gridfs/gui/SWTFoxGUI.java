package edu.mongo9gridfs.gui;

import edu.mongo9gridfs.main.FileService;
import edu.mongo9gridfs.main.GUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class SWTFoxGUI implements GUI {

    private final FileService fileService;

    public SWTFoxGUI(FileService fileService) {
        this.fileService = fileService;
    }

    public void start(){
        int margin = 10;
        int gap = 50;
        int buttonHeight = 35;
        int defaultButtonWidth = 160;

        Display display = new Display();
        Shell mainWindow = new Shell(display);
        mainWindow.setSize(1280, 720);
        mainWindow.setLayout(null);


        Composite buttonBar = new Composite(mainWindow, SWT.NONE); //buttonBar.setBackground(new Color(display, 10, 100, 100));
        Button refreshButton = new Button(buttonBar, SWT.CENTER);
        refreshButton.setText("Refresh Table");
        refreshButton.addListener(SWT.MouseUp, event -> {

        });
        Button uploadButton = new Button(buttonBar, SWT.CENTER);
        uploadButton.setText("Upload File");
        uploadButton.addListener(SWT.MouseUp, event -> {

        });
        Button downloadButton = new Button(buttonBar, SWT.CENTER);
        downloadButton.setText("Download File");
        downloadButton.addListener(SWT.MouseUp, event -> {

        });
        Button deleteButton = new Button(buttonBar, SWT.CENTER);
        deleteButton.setText("Delete File");
        deleteButton.addListener(SWT.MouseUp, event -> {

        });
        buttonBar.addListener(SWT.Resize, event -> {
            Rectangle clientArea = buttonBar.getClientArea();
            if(clientArea.width<640 || clientArea.height<360) return;
            int buttonCount = 4;
            int spacingSize = 64;
            int totalSpacing = spacingSize*(buttonCount+1);
            int buttonWidth = (clientArea.width - totalSpacing) / buttonCount;
            refreshButton.setBounds(spacingSize, gap, buttonWidth, buttonHeight);
            uploadButton.setBounds(spacingSize*2+buttonWidth, gap, buttonWidth, buttonHeight);
            downloadButton.setBounds(spacingSize*3+buttonWidth*2, gap, buttonWidth, buttonHeight);
            deleteButton.setBounds(spacingSize*4+buttonWidth*3, gap, buttonWidth, buttonHeight);
        });

        Table filesTable = new Table(mainWindow, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
        filesTable.setHeaderVisible(true);
        filesTable.setLinesVisible(true);
        TableColumn nameCol = new TableColumn(filesTable, SWT.NONE);
        nameCol.setText("File name");
        TableColumn sizeCol = new TableColumn(filesTable, SWT.NONE);
        sizeCol.setText("File size");
        TableColumn dateCol = new TableColumn(filesTable, SWT.NONE);
        dateCol.setText("Upload date");
        filesTable.addListener(SWT.Resize, event -> {
            int tableWidth = filesTable.getClientArea().width;
            nameCol.setWidth((int)(tableWidth * 0.5));
            sizeCol.setWidth((int)(tableWidth * 0.25));
            dateCol.setWidth((int)(tableWidth*0.25));
        });



        mainWindow.addListener(SWT.Resize, event -> {
            Rectangle clientArea = mainWindow.getClientArea();
            if(clientArea.width<640 || clientArea.height<360) return;
            filesTable.setBounds(margin, margin, clientArea.width-(margin*2), clientArea.height-(buttonHeight+gap*2));
            buttonBar.setBounds(margin,clientArea.height-(buttonHeight+gap*2), clientArea.width-margin*2, clientArea.height-gap);


        });

        mainWindow.open();
        while(!mainWindow.isDisposed()){
            if(!display.readAndDispatch()){
                display.sleep();
            }
        }
    }

//    private void customizeButton(Button button, String text, int width, int height){
//        button.setText(text);
//        button.setSize(width, height);
//    }
}
