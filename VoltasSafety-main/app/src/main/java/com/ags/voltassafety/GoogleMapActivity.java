package com.ags.voltassafety;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.ags.voltassafety.model.ObservationHeader;
import com.ags.voltassafety.model.ObservationResponse;
import com.ags.voltassafety.model.UserGpsInfo;
import com.ags.voltassafety.retrofitConnections.ApiInterface;
import com.ags.voltassafety.retrofitConnections.RetrofitConnect;
import com.ags.voltassafety.utility.Utilities;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback
       /* GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener*/ {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    //Location mLastLocation;
    private String flag;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;
    List<ObservationHeader> gpsList;
    UserGpsInfo objUserGpsInfo;
    ObservationHeader observationHeader;
    private TextView openText, assignText, closeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_map);
            openText = (TextView) findViewById(R.id.openText);
            assignText = (TextView) findViewById(R.id.assignText);
            closeText = (TextView) findViewById(R.id.closeText);
            openText.setTypeface(Utilities.fontRegular(GoogleMapActivity.this));
            assignText.setTypeface(Utilities.fontRegular(GoogleMapActivity.this));
            closeText.setTypeface(Utilities.fontRegular(GoogleMapActivity.this));

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                flag = bundle.getString("FLAG");
                if (flag.equalsIgnoreCase("Observation")) {
                    observationHeader = (ObservationHeader) bundle.getSerializable("Observation");
                    gpsList = new ArrayList<>();
                    gpsList.add(observationHeader);
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }
            SupportMapFragment mapFragment = (SupportMapFragment)
                    getSupportFragmentManager()
                            .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);

       /* gpsList = new ArrayList<>();
        objUserGpsInfo = new UserGpsInfo();
        objUserGpsInfo.setLatitude("17.4934");
        objUserGpsInfo.setLangitude("78.3278");
        objUserGpsInfo.setAddress("MARAL AGENCY");
        gpsList.add(objUserGpsInfo);
        objUserGpsInfo = new UserGpsInfo();
        objUserGpsInfo.setLatitude("17.5171");
        objUserGpsInfo.setLangitude("78.3157");
        objUserGpsInfo.setAddress("FABRIC AGENCY");
        gpsList.add(objUserGpsInfo);
        objUserGpsInfo = new UserGpsInfo();
        objUserGpsInfo.setLatitude("17.3685");
        objUserGpsInfo.setLangitude("78.5316");
        objUserGpsInfo.setAddress("Gokul Enterprises");
        gpsList.add(objUserGpsInfo);
        objUserGpsInfo = new UserGpsInfo();
        objUserGpsInfo.setLatitude("17.4699");
        objUserGpsInfo.setLangitude("78.3578");
        objUserGpsInfo.setAddress("Galaxy Agencies");
        gpsList.add(objUserGpsInfo);*/
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
       /* try {
            mMap.setMyLocationEnabled(true);
        }catch (SecurityException e){
            e.getMessage();
        }*/
            if (flag.equalsIgnoreCase("Observation")) {
                addGoogleMapMarker(gpsList);
            } else {

                getObservationViewList();
            }


            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                // Use default InfoWindow frame
                @Override
                public View getInfoWindow(Marker arg0) {
                    //LatLng latLng = arg0.getPosition();
                    //StringBuilder stringBuilder = new StringBuilder();
                    //String strAddress = getAddressFromLocation(latLng.latitude, latLng.longitude);
                    View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
                    TextView tvid = (TextView) v.findViewById(R.id.tv_id);
                    TextView tvname = (TextView) v.findViewById(R.id.tv_cust);
                    TextView tvaddrs = (TextView) v.findViewById(R.id.tv_addrss);

                    tvid.setTypeface(Utilities.fontRegular(getApplicationContext()));
                    tvname.setTypeface(Utilities.fontRegular(getApplicationContext()));
                    tvaddrs.setTypeface(Utilities.fontRegular(getApplicationContext()));
                    for (int i = 0; i < gpsList.size(); i++) {
                        if (arg0.getTitle().equalsIgnoreCase(gpsList.get(i).getObservationId())) {

                            tvid.setText(gpsList.get(i).getObservationId());
                            tvname.setText(gpsList.get(i).getCustomerName());
                            tvaddrs.setText(gpsList.get(i).getAddress());
                        }
                    }


                    return v;
                }

                // Defines the contents of the InfoWindow
                @Override
                public View getInfoContents(Marker arg0) {

                    // Getting view from the layout file info_window_layout
//                    LatLng latLng = arg0.getPosition();
//                    String strAddress = getAddressFromLocation(latLng.latitude,latLng.longitude);
//                    View v = getActivity().getLayoutInflater().inflate(R.layout.info_window_layout, null);
//                    TextView tvname = (TextView) v.findViewById(R.id.tv_engineername);
//                    tvname.setText(strAddress);
//                    tvname.setTypeface(Utilities.fontRegular(getActivity()));
//                    return v;
                    return null;

                }
            });
            //Initialize Google Play Services
        /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }*/
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /* protected synchronized void buildGoogleApiClient() {
         mGoogleApiClient = new GoogleApiClient.Builder(this)
                 .addConnectionCallbacks(this)
                 .addOnConnectionFailedListener(this)
                 .addApi(LocationServices.API)
                 .build();
         mGoogleApiClient.connect();
     }

     @Override
     public void onConnected(Bundle bundle) {
         mLocationRequest = new LocationRequest();
         mLocationRequest.setInterval(1000);
         mLocationRequest.setFastestInterval(1000);
         mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
         if (ContextCompat.checkSelfPermission(this,
                 Manifest.permission.ACCESS_FINE_LOCATION)
                 == PackageManager.PERMISSION_GRANTED) {
             LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                     mLocationRequest, this);
         }
     }

     @Override
     public void onConnectionSuspended(int i) {
     }

     @Override
     public void onLocationChanged(Location location) {
         mLastLocation = location;
         if (mCurrLocationMarker != null) {
             mCurrLocationMarker.remove();
         }
 //Showing Current Location Marker on Map
         LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
         MarkerOptions markerOptions = new MarkerOptions();
         markerOptions.position(latLng);
         LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
         String provider = locationManager.getBestProvider(new Criteria(), true);
         if (ActivityCompat.checkSelfPermission(this,
                 Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                 ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                         != PackageManager.PERMISSION_GRANTED) {
             return;
         }
         Location locations = locationManager.getLastKnownLocation(provider);
         List<String> providerList = locationManager.getAllProviders();
         if (null != locations && null != providerList && providerList.size() > 0) {
             double longitude = locations.getLongitude();
             double latitude = locations.getLatitude();
             Geocoder geocoder = new Geocoder(getApplicationContext(),
                     Locale.getDefault());
             try {
                 List<Address> listAddresses = geocoder.getFromLocation(latitude,
                         longitude, 1);
                 if (null != listAddresses && listAddresses.size() > 0) {
                     String state = listAddresses.get(0).getAdminArea();
                     String country = listAddresses.get(0).getCountryName();
                     String subLocality = listAddresses.get(0).getSubLocality();
                     markerOptions.title("" + latLng + "," + subLocality + "," + state
                             + "," + country);
                 }
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
         markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
         mCurrLocationMarker = mMap.addMarker(markerOptions);
         mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
         mMap.animateCamera(CameraUpdateFactory.zoomTo(6));
         if (mGoogleApiClient != null) {
             LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                     this);
         }
     }

     @Override
     public void onConnectionFailed(ConnectionResult connectionResult) {
     }
 */
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            //buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void addGoogleMapMarker(List<ObservationHeader> list) {
        try {
            for (int i = 0; i < list.size(); i++) {

                double latitude = Double.parseDouble(list.get(i).getLatitude());
                double longitude = Double.parseDouble(list.get(i).getLongitude());
                // String strAddress = getAddressFromLocation(latitude, longitude);
                LatLng latLng = new LatLng(latitude, longitude);
                MarkerOptions marker = new MarkerOptions().position(latLng);
                // ROSE color icon
                marker.title(list.get(i).getObservationId());
                if (list.get(i).getStatus().equalsIgnoreCase("Open")) {
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                } else if (list.get(i).getStatus().equalsIgnoreCase("Assigned")) {
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                } else {
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }
                //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loaction));
                // adding marker
                mMap.addMarker(marker);
            }
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(20.5937, 78.9629)).zoom(4.6f).build();
            //CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(17.3850, 78.4867)).zoom(10).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

   /* private void addGoogleMarker(ObservationHeader observationHeader) {
        try {
//            for (int i = 0; i < list.size(); i++) {

            double latitude = Double.parseDouble(observationHeader.getLatitude());
            double longitude = Double.parseDouble(observationHeader.getLongitude());
            String strAddress = getAddressFromLocation(latitude, longitude);
            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions marker = new MarkerOptions().position(latLng);
            // ROSE color icon
            marker.title(observationHeader.getObservationId());

            //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loaction));
            // adding marker
            mMap.addMarker(marker);
//            }
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(20.5937, 78.9629)).zoom(4.6f).build();
            //CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(17.3850, 78.4867)).zoom(10).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/

    /*private String getAddressFromLocation(double latitude, double longitude) {
        // TODO Auto-generated method stub
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String str_Location = "";
        // String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude,
                    longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < addressList.size(); i++) {
                    sb.append(address.getAddressLine(i));
                }
                *//*
     * sb.append(addressList.get(0).getLocality()).append("\n");
     * sb.append(addressList.get(0).getPostalCode()).append("\n");
     * sb.append(address.getCountryName());
     *//*
                str_Location = sb.toString();
                // Toast.makeText(getActivity(), "Address:"+str_Location,
                // Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            // Log.e(TAG, "Unable connect to Geocoder", e);
        }
        return str_Location;
    }
*/
    private void getObservationViewList() {
        try {
            if (Utilities.isConnectingToInternet(getApplicationContext())) {
                SharedPreferences sharedPreferences = getSharedPreferences("Bearer", MODE_PRIVATE);
                ApiInterface apiInterface = RetrofitConnect.getClient().create(ApiInterface.class);
                Call<ObservationResponse> call = apiInterface.getAllObservations("Bearer " + sharedPreferences.getString("Bearertoken", null));
                final ProgressDialog progressDialog = new ProgressDialog(GoogleMapActivity.this, R.style.MyDialogTheme);
                progressDialog.setMessage("Please wait verifying....");
                //progressDialog.setTitle("ProgressDialog");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                // show it
                progressDialog.show();
                //observationDataArrayList = new ArrayList<>();
                call.enqueue(new Callback<ObservationResponse>() {
                    public void onResponse(Call<ObservationResponse> call, Response<ObservationResponse> response) {
                        int statusCode = response.code();
                        if (response.isSuccessful()) {
                            if (response.body().getResult() != null) {
                                ObservationResponse loginResponse = response.body();
                                Log.d("observationlistview", loginResponse.getSuccess() + "");
                                if (loginResponse.getSuccess()) {
                                    progressDialog.dismiss();
                                    gpsList = new ArrayList<>();
                                    if (response.body().getResult() != null) {
                                        for (ObservationHeader observationViewResult : response.body().getResult()) {
                                            if (observationViewResult.getLatitude() != null && observationViewResult.getLatitude().length() > 0) {
                                        /*objUserGpsInfo = new UserGpsInfo();
                                        objUserGpsInfo.setLatitude(observationViewResult.getLatitude());
                                        objUserGpsInfo.setLangitude(observationViewResult.getLongitude());
                                        objUserGpsInfo.setUserID(observationViewResult.getObservationId())*/

                                                gpsList.add(observationViewResult);
                                            }
                                        }
                                        Log.d("ArrayListSize", gpsList.size() + "");
                                        addGoogleMapMarker(gpsList);
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Utilities.showAlertDialog(loginResponse.getErrors() + "", GoogleMapActivity.this);
                                    //showToast(loginResponse.getErrors() + "");
                                }

                            } else {
                                progressDialog.dismiss();
                                Utilities.showAlertDialog("No data to display", GoogleMapActivity.this);

                            }
                        } else {
                            progressDialog.dismiss();
                            Utilities.showAlertDialog("No data to display", GoogleMapActivity.this);
                        }


                    }

                    public void onFailure(Call<ObservationResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.d("LoginResponse", t.getMessage() + "");
                    }

                });
            } else {
                Utilities.showAlertDialog("Please check your internet connection", GoogleMapActivity.this);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
}

