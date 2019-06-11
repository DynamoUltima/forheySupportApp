package app.com.forheypanel.activity;

/**
 * Created by nayram on 3/10/15.
 */

//import android.app.Activity;
//import android.os.Bundle;
//import android.widget.Toast;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.CameraUpdate;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import app.com.forheypanel.R;
//
//
//public class GoogleMapLoc extends Activity {
//
//
//    private static GoogleMap googleMap;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //getActionBar().hide();
//        setContentView(R.layout.map_fragment);
//
//
//        try {
//            // Loading map
//            initilizeMap();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void initilizeMap() {
//        final Bundle getgroup=getIntent().getExtras();
//        if (googleMap == null) {
//            ((MapFragment) getFragmentManager().findFragmentById(
//                    R.id.map)).getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(GoogleMap googleMap) {
//                   GoogleMapLoc.googleMap =googleMap;
//                    MarkerOptions marker = new MarkerOptions().position(new LatLng(getgroup.getDouble("latitude"), getgroup.getDouble("longitude")));
//                    // adding marker
//                    // marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon)));
//                    //  googleMap.setMyLocationEnabled(true);
//                    CameraUpdate center= CameraUpdateFactory.newLatLng(new LatLng(getgroup.getDouble("latitude"),
//                            getgroup.getDouble("longitude")));
//                    CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
//                    googleMap.addMarker(marker);
//                    googleMap.moveCamera(center);
//                    googleMap.animateCamera(zoom);
//                    // check if map is created successfully or not
//                    if (googleMap == null) {
//                        Toast.makeText(getApplicationContext(),
//                                "Sorry! unable to create maps", Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                }
//            });
//
//        }
//    }
//}
