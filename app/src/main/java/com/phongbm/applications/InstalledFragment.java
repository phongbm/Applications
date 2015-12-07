package com.phongbm.applications;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class InstalledFragment extends Fragment implements AdapterView.OnItemClickListener,
        AppInfoFragment.OnUninstallListener {
    private static final String TAG = InstalledFragment.class.getSimpleName();

    private ApplicationAdapter applicationAdapter;
    private int indexRemove = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationAdapter = new ApplicationAdapter(this.getActivity(), ApplicationManager.getInstance().getApps());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_installed, container, false);
        ListView listViewApp = (ListView) view.findViewById(R.id.list_view_app);
        listViewApp.setOnItemClickListener(this);
        listViewApp.setAdapter(applicationAdapter);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        indexRemove = position;
        ApplicationManager.getInstance().setPackageInfo((PackageInfo) parent.getItemAtPosition(position));
        AppInfoFragment appInfoFragment = new AppInfoFragment();
        appInfoFragment.setOnUninstallListener(this);
        appInfoFragment.show(this.getActivity().getSupportFragmentManager(), null);
    }

    @Override
    public void onUninstalled(boolean result) {
        if (result) {
            applicationAdapter.remove(indexRemove);
            applicationAdapter.notifyDataSetChanged();
            indexRemove = -1;
        }
    }

}