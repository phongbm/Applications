package com.phongbm.applications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppInfoFragment extends DialogFragment implements View.OnClickListener {
    private static final String TAG = AppInfoFragment.class.getSimpleName();
    private static final String SP_PACKAGE_NAME_DELETE = "SP_PACKAGE_NAME_DELETE";
    private static final int REQUEST_CODE_UNINSTALL = 111;

    private View view;
    private PackageManager packageManager;
    private PackageInfo packageInfo;
    private ImageView imgIcon;
    private TextView txtAppName;
    private TextView txtPackageName;
    private TextView txtVersionName;
    private TextView txtFeature;
    private TextView txtPermission;
    private TextView txtPath;
    private TextView txtTargetVersion;
    private TextView txtInstalledDate;
    private TextView txtLastModify;
    private OnUninstallListener onUninstallListener;
    private SharedPreferences sharedPreferences;

    public AppInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        this.setHasOptionsMenu(true);
        packageInfo = ApplicationManager.getInstance().getPackageInfo();
        sharedPreferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_app_info, container, false);
        this.initializeToolbar();
        this.initializeComponent();
        this.setValues();
        return view;
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) this.getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("App Info");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }
    }

    private void initializeComponent() {
        imgIcon = (ImageView) view.findViewById(R.id.img_icon);
        txtAppName = (TextView) view.findViewById(R.id.txt_app_name);
        txtVersionName = (TextView) view.findViewById(R.id.txt_version_name);
        txtPackageName = (TextView) view.findViewById(R.id.txt_package_name);
        txtFeature = (TextView) view.findViewById(R.id.txt_feature);
        txtPermission = (TextView) view.findViewById(R.id.txt_permission);
        txtPath = (TextView) view.findViewById(R.id.txt_path);
        txtTargetVersion = (TextView) view.findViewById(R.id.txt_target_version);
        txtInstalledDate = (TextView) view.findViewById(R.id.txt_installed_date);
        txtLastModify = (TextView) view.findViewById(R.id.txt_last_modify);

        view.findViewById(R.id.btn_uninstall).setOnClickListener(this);
        view.findViewById(R.id.btn_open).setOnClickListener(this);
    }

    private void setValues() {
        packageManager = this.getActivity().getPackageManager();

        imgIcon.setImageDrawable(packageManager.getApplicationIcon(packageInfo.applicationInfo));
        txtAppName.setText(packageManager.getApplicationLabel(packageInfo.applicationInfo));
        txtVersionName.setText("Version: " + packageInfo.versionName);
        txtPackageName.setText(packageInfo.packageName);
        if (packageInfo.reqFeatures != null) {
            txtFeature.setText(this.getFeatures(packageInfo.reqFeatures));
        } else {
            txtFeature.setText("-");
        }
        if (packageInfo.requestedPermissions != null) {
            txtPermission.setText(this.getPermissions(packageInfo.requestedPermissions));
        } else {
            txtPermission.setText("-");
        }
        txtPath.setText(packageInfo.applicationInfo.sourceDir);
        txtTargetVersion.setText(String.valueOf(packageInfo.applicationInfo.targetSdkVersion));
        txtInstalledDate.setText(this.setDateFormat(packageInfo.firstInstallTime));
        txtLastModify.setText(this.setDateFormat(packageInfo.lastUpdateTime));
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.getDialog().getWindow().getAttributes().windowAnimations = R.style.FragmentDialog;
    }

    private String getFeatures(FeatureInfo[] features) {
        String feature = "";
        for (FeatureInfo i : features) {
            feature = feature + i + "\n";
        }
        return feature;
    }

    private String getPermissions(String[] permissions) {
        String permission = "";
        for (String i : permissions) {
            permission = permission + i + "\n";
        }
        return permission;
    }

    private String setDateFormat(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        return formatter.format(new Date(time));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.dismiss();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                String packageName = txtPackageName.getText().toString();
                Intent launchIntent = packageManager.getLaunchIntentForPackage(packageName);
                this.startActivity(launchIntent);
                break;

            case R.id.btn_uninstall:
                packageName = txtPackageName.getText().toString();
                sharedPreferences.edit().putString(SP_PACKAGE_NAME_DELETE, packageName).apply();
                Intent unInstallIntent = new Intent(Intent.ACTION_DELETE);
                unInstallIntent.setData(Uri.parse("package:" + packageName));
                this.startActivityForResult(unInstallIntent, REQUEST_CODE_UNINSTALL);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_UNINSTALL) {
            String requestedPackageName = sharedPreferences.getString(SP_PACKAGE_NAME_DELETE, "");
            boolean isPresent = this.isAppPresent(requestedPackageName, this.getActivity());
            if (isPresent) {
                onUninstallListener.onUninstalled(false);
            } else {
                onUninstallListener.onUninstalled(true);
                this.dismiss();
            }
        }
    }

    public boolean isAppPresent(String packageName, Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            return applicationInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void setOnUninstallListener(OnUninstallListener onUninstallListener) {
        this.onUninstallListener = onUninstallListener;
    }

    public interface OnUninstallListener {
        void onUninstalled(boolean result);
    }

}