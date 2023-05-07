package com.pinnacle.winwin.fileutils;

import android.content.Context;
import android.os.Environment;

public class DeviceStorage {

    // Storage states
    private boolean externalStorageAvailable, externalStorageWriteable;

    /**
     * Checks the external storage's state and saves it in member attributes.
     */
    private void checkStorage()
    {
// Get the external storage's state
        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED))
        {
            // Storage is available and writeable
            externalStorageAvailable = externalStorageWriteable = true;
        }
        else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            // Storage is only readable
            externalStorageAvailable = true;
            externalStorageWriteable = false;
        }
        else
        {
            // Storage is neither readable nor writeable
            externalStorageAvailable = externalStorageWriteable = false;
        }
    }

    /**
     * Checks the state of the external storage.
     *
     * @return True if the external storage is available, false otherwise.
     */
    public boolean isExternalStorageAvailable()
    {
        checkStorage();

        return externalStorageAvailable;
    }

    /**
     * Checks the state of the external storage.
     *
     * @return True if the external storage is writeable, false otherwise.
     */
    public boolean isExternalStorageWriteable()
    {
        checkStorage();

        return externalStorageWriteable;
    }

    /**
     * Checks the state of the external storage.
     *
     * @return True if the external storage is available and writeable, false
     *         otherwise.
     */
    public boolean isExternalStorageAvailableAndWriteable()
    {
        checkStorage();

        if (!externalStorageAvailable)
        {
            return false;
        }
        else if (!externalStorageWriteable)
        {
            return false;
        }
        else
        {
            return false;
        }
    }

    public String getFolderPath(String folderName, Context context){

        String folderPath = null;

        if(isExternalStorageAvailableAndWriteable())
        {
            //AppConstant.show("DeviceStorage","ExternalStorageAvailableAndWriteable");
            folderPath = Environment.getExternalStorageDirectory() + "/" + folderName;
        }
        else {

            //AppConstant.show("DeviceStorage","ExternalStorageAvailableAndWriteable is not there");

            folderPath = context.getFilesDir() + "/" + folderName;
        }

        return folderPath;
    }

    public String getFilePath(String fileName, Context context){

        String filePath = null;

        if(isExternalStorageAvailableAndWriteable())
        {
            //AppConstant.show("DeviceStorage","ExternalStorageAvailableAndWriteable in getFilePath");
            filePath = Environment.getExternalStorageDirectory()+"/"+fileName;
        }
        else {

            //AppConstant.show("DeviceStorage","ExternalStorageAvailableAndWriteable is not there in getFilePath");

            filePath = context.getFilesDir()+"/"+fileName;
        }

        return filePath;
    }

    public String getExternalFilePath(String fileName) {
        // Get the directory for the user's public pictures directory.
        String filePath = null;

        filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+fileName;

        /*File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), fileName);
        if (!file.mkdirs()) {
            AppConstant.show("DeviceStorage", "Directory not created");
        }*/
        return filePath;
    }

}
