package com.phongbm.applications;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ApplicationManager implements Cloneable, Serializable {
    private static class LazyInit {
        private static final ApplicationManager INSTANCE = new ApplicationManager();
    }

    public static ApplicationManager getInstance() {
        return LazyInit.INSTANCE;
    }

    private ApplicationManager() {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        return new CloneNotSupportedException();
    }

    protected Object readResolve() {
        return ApplicationManager.getInstance();
    }

    private ArrayList<PackageInfo> apps;
    private ArrayList<PackageInfo> systemApps;
    private PackageInfo packageInfo;

    public void getData(final Context context) {
        if (apps == null) {
            apps = new ArrayList<>();
        } else {
            apps.clear();
        }
        if (systemApps == null) {
            systemApps = new ArrayList<>();
        } else {
            systemApps.clear();
        }
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> list = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (PackageInfo packageInfo : list) {
            if (this.isSystemPackage(packageInfo)) {
                systemApps.add(packageInfo);
            }
            if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null &&
                    !packageManager.getLaunchIntentForPackage(packageInfo.packageName).toString().equals("")) {
                apps.add(packageInfo);
            }
        }

        Collections.sort(apps, new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo pi1, PackageInfo pi2) {
                String name1 = packageManager.getApplicationLabel(pi1.applicationInfo).toString();
                String name2 = packageManager.getApplicationLabel(pi2.applicationInfo).toString();
                return name1.toUpperCase().compareTo(name2.toUpperCase());
            }
        });
        Collections.sort(systemApps, new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo pi1, PackageInfo pi2) {
                String name1 = packageManager.getApplicationLabel(pi1.applicationInfo).toString();
                String name2 = packageManager.getApplicationLabel(pi2.applicationInfo).toString();
                return name1.toUpperCase().compareTo(name2.toUpperCase());
            }
        });
    }

    public boolean isSystemPackage(PackageInfo packageInfo) {
        return (packageInfo.applicationInfo.flags &
                (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0;
    }

    public ArrayList<PackageInfo> getApps() {
        return apps;
    }

    public ArrayList<PackageInfo> getSystemApps() {
        return systemApps;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

}