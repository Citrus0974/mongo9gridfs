package edu.mongo9gridfs.gui;

import org.eclipse.swt.widgets.Shell;

public class SWTFoxAdditions {
    public static int showNativeError(Shell shell, String title, String message) {
        // Using the constants directly from your OS class
        int opts = org.eclipse.swt.internal.fox.OS.MBOX_OK |
                org.eclipse.swt.internal.fox.OS.DECOR_TITLE |
                org.eclipse.swt.internal.fox.OS.DECOR_BORDER;

        // Create the handle
        long handle = org.eclipse.swt.internal.fox.OS.FXMessageBox_new2(
                shell,
                shell.handle,
                title,
                message,
                0,      // iconHandle
                opts,   // Use the MBOX_OK constant from OS.java
                0, 0
        );

        // Show it modally
        // placement: 1 = PLACEMENT_VISIBLE or 3 = PLACEMENT_OWNER
        return org.eclipse.swt.internal.fox.OS.FXDialogBox_execute(handle, 3);
    }

    public static int showNativeInfo(Shell shell, String title, String message) {
        // opts:
        // OS.MBOX_OK (0x10000000) -> The OK button
        // OS.DECOR_TITLE (0x00020000) -> Show the window title bar
        // OS.DECOR_BORDER (0x00200000) -> Standard window border
        int opts = org.eclipse.swt.internal.fox.OS.MBOX_OK |
                org.eclipse.swt.internal.fox.OS.DECOR_TITLE |
                org.eclipse.swt.internal.fox.OS.DECOR_BORDER;

        long handle = org.eclipse.swt.internal.fox.OS.FXMessageBox_new2(
                shell,
                shell.handle,
                title,
                message,
                0,      // iconHandle (0 = default)
                opts,
                0, 0
        );

        return org.eclipse.swt.internal.fox.OS.FXDialogBox_execute(handle, 3);
    }
}
