package app.com.forheypanel.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.rey.material.widget.Switch;
import app.com.forheypanel.R;
import app.com.forheypanel.model.Client;
import app.com.forheypanel.tools.App;
import butterknife.Bind;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityClient extends BaseActivity{

    @Bind(R.id.etCrName)
    EditText etName;

    @Bind(R.id.etCrEmail)
    EditText etEmail;

    @Bind(R.id.etCrloc)
    EditText etLocation;

    @Bind(R.id.etCrhouseNum)
    EditText ethouseNum;

    @Bind(R.id.etCrOther)
    EditText etOther;

    @Bind(R.id.etCrPhone)
    EditText etPhone;

    @Bind(R.id.etCrRef)
    EditText etRef;

    @Bind(R.id.etCrStName)
    EditText etStreetName;

    @Bind(R.id.etCompanyName)
    EditText etCompany;

    @Bind(R.id.swAddress)
    Switch swAddress;

    @Bind(R.id.btnMap)
    Button btnMap;

    @Bind(R.id.llLayout)
    LinearLayout linearLayout;



    String home_location="",home_street="",home_hseNum="",home_ref="",office_location="",office_street="",office_hseNum="",office_ref="",
    office_other="",home_other="",office_company="",home_lat="",home_lng="",office_lat="",office_lng="",lat="",lng="",location="";
    String TAG=getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle=getIntent().getExtras();
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        if (bundle !=null){
            int id=bundle.getInt("client_id");
            progressDialog.show();
            App.supportService.getClientdataById("getClientDataById",id).enqueue(new Callback<Client>() {
                @Override
                public void onResponse(Call<Client> call, Response<Client> response) {
                    progressDialog.dismiss();
                    if (response.body().success == 1) {
                        linearLayout.setVisibility(View.VISIBLE);
                        if (response.body().name != null && !response.body().name.contains("null")) {
                            etName.setText(response.body().name);
                        }
                        if (response.body().email != null && !response.body().email.contains("null")) {
                            etEmail.setText(response.body().email);
                        }

                        if (response.body().location != null && !response.body().location.contains("null")) {
                            location = response.body().location;
                            etLocation.setText(response.body().location);
                        }


                        if (response.body().phone != null && !response.body().phone.contains("null")) {
                            etPhone.setText(response.body().phone);
                        }
                        if (response.body().home_other_location != null && !response.body().home_other_location.contains("null")) {
                            home_other = response.body().home_other_location;

                        }
                        if (response.body().home_location != null && !response.body().home_location.contains("null") && !response.body().home_location.isEmpty()) {
                            etLocation.setText(response.body().home_location);

                            if (home_location.contains("Other")) {

                                etOther.setText(home_other);
                                etOther.setVisibility(View.VISIBLE);
                            } else {
                                etOther.setVisibility(View.GONE);
                            }
                        }


                        if (response.body().office_other_location != null && !response.body().office_other_location.contains("null")) {
                            office_other = response.body().office_other_location;
                        }

                        if (response.body().home_house_number != null && !response.body().home_house_number.contains("null")) {
                            ethouseNum.setText(response.body().home_house_number);
                            home_hseNum = response.body().home_house_number;
                        }

                        if (response.body().home_reference != null && !response.body().home_reference.contains("null")) {
                            etRef.setText(response.body().home_reference);
                            home_ref = response.body().home_reference;
                        }

                        if (response.body().home_street_name != null && !response.body().home_street_name.contains("null")) {
                            etStreetName.setText(response.body().home_street_name);
                            home_street = response.body().home_street_name;
                        }

                        if (response.body().office_location != null && !response.body().office_location.contains("null")) {
                            office_location = response.body().office_location;
                        }

                        if (response.body().office_reference != null && !response.body().office_reference.contains("null")) {
                            office_ref = response.body().office_reference;
                        }

                        if (response.body().office_house_number != null && !response.body().office_house_number.contains("null")) {
                            office_hseNum = response.body().office_house_number;
                        }

                        if (response.body().office_street_name != null && !response.body().office_street_name.contains("null")) {
                            office_street=response.body().office_street_name;
                        }

                        if (response.body().office_latitude != null && !response.body().office_latitude.contains("null")) {
                            office_lat = response.body().office_latitude;
                        }
                        if (response.body().office_longitude != null && !response.body().office_longitude.contains("null")) {
                            office_lng = response.body().office_longitude;
                        }

                        if (response.body().home_latitude != null && !response.body().home_latitude.contains("null")) {
                            home_lat = response.body().home_latitude;
                            lat = home_lat;
                        }

                        if (response.body().home_longitude != null && !response.body().home_longitude.contains("null")) {
                            home_lng = response.body().home_longitude;
                            lng = home_lng;
                        }

                        if (response.body().company_name != null && !response.body().company_name.contains("null")) {
                            office_company = response.body().company_name;
                            etCompany.setText(response.body().company_name);
                        }


                    } else {
                        Toast.makeText(ActivityClient.this, response.body().error_msg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Client> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(ActivityClient.this, "Sorry, failed to load client's data", Toast.LENGTH_SHORT).show();

                }
            });


//            btnMap.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (lat != null && lat.isEmpty() && lng != null && !lng.isEmpty()) {
//                        if (!lat.contains("0.0") && !lng.contains("0.0")) {
//                            Intent intent = new Intent(ActivityClient.this, GoogleMapLoc.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putDouble("latitude", Double.parseDouble(lat));
//                            bundle.putDouble("longitude", Double.parseDouble(lng));
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//
//                        } else {
//                            Toast.makeText(ActivityClient.this, "Coordinates not provided ", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(ActivityClient.this, "Coordinates not provided ", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        }

        swAddress.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if (checked){

                    etLocation.setText(office_location);

                    etRef.setText(office_ref);
                    etCompany.setText(office_company);
                    if (office_company!=null && !office_company.isEmpty() && !office_company.contains("null") && (office_location.isEmpty()|| office_location.contains("null"))){
                        etLocation.setText(location);
                    }
                    etCompany.setVisibility(View.VISIBLE);

                    if (office_location.contains("Other")){
                        etOther.setText(office_other);
                        etOther.setVisibility(View.VISIBLE);
                    }else {
                        etOther.setVisibility(View.GONE);
                    }
                    etStreetName.setText(office_street);
                    ethouseNum.setText(office_hseNum);
                    lat=office_lat;
                    lng=office_lng;

                }else{
                    if (!home_location.isEmpty())
                    etLocation.setText(home_location);
                    else
                        etLocation.setText(location);
                    Log.e(TAG,"location home "+home_location+"\nLocation "+location);
                    etRef.setText(home_ref);
                    etStreetName.setText(home_street);
                    ethouseNum.setText(home_hseNum);
                    if (home_location.contains("Other")){
                        etOther.setText(home_other);
                        etOther.setText(View.VISIBLE);
                    }else {
                        etOther.setVisibility(View.GONE);
                    }
                    etCompany.setVisibility(View.GONE);
                    lat=home_lat;
                    lng=home_lng;
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}


