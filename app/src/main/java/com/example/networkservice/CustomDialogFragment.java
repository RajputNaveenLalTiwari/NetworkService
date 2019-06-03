package com.example.networkservice;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class CustomDialogFragment extends DialogFragment {
    static CustomDialogFragment newInstance() {
        return new CustomDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.custom_dialog_fragment_layout, container, false);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();

            Fragment previousFragment = manager.findFragmentByTag("Dialog Fragment");
            if (previousFragment != null) {
                ft.remove(previousFragment);
            } else {
                ft.remove(this);
                ft.add(this, tag);
            }
            ft.commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
