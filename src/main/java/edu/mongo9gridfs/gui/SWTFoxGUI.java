package edu.mongo9gridfs.gui;

import edu.mongo9gridfs.main.FileService;
import edu.mongo9gridfs.main.GUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class SWTFoxGUI implements GUI {

    private final FileService fileService;
    private final String windowTitle;

    public SWTFoxGUI(FileService fileService, String windowTitle) {
        this.fileService = fileService;
        this.windowTitle=windowTitle;
    }

    public void start(){
        int margin = 10;            //Default space between table and windows edges
        int gap = 50;               //Default space between buttons and table/edges
        int buttonHeight = 35;

        //Window and components initialization
        Display display = new Display();
        Shell mainWindow = new Shell(display);
        Table filesTable = new Table(mainWindow, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
//        Composite bridgeHandle = new Composite(mainWindow, SWT.EMBEDDED);
        Composite buttonBar = new Composite(mainWindow, SWT.NONE); //buttonBar.setBackground(new Color(display, 10, 100, 100));
        Button refreshButton = new Button(buttonBar, SWT.CENTER);
        Button uploadButton = new Button(buttonBar, SWT.CENTER);
        Button downloadButton = new Button(buttonBar, SWT.CENTER);
        Button deleteButton = new Button(buttonBar, SWT.CENTER);

        //Window Settings
        mainWindow.setSize(1280, 720);
        mainWindow.setLayout(null);
        mainWindow.setText(windowTitle);

        //Buttons settings
        refreshButton.setText("Refresh Table");
        uploadButton.setText("Upload File");
        downloadButton.setText("Download File");
        deleteButton.setText("Delete File");

        //Buttons' actions
        refreshButton.addListener(SWT.Selection, event -> updateTable(filesTable, fileService.getFilesInfo()));
        uploadButton.addListener(SWT.Selection, event -> {                                //Using swt-awt led to annoying "EmbeddedJavaFrame"
//            Frame bridgeFrame = SWT_AWT.new_Frame(bridgeHandle);                              //invisible windows stuck in background in my kde setup.
            FileDialog fd = new FileDialog((Frame)null, "Select File", FileDialog.LOAD);     //So used awt with "null" awt init instead of swt-awt
            fd.setVisible(true);
            String directory = fd.getDirectory();
            String filename = fd.getFile();
            if(filename == null){
                System.out.println("cancelled");
                return;
            }
            Object uploaded = fileService.uploadFile(directory+filename);
            if(uploaded==null){
                SWTFoxAdditions.showNativeError(mainWindow, "FileService Error", "File was not uploaded");
            } else{
                SWTFoxAdditions.showNativeInfo(mainWindow, "Success", "File Uploaded. New ID: " + uploaded);
                updateTable(filesTable, fileService.getFilesInfo());
            }
        });
        downloadButton.addListener(SWT.Selection, event -> {
            TableItem[] selected = filesTable.getSelection();
            if(selected.length == 0){
                System.out.println("Table row is not selected");
                SWTFoxAdditions.showNativeError(mainWindow, "Error", "Table row is not selected!");
                return;
            }
            String name = selected[0].getText(0);
            Object fileId = selected[0].getData();
            FileDialog sd = new FileDialog((Frame)null, "Save File", FileDialog.SAVE);
            sd.setFile(name);
            sd.setVisible(true);
            String directory = sd.getDirectory();
            String filename = sd.getFile();
            if(filename==null){
                System.out.println("cancelled");
                SWTFoxAdditions.showNativeError(mainWindow, "Error", "You should select destination file");
            } else {
                String fullPath = directory + filename;
                boolean downloaded = fileService.downloadFile(fileId, fullPath);
                if(downloaded){
                    SWTFoxAdditions.showNativeInfo(mainWindow, "Success", filename + "- File is downloaded!");
                } else {
                    SWTFoxAdditions.showNativeError(mainWindow, "Error", "Failed downloading file");
                }

            }
        });
        deleteButton.addListener(SWT.Selection, event -> {
            TableItem[] selected = filesTable.getSelection();
            if(selected.length == 0){
                System.out.println("Table row is not selected");
                SWTFoxAdditions.showNativeError(mainWindow, "Error", "Table row is not selected!");
            }
            Object fileId = selected[0].getData();
            boolean deleted = fileService.deleteFile(fileId);
            if (deleted){
                SWTFoxAdditions.showNativeInfo(mainWindow, "Success", "Deleted file with ID: " + fileId.toString());
                updateTable(filesTable, fileService.getFilesInfo());
            } else {
                SWTFoxAdditions.showNativeError(mainWindow, "Error", "Failed deleting file");
            }
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
//        bridgeHandle.setVisible(false);

        //Table settings, Columns initialization
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

        updateTable(filesTable, fileService.getFilesInfo()); //Initial table population


        //Main window functionality
        mainWindow.addListener(SWT.Resize, event -> {
            Rectangle clientArea = mainWindow.getClientArea();
            if(clientArea.width<640 || clientArea.height<360) return;
            filesTable.setBounds(margin, margin, clientArea.width-(margin*2), clientArea.height-(buttonHeight+gap*2));
            buttonBar.setBounds(margin,clientArea.height-(buttonHeight+gap*2), clientArea.width-margin*2, clientArea.height-gap);


        });

        mainWindow.open();
        mainWindow.addListener(SWT.Close, event -> { //Using this to shut down both SWT and AWT at the same time
            System.exit(0);
        });
        while(!mainWindow.isDisposed()){
            if(!display.readAndDispatch()){
                display.sleep();
            }
        }

    }

    private void updateTable(Table table, Map<Object, List<String>> filesInfoMap){
        table.removeAll();
        for(Map.Entry<Object, List<String>> entry : filesInfoMap.entrySet()){
            TableItem item = new TableItem(table, SWT.NONE);
            item.setData(entry.getKey());
            int columnNum = 0;
            for(String info : entry.getValue()){
                item.setText(columnNum, info);
                columnNum++;
            }
        }
    }
}
