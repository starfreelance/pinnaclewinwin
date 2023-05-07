package com.pinnacle.winwin.fileutils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileOperation {

    Context context;

    public FileOperation(Context context) {

        this.context = context;
    }

    public Boolean isFileExits(String fileName) {
        DeviceStorage deviceStorage = new DeviceStorage();


        String filePath = deviceStorage.getFilePath(fileName, context);
        File file = new File(filePath);

        if (file.exists()) {
            return true;
        }

        return false;
    }

    public Boolean isFileExitsInSideFolder(String folderName, String fileName) {
        DeviceStorage deviceStorage = new DeviceStorage();

        String folderPath = deviceStorage.getFolderPath(folderName, context);
        String filePath = folderPath + "/" + fileName;

        File file = new File(filePath);

        if (file.exists()) {
            return true;
        }

        return false;
    }

    public String getFilePath(String fileName) {
        DeviceStorage deviceStorage = new DeviceStorage();

        String filePath = deviceStorage.getFilePath(fileName, context);

        return filePath;
    }

    public String getFilePathWithFolder(String folderName, String fileName) {
        DeviceStorage deviceStorage = new DeviceStorage();

        String folderPath = deviceStorage.getFolderPath(folderName, context);
        String filePath = folderPath + "/" + fileName;

        return filePath;
    }

    public Boolean write(String fileName, byte[] fileContent) {

        DeviceStorage deviceStorage = new DeviceStorage();
        String filePath = deviceStorage.getFilePath(fileName, context);
        //AppConstant.show("FileOperation","path is "+filePath);

        try {

            File file = new File(filePath);
            if (!file.exists()) {
                //AppConstant.show("FileOperation","path does not exists creating");
                file.createNewFile();
            }

//            FileWriter fw = new FileWriter(file.getAbsoluteFile());
//            BufferedWriter bufferedWriter = new BufferedWriter(fw);
//            bufferedWriter.write(String.valueOf(fileContent));
//            bufferedWriter.close();
//            fw.close();


            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            bos.write(fileContent);
            bos.flush();
            bos.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }

    public Boolean writeFileToExternalStorage(String fileName, byte[] fileContent) {

        DeviceStorage deviceStorage = new DeviceStorage();
        String filePath = deviceStorage.getExternalFilePath(fileName);

        BufferedOutputStream bos = null;
        try {

            File file = new File(filePath);
            if (!file.exists()) {
                //AppConstant.show("FileOperation","path does not exists creating");
                file.createNewFile();
            }

            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(fileContent);
            bos.flush();
            bos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public Boolean isDocumentFileExits(String fileName) {
        DeviceStorage deviceStorage = new DeviceStorage();


        String filePath = deviceStorage.getExternalFilePath(fileName);
        File file = new File(filePath);

        if (file.exists()) {
            return true;
        }

        return false;
    }


    public String getDocumentFilePath(String fileName) {
        DeviceStorage deviceStorage = new DeviceStorage();

        return deviceStorage.getExternalFilePath(fileName);
    }

    public void removeExternalFile(String fileName) {

        DeviceStorage deviceStorage = new DeviceStorage();
        String filePath = deviceStorage.getExternalFilePath(fileName);
        File file = new File(filePath);

        file.delete();

    }

    private Bitmap decodeFile(File f){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_SIZE=70;

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    public static Bitmap resizeBitMapImage1(String filePath, int targetWidth, int targetHeight) {
        Bitmap bitMapImage = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize = 1;// this is needed to compress the bitmap
            //options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            BitmapFactory.decodeFile(filePath, options);

            double sampleSize = 0;
            Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math.abs(options.outWidth
                    - targetWidth);
            if (options.outHeight * options.outWidth * 2 >= 1638) {
                sampleSize = scaleByHeight ? options.outHeight / targetHeight : options.outWidth / targetWidth;
                sampleSize = (int) Math.pow(2d, Math.floor(Math.log(sampleSize) / Math.log(2d)));
            }
            options.inJustDecodeBounds = false;
            options.inTempStorage = new byte[128];
            while (true) {
                try {
                    options.inSampleSize = (int) sampleSize;
                    bitMapImage = BitmapFactory.decodeFile(filePath, options);
                    break;
                } catch (Exception ex) {
                    try {
                        sampleSize = sampleSize * 2;
                    } catch (Exception ex1) {

                    }
                }
            }
        } catch (Exception ex) {

        }
        return bitMapImage;
    }

    /*Hint to the compressor, 0-100. 0 meaning compress for small size,
    100 meaning compress for max quality.Some formats, like PNG which is lossless, will ignore the quality setting*/
    public Boolean writeImageWithCompression(String fileName, byte[] fileContent, int compression) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;// this is needed to compress the bitmap
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeByteArray(fileContent, 0, fileContent.length, options);
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();
//        AppConstant.show("Image Width", String.valueOf(imageWidth));
//        AppConstant.show("Image Height", String.valueOf(imageWidth));
        if (imageWidth > 2000 && imageHeight > 2000) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        } else if (imageWidth > 800 && imageHeight > 600) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }

        byte[] imageData = stream.toByteArray();
        return write(fileName,imageData);

    }

    public Bitmap getCompressedBitmap(String imageFilePath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;// this is needed to compress the bitmap
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, options);
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();

        if (imageWidth > 2000 && imageHeight > 2000) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        } else if (imageWidth > 800 && imageHeight > 600) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }

        byte[] bitmapData = stream.toByteArray();
        return BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);

    }

    public Boolean writeImageToTemporary(String fileName, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] imageData = bytes.toByteArray();
        return write(fileName,imageData);
    }

    public Boolean write(String folderName, String fileName, byte[] fileContent) {

        DeviceStorage deviceStorage = new DeviceStorage();


        String folderPath = deviceStorage.getFolderPath(folderName, context);
        String filePath = folderPath + "/" + fileName;

        //AppConstant.show("FileOperation","path is "+filePath);

        try {

            File file = new File(filePath);
            if (!file.exists()) {
                //AppConstant.show("FileOperation","path does not exists creating");
                file.createNewFile();
            }

//            FileWriter fw = new FileWriter(file.getAbsoluteFile());
//            BufferedWriter bufferedWriter = new BufferedWriter(fw);
//            bufferedWriter.write(String.valueOf(fileContent));
//            bufferedWriter.close();
//            fw.close();

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            bos.write(fileContent);
            bos.flush();
            bos.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }

    public void remove(String fileName) {
        DeviceStorage deviceStorage = new DeviceStorage();
        String filePath = deviceStorage.getFilePath(fileName, context);
        File file = new File(filePath);

        file.delete();
    }

    public void remove(String folderName, String fileName) {
        DeviceStorage deviceStorage = new DeviceStorage();
        String folderPath = deviceStorage.getFolderPath(folderName, context);
        String filePath = folderPath + "/" + fileName;

        File file = new File(filePath);
        file.delete();
    }

    public void move(String fromPath, String toPath) {
        /*
        For moving the files we need to add

        compile group: 'commons-io', name: 'commons-io', version: '2.0.1'

        into the dependencies in the build.gradle of the app
         */

        File from = new File(fromPath);
        File to = new File(toPath);

        if (!to.exists()) {
            //AppConstant.show("FileOperation","path does not exists creating");
            try {
                to.createNewFile();
                //from.renameTo(to);

                InputStream in = new FileInputStream(from);

                OutputStream out = new FileOutputStream(to);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                in = null;

                out.flush();
                out.close();
                out = null;

                from.delete();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getByteArrayFromFilePath(String fileName) {
        FileOperation fileOperation = new FileOperation(context);
        String path = fileOperation.getFilePath(fileName);
        File file = new File(path);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bytes;
    }

    public byte[] getByteArrayFromDocumentFilePath(String fileName) {
        FileOperation fileOperation = new FileOperation(context);
        String path = fileOperation.getDocumentFilePath(fileName);
        File file = new File(path);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bytes;
    }

    public void copy(String fromPath, String toPath) {
        /*
        For moving the files we need to add

        compile group: 'commons-io', name: 'commons-io', version: '2.0.1'

        into the dependencies in the build.gradle of the app
         */

        File from = new File(fromPath);
        File to = new File(toPath);

        if (!to.exists()) {
            //AppConstant.show("FileOperation","path does not exists creating");
            try {
                to.createNewFile();
                //from.renameTo(to);

                InputStream in = new FileInputStream(from);

                OutputStream out = new FileOutputStream(to);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                in = null;

                out.flush();
                out.close();
                out = null;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public long getFileLength(String fileName) {
        File file = new File(getFilePath(fileName));
        return file.length() / 1024; //in KB
    }

    public long getFileLengthFromPath(String filePath) {
        File file = new File(filePath);
        return file.length() / 1024; // IN KB
    }

    // Following methods are used to pick the image from camera or gallery

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getPath(Uri uri) {
        final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context.getApplicationContext(), contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context.getApplicationContext(), contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context.getApplicationContext(), uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
