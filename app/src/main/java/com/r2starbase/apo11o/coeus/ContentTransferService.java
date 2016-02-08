package com.r2starbase.apo11o.coeus;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Originally created by apo11o on 2/6/16.
 */
public class ContentTransferService extends IntentService {
    public static final String TAG = "ContentTransferService";
    public static final int SOCKET_TIMEOUT = 5000;
    public static final String ACTION_SEND_FILE = "com.r2starbase.apo11o.coeus.SEND_FILE";
    public static final String ACTION_SEND_TEXT = "com.r2starbase.apo11o.coeus.SEND_TEXT";
    public static final String EXTRAS_FILE_PATH = "file_url";
    public static final String EXTRAS_GROUP_OWNER_ADDRESS = "go_host";
    public static final String EXTRAS_GROUP_OWNER_PORT = "go_port";

    public ContentTransferService() {
        super("ContentTransferService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context ctx = getApplicationContext();
        String action = intent.getAction();

        if (action.equals(ContentTransferService.ACTION_SEND_FILE)) {
            String fileUri = intent.getExtras().getString(ContentTransferService.EXTRAS_FILE_PATH);
            String host = intent.getExtras().getString(ContentTransferService.EXTRAS_GROUP_OWNER_ADDRESS);
            int port = intent.getExtras().getInt(ContentTransferService.EXTRAS_GROUP_OWNER_PORT);
            Socket sock = new Socket();

            try {
                sock.bind(null);
                sock.connect(new InetSocketAddress(host, port), ContentTransferService.SOCKET_TIMEOUT);
                OutputStream oStream = sock.getOutputStream();
                ContentResolver cr = ctx.getContentResolver();
                InputStream iStream = cr.openInputStream(Uri.parse(fileUri));
                ContentTransferService.transferContent(iStream, oStream);
                oStream.close();
                if (iStream != null) {
                    iStream.close();
                }
            } catch (IllegalArgumentException | IOException e) {
                Log.d(ContentTransferService.TAG, e.getMessage());
            } finally {
                if (sock.isConnected()) {
                    try {
                        sock.close();
                    } catch (IOException e) {
                        Log.d(ContentTransferService.TAG, e.toString());
                    }
                }
            }
        }
    }

    public static boolean transferContent(InputStream is, OutputStream os) {
        int len;
        byte buf[] = new byte[1024];
        try {
            while ((len = is.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
        } catch (IOException e) {
            Log.d(ContentTransferService.TAG, e.getMessage());
            return false;
        }
        return true;
    }
}
