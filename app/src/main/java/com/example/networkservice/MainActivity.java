package com.example.networkservice;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.networkservice.networking.NetworkClient;
import com.example.networkservice.networking.NetworkRequestCode;
import com.example.networkservice.networking.NetworkServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NetworkServiceGenerator.NetworkServiceReturns, NetworkServiceGenerator.ProgressCallback {
    private static final String TAG = "MainActivity";
    private DialogFragment dialogFragment;
    private FragmentManager fragmentManager;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);

        dialogFragment = CustomDialogFragment.newInstance();
        fragmentManager = getSupportFragmentManager();

        NetworkServiceGenerator serviceGenerator = new NetworkServiceGenerator();
        serviceGenerator.setNetworkServiceReturns(this);
        serviceGenerator.setProgressCallback(this);

        NetworkServiceGenerator.changeApiBaseUrl("https://reqres.in/");
        NetworkClient networkClient =
                NetworkServiceGenerator.createNetworkClient(NetworkClient.class);
//        executeGET(networkClient);
        executePOST(networkClient);
//        executePUT(networkClient);
    }

    private void executePUT(NetworkClient networkClient) {
        Call<User> call = networkClient.putObject(new User("Naveen", "Mobile Apps"));
        NetworkServiceGenerator.callNetworkService(NetworkRequestCode.REQUEST_CODE_PUT, call);
    }

    private void executePOST(NetworkClient networkClient) {
        Call<User> call = networkClient.postObject(new User("Naveen", "Mobile Apps"));
        NetworkServiceGenerator.callNetworkService(NetworkRequestCode.REQUEST_CODE_POST, call);
    }

    private void executeGET(NetworkClient networkClient) {
        Call<List<GitHubRepo>> call = networkClient.getGithubRepositories("googlesamples");
        NetworkServiceGenerator.callNetworkService(NetworkRequestCode.REQUEST_CODE_GET, call);
    }

    @Override
    public <T> void onNetworkResponse(NetworkRequestCode requestCode, Response<T> response) {
        switch (requestCode) {
            case REQUEST_CODE_GET:
                Log.i(TAG, (response.body() != null ? "GET --> " + response.body().toString() : ""));
                break;
            case REQUEST_CODE_PUT:
                Log.i(TAG, (response.body() != null ? "PUT --> " + response.body().toString() : ""));
                break;
            case REQUEST_CODE_POST:
                Log.i(TAG, (response.body() != null ? "POST --> " + response.body().toString() : ""));
                break;
        }
    }

    @Override
    public void startProgress() {
        if (dialogFragment != null) {
            dialogFragment.show(fragmentManager, "Dialog Fragment");
            dialogFragment.setCancelable(false);
        }
    }

    @Override
    public void dismissProgress() {
        if (dialogFragment != null) {
            Fragment previousFragment = fragmentManager.findFragmentByTag("Dialog Fragment");
            if (previousFragment != null) {
                DialogFragment dialogFragment = (DialogFragment)previousFragment;
                dialogFragment.dismiss();
            }
        }
    }

    /*private void showProgress() {
        dialogFragment.show(fragmentManager, "Dialog Fragment");
    }

    private void dismissProgress() {
        dialogFragment.dismiss();
    }*/
}
