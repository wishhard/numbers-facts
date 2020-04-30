package com.wishhard.nf.numbersfacts.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


import com.wishhard.nf.numbersfacts.pref.FactPref;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AppStickyService extends Service {

    private FactPref pref;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pref = FactPref.getInstance(getApplicationContext());
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        try {
            deleteCaches();
        } catch (IOException e) {

        }

        pref.clear();
        stopSelf();
    }

    private void deleteCaches() throws IOException {
        File dir = this.getCacheDir();
        List<File> children = (List<File>) FileUtils.listFilesAndDirs(dir,
                TrueFileFilter.INSTANCE,TrueFileFilter.INSTANCE);
        for (int i = children.size()-1; i >= 0;i--) {
            if(i == 0) {
                break;
            }
            FileUtils.deleteQuietly(children.get(i));
        }
    }
}
