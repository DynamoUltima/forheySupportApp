package app.com.forheypanel.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.Switch;
import com.squareup.picasso.Picasso;

import app.com.forheypanel.R;
import app.com.forheypanel.adapter.NotificationAdapter;
import app.com.forheypanel.model.SuccessClass;
import app.com.forheypanel.tools.App;
import butterknife.Bind;
import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayram on 12/10/15.
 */
public class ActivityPriceDetails extends BaseActivity {

    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvCat)
    TextView tvCat;

    @Bind(R.id.etPrice)
    EditText etPrice;

    @Bind(R.id.etLaundry)
    EditText etLaundry;

    @Bind(R.id.etDryCleaning)
    EditText etDryCleaning;

    @Bind(R.id.tvLaundryEdit)
    TextView tvLaundryEdit;

    @Bind(R.id.tvDCEdit)
    TextView tvDCEdit;

    @Bind(R.id.tvEdit)
    TextView tvEdit;


    @Bind(R.id.btnUpdate)
    Button btnUpdate;

    @Bind(R.id.imageView)
    ImageView img;

    @Bind(R.id.layout_wash_n_fold)
    LinearLayout layout;

    @Bind(R.id.layout_dryClean)
            LinearLayout layout_dryClean;

    @Bind(R.id.layout_laundry)
            LinearLayout layout_laundry;

    int visible,resVisible=0,id;
    @Bind(R.id.swVisibility)
    Switch swVisibility;

    @Bind(R.id.swType)
            Switch swType;

    String price="",laundry,dry_cleaning;

    ProgressDialog progressDialog;
    boolean isWashAndFold=false;
    View resultBox;
    TextView tvTitle,tvMessage;
    MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fadeInActivity();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_price);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle=getIntent().getExtras();
        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Updating Price");

        resultBox= LayoutInflater.from(this).inflate(R.layout.layout_dialog_box,null);
        tvTitle=(TextView)resultBox.findViewById(R.id.tvTitle);
        tvMessage=(TextView)resultBox.findViewById(R.id.tvMessage);
        materialDialog=new MaterialDialog(this);
        materialDialog.setView(resultBox);
        materialDialog.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
            }
        });

        if (bundle!=null){

            tvName.setText(bundle.getString("name",""));
            tvCat.setText(bundle.getString("Category",""));
            etPrice.setText(bundle.getString("price",""));
            laundry=bundle.getString("laundry","");
            dry_cleaning=bundle.getString("dry_cleaning","");
            isWashAndFold=bundle.getBoolean("wash_n_fold");

            if (laundry!=null){
                if (!laundry.isEmpty())
                etLaundry.setText(laundry);
                else{
                    etLaundry.setText("0.00");
                }
            }else{
                etLaundry.setText("0.00");
            }

            if (dry_cleaning!=null)
                if (!dry_cleaning.isEmpty()) {
                    etDryCleaning.setText(dry_cleaning);
                }else {
                    etDryCleaning.setText("0.00");
                }
            else{
                etDryCleaning.setText("0.00");
            }

            etPrice.setEnabled(false);
            price=bundle.getString("price","");

            if (price!=null){

                if (!price.isEmpty()){
                    layout.setVisibility(View.VISIBLE);
                    layout_dryClean.setVisibility(View.GONE);
                    layout_laundry.setVisibility(View.GONE);
                }

            }

            id=bundle.getInt("item_id",0);
            String image=bundle.getString("image","");
            visible=bundle.getInt("visible");

            if (image !=null){
                if (!image.isEmpty()){
                    Picasso.with(this)
                            .load(image)
                            .placeholder(R.drawable.ic_photos)
                            .into(img);
                }
            }

            if (visible ==0){
                swVisibility.setChecked(true);
            }

            if (isWashAndFold){
                layout_dryClean.setVisibility(View.GONE);
                layout_laundry.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                etPrice.setText(price);
            }
            etPrice.setEnabled(false);
            /*if (swType.isChecked()){
                layout_dryClean.setVisibility(View.VISIBLE);
            }else{
                layout_dryClean.setVisibility(View.GONE);
            }*/

            tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etPrice.setEnabled(true);
//                    etPrice.setFocusable(true);
                    requestFocus(etPrice);
                }
            });

            swVisibility.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(Switch view, boolean checked) {
                    if (checked) {
                        resVisible = 0;
                        visible=0;
                    } else {
                        resVisible = 1;
                        visible=1;
                    }
                }
            });

            swType.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(Switch view, boolean checked) {
                    if (checked){
                        layout_laundry.setVisibility(View.GONE);
                        layout_dryClean.setVisibility(View.VISIBLE);
                    }else{
                        layout_laundry.setVisibility(View.VISIBLE);
                        layout_dryClean.setVisibility(View.GONE);
                    }
                }
            });

            tvLaundryEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etLaundry.setEnabled(true);
                    requestFocus(etLaundry);
                }
            });

            tvDCEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etDryCleaning.setEnabled(true);
                    requestFocus(etDryCleaning);
                }
            });

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (isWashAndFold){
                        if (!etPrice.getText().toString().isEmpty()) {
                            progressDialog.show();
                            App.supportService.updateWash_n_fold("update_wash_fold",id,etPrice.getText().toString())
                                    .enqueue(new Callback<SuccessClass>() {
                                        @Override
                                        public void onResponse(Call<SuccessClass> call, Response<SuccessClass> response) {
                                            progressDialog.dismiss();
                                            if (response.body().success == 1) {
                                                setResult(120);
                                                tvTitle.setText("Price Info");
                                                tvMessage.setText("Price Updated Sucessfully");
                                                materialDialog.show();

                                            } else {

                                                Toast.makeText(etPrice.getContext(),
                                                        "Sorry, you failed to update this item", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<SuccessClass> call, Throwable t) {
                                            progressDialog.dismiss();
                                            Toast.makeText(etPrice.getContext(),
                                                    "Sorry, you failed to update this item", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        } else Toast.makeText(etPrice.getContext(),"Price must be entered",Toast.LENGTH_SHORT).show();
                    }else {
                        if (!etLaundry.getText().toString().isEmpty()|| !etDryCleaning.getText().toString().isEmpty()) {
                            progressDialog.show();
                            App.supportService.updateItemPrices("update_price",id,etLaundry.getText().toString(), etDryCleaning.getText().toString(), etPrice.getText().toString(), visible)
                                    .enqueue(new Callback<SuccessClass>() {
                                        @Override
                                        public void onResponse(Call<SuccessClass> call, Response<SuccessClass> response) {
                                            progressDialog.dismiss();
                                            if (response.body().success == 1) {
                                                setResult(120);
                                                tvTitle.setText("Price Info");
                                                tvMessage.setText("Laundry Price : " + response.body().laundry + "\nDry Cleaning Price : " + response.body().dry_cleaning);

                                                materialDialog.show();

                                            } else {

                                                Toast.makeText(etPrice.getContext(),
                                                        "Sorry, you failed to update this item", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<SuccessClass> call, Throwable t) {
                                            progressDialog.dismiss();
                                            Toast.makeText(etPrice.getContext(),
                                                    "Sorry, you failed to update this item", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        }

                    }


                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
