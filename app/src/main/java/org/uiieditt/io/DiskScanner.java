package org.uiieditt.io;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import org.uiieditt.io.fileutil.FileController;

public class DiskScanner implements
        MediaScannerConnection.MediaScannerConnectionClient {

    private static DiskScanner media;
    private static MediaScannerConnection conn;
    private static FileController imgFile;

    public static DiskScanner getMedia(FileController file) {

        imgFile = file;

        if (media == null)
            media = new DiskScanner();

        return media;
    }

    public void startScan(Context context) {
        if (conn != null)
            conn.disconnect();

        conn = new MediaScannerConnection(context, this);
        conn.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        try {
            conn.scanFile(imgFile.toString(), "image/*");
        } catch (IllegalStateException e) {
            // todo add permission to micro sd
        }
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        conn.disconnect();
    }
}