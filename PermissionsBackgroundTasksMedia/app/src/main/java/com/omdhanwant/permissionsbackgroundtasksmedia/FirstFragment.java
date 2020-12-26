package com.omdhanwant.permissionsbackgroundtasksmedia;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.omdhanwant.permissionsbackgroundtasksmedia.MediaPlayer.MediaPlayerActivity;
import com.omdhanwant.permissionsbackgroundtasksmedia.Notification.NotifyMe;

import java.util.Objects;

public class FirstFragment extends Fragment {
    View mView;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });


        view.findViewById(R.id.getlocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        view.findViewById(R.id.seekbaract).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =  new Intent(getContext(), SeekBarWithThread.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.mediaPlayer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =  new Intent(getContext(), MediaPlayerActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =  new Intent(getContext(), NotifyMe.class);
                startActivity(intent);
            }
        });

        checkSMSPermission();

        view.findViewById(R.id.broadcastmsg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =  new Intent();
                intent.setAction("test_action");
                intent.putExtra("msg","hello from broadcast");
                getActivity().sendBroadcast(intent);

            }
        });
    }


    //access to permission
    void checkSMSPermission(){
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) !=
                    PackageManager.PERMISSION_GRANTED  ){
                if(!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_SMS)) {
                    requestPermissions(new String[]{
                                    android.Manifest.permission.READ_SMS},
                            0);
                }
                return ;
            }
        }

    }

    //access to permission
    void checkPermission(){
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED  ){
                if(!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(new String[]{
                                    android.Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }
                return ;
            }
        }

        getLocation();

    }
    //get access to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
//    final private int REQUEST_CODE_ASK_SMS_PERMISSIONS = 321;



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();

                } else {
                    // Permission Denied
                    Toast.makeText( getContext(),"permission denied!" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



   @SuppressLint("SetTextI18n")
   public void getLocation() {
        LocationManager locationManager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
       TextView tv = mView.findViewById(R.id.tvLoc);
       tv.setText("longitude: " +location.getLongitude() + "latitude: " + location.getLatitude());

    }


}