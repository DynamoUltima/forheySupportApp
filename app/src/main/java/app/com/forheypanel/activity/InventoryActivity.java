package app.com.forheypanel.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import app.com.forheypanel.R;
import app.com.forheypanel.model.Inventory;
import app.com.forheypanel.tools.App;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayrammensah on 9/21/17.
 */

public class InventoryActivity extends AppCompatActivity {

    public static String orderId;
    @Bind(R.id.actGarment)
    AutoCompleteTextView actGarment;
    @Bind(R.id.edtQuantity)
    EditText edtQuantity;
    @Bind(R.id.rdWashFold)
    RadioButton rbWashFold;
    @Bind(R.id.rdDryClean)
    RadioButton rbDryClean;
    @Bind(R.id.rdLaundry)
    RadioButton rbLaundry;
    @Bind(R.id.rdPressOnly)
    RadioButton rbPressOnly;
    @Bind(R.id.btnSaveInv)
    Button btnSaveInv;
    @Bind(R.id.rgService)
    RadioGroup rgService;
    String type="Wash & Fold";

    ProgressDialog progressDialog;

    String TAG=getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_layout);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Saving Inventory..");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        rbWashFold.setChecked(true);

        String[] countries = getResources().getStringArray(R.array.list_of_garments);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,countries);
        actGarment.setAdapter(adapter);

        rgService.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rdWashFold:
//                        item.setType("Wash & Fold");
                        type="Wash & Fold";
                        break;
                    case R.id.rdDryClean:
//                        item.setType();
                        type="Dry Cleaning";
                        break;
                    case R.id.rdPressOnly:
                        type="Press Only";
                        break;
                    case R.id.rdLaundry:
                        type="Laundry";
                        break;
                }
            }
        });

        btnSaveInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()){
                    addInventoryItem();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setResult(RESULT_OK);
        finish();
        return super.onOptionsItemSelected(item);
    }

    boolean validateInput(){
        boolean result=true;
        if (actGarment.getText().toString().isEmpty()){
            Toast.makeText(InventoryActivity.this,"Enter Garment Type",Toast.LENGTH_SHORT).show();
            result =false;
        }else if(edtQuantity.getText().toString().isEmpty()) {
            Toast.makeText(InventoryActivity.this,"Enter Quantity",Toast.LENGTH_SHORT).show();
            result=false;
        }
        return result;
    }

    void addInventoryItem(){
        progressDialog.show();
        App.supportService.addInventory(orderId,actGarment.getText().toString(),edtQuantity.getText().toString(),
                type,"AddInventory").enqueue(new Callback<Inventory>() {
            @Override
            public void onResponse(Call<Inventory> call, Response<Inventory> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    Log.d(TAG,response.body().toString());
                    if (response.body().success==1){
                        Toast.makeText(InventoryActivity.this, "Garment added successfully", Toast.LENGTH_SHORT).show();
                        actGarment.getText().clear();
                        edtQuantity.getText().clear();
                        rbWashFold.setChecked(true);
                    }else{
                        Toast.makeText(InventoryActivity.this, "Failed to add Garment. Please try again", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(InventoryActivity.this, "Failed to add Garment. Please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Inventory> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(InventoryActivity.this, "Failed to add Garment. Check internet and try again", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
