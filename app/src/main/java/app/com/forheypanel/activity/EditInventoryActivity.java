package app.com.forheypanel.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.com.forheypanel.R;
import app.com.forheypanel.model.Inventory;
import app.com.forheypanel.tools.App;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayrammensah on 9/21/17.
 */

public class EditInventoryActivity extends AppCompatActivity {

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

    @Bind(R.id.garment_price)
    TextView garmentPrice;

    double currentPrice;

    @Bind(R.id.edtWeight)
    EditText edtWeight;

    @Bind(R.id.textView8)
    TextView weightText;

    @Bind(R.id.textView7)
    TextView quantityText;



    public static Inventory item;

    String type="";

    ProgressDialog progressDialog;

    private List<CountryItem> countryList;

    AutoCompleteCountryAdapter adapter;
    String amount;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_layout);
        ButterKnife.bind(this);

        getSupportActionBar().setHomeButtonEnabled(true);

//        String[] countries = getResources().getStringArray(R.array.list_of_garments);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                (this,android.R.layout.simple_list_item_1,countryList);


        fillCountryList();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Updating Inventory..");
        progressDialog.setIndeterminate(false);

        progressDialog.setCancelable(false);
        rbWashFold.setChecked(true);
        btnSaveInv.setText("Update");
        type=item.getType();
        quantityText.setVisibility(View.GONE);
        edtQuantity.setVisibility(View.GONE);

        adapter = new AutoCompleteCountryAdapter(this, countryList);
        actGarment.setAdapter(adapter);
        actGarment.setOnItemClickListener(onItemClickListener);


        actGarment.setText(item.getItem());
        edtQuantity.setText(item.getNoOfITems());
        switch (item.getType()){
            case "Wash & Fold":
                rbWashFold.setChecked(true);
                break;
            case "Laundry":
                rbLaundry.setChecked(true);
                break;
            case "Dry Cleaning":
                rbDryClean.setChecked(true);
                break;
            case "Press Only":
                rbPressOnly.setChecked(true);
                break;
        }


        rgService.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rdWashFold:
//                        item.setType("Wash & Fold");
                        type = "Wash & Fold";
                        edtWeight.setVisibility(View.VISIBLE);
                        weightText.setVisibility(View.VISIBLE);
                        edtQuantity.setVisibility(View.GONE);
                        quantityText.setVisibility(View.GONE);
                        actGarment.setVisibility(View.VISIBLE);


                        break;
                    case R.id.rdDryClean:
//                        item.setType();
                        type = "Dry Cleaning";
                        edtWeight.setVisibility(View.GONE);
                        weightText.setVisibility(View.GONE);
                        quantityText.setVisibility(View.VISIBLE);
                        edtQuantity.setVisibility(View.VISIBLE);
                        actGarment.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rdPressOnly:
                        type = "Press Only";
                        edtWeight.setVisibility(View.GONE);
                        weightText.setVisibility(View.GONE);
                        quantityText.setVisibility(View.VISIBLE);
                        edtQuantity.setVisibility(View.VISIBLE);
                        actGarment.setVisibility(View.VISIBLE);

                        break;
                    case R.id.rdLaundry:
                        type = "Laundry";
                        edtWeight.setVisibility(View.GONE);
                        weightText.setVisibility(View.GONE);
                        quantityText.setVisibility(View.VISIBLE);
                        edtQuantity.setVisibility(View.VISIBLE);
                        actGarment.setVisibility(View.VISIBLE);


                        break;
                }
            }
        });

        btnSaveInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()){
                    updateItem();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    String myQuantity = edtQuantity.getText().toString();
                    final String myWeight = edtWeight.getText().toString();
                    if (type.equals("Dry Cleaning")) {
                        //edtQuantity.addTextChangedListener();
                        if (!myQuantity.isEmpty()) {
                            currentPrice = adapter.getItem(i).getDryCleaning() * Double.parseDouble(edtQuantity.getText().toString());
                            garmentPrice.setText("GH¢"+String.valueOf(currentPrice));
                        } else {
                            edtQuantity.setError("Enter Quantity");
                        }
                    } else if (type.equals("Press Only")) {
                        if (!myQuantity.isEmpty()) {
                            currentPrice = adapter.getItem(i).getPressOnly() * Double.parseDouble(edtQuantity.getText().toString());
                            garmentPrice.setText("GH¢ "+String.valueOf(currentPrice));
                        } else {
                            edtQuantity.setError("Enter Quantity");
                        }

                    } else if (type.equals("Laundry")) {
                        if (!myQuantity.isEmpty()) {
                            currentPrice = adapter.getItem(i).getLaundry() * Double.parseDouble(edtQuantity.getText().toString());
                            garmentPrice.setText("GH¢"+String.valueOf(currentPrice));
                        } else {
                            edtQuantity.setError("Enter Quantity");
                        }

                    } else if (type.equals("Wash & Fold")) {
                        final String et = edtWeight.getText().toString();

                        try {
                            //double weightValue = Double.parseDouble(et);
                            Integer weightValue = Integer.parseInt(et);


                            if (!myWeight.isEmpty()) {
                                if (weightValue < 5) {
                                    currentPrice = 45.0;
                                    garmentPrice.setText("GH¢ "+String.valueOf(currentPrice));

                                } else if (weightValue > 5 &&
                                        weightValue < 10) {
                                    currentPrice = 75.0;
                                    garmentPrice.setText(String.valueOf(currentPrice));
                                } else if (weightValue >= 10 &&
                                        weightValue <= 12) {
                                    currentPrice = 100.0;
                                    garmentPrice.setText("GH¢"+String.valueOf(currentPrice));
                                } else {
                                    edtWeight.setError("Enter Weight");
                                }
                            }

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Toast.makeText(EditInventoryActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }


                    }

                }
            };

    boolean validateInput(){
        boolean result=true;
        if (actGarment.getText().toString().isEmpty()){
            Toast.makeText(EditInventoryActivity.this,"Enter Garment Type",Toast.LENGTH_SHORT).show();
            result =false;}
//        }else if(edtQuantity.getText().toString().isEmpty()) {
//            Toast.makeText(EditInventoryActivity.this,"Enter Quantity",Toast.LENGTH_SHORT).show();
//            result=false;
//        }
        return result;
    }

    private void fillCountryList() {
        countryList = new ArrayList<>();
        countryList.add(new CountryItem("Shirt", 7.00, 12.00, 5.00));
        countryList.add(new CountryItem("T-Shirt", 5.00, 7.00, 3.00));
        countryList.add(new CountryItem("Linen Shirt", 9.00, 14.00, 7.00));
        countryList.add(new CountryItem("Silk Shirt", 11.00, 14.00, 9.00));
        countryList.add(new CountryItem("Blouse", 7.00, 9.00, 5.00));
        countryList.add(new CountryItem("Silk Blouse", 9.00, 11.00, 7.00));
        countryList.add(new CountryItem("Blouse with Cap", 7.00, 11.00, 5.00));
        countryList.add(new CountryItem("Skirt", 7.00, 11.00, 5.00));
        countryList.add(new CountryItem("Pleated Long Skirt", 12.00, 15.00, 10.00));
        countryList.add(new CountryItem("Blouse & Skirt", 14.00, 20.00, 12.00));
        countryList.add(new CountryItem("Wedding Gown with veil & underskirt", 120.00, 130.00, 100.00));
        countryList.add(new CountryItem("Wedding Gown  ", 118.00, 130.00, 80.00));
        countryList.add(new CountryItem("Shorts", 8.00, 11.00, 4.00));
        countryList.add(new CountryItem("Short Dress", 14.00, 18.00, 6.00));
        countryList.add(new CountryItem("Suit 2pc (Ladies)", 29.00, 35.00, 25.00));
        countryList.add(new CountryItem("Suit 2pc  ", 33.00, 38.00, 28.00));
        countryList.add(new CountryItem("Suit 3pc", 40.00, 45.00, 35.00));
        countryList.add(new CountryItem("Suit Vest", 8.50, 12.00, 7.00));
        countryList.add(new CountryItem("Political Suit", 29.00, 35.00, 25.00));
        countryList.add(new CountryItem("Jumpsuit", 25.00, 25.00, 7.00));
        countryList.add(new CountryItem("Trouser", 14.00, 19.00, 7.00));
        countryList.add(new CountryItem("Khaki Trouser", 9.00, 19.00, 7.00));
        countryList.add(new CountryItem("Ladies Trouser", 16.00, 21.00, 7.00));
        countryList.add(new CountryItem("Singlet", 5.00, 7.00, 3.00));
        countryList.add(new CountryItem("Caftan Top", 9.00, 13.00, 6.00));
        countryList.add(new CountryItem("Caftan 2pc", 26.00, 29.00, 15.00));
        countryList.add(new CountryItem("Caftan 3pc", 32.00, 35.00, 18.00));
        countryList.add(new CountryItem("Boxer", 5.00, 7.00, 2.00));
        countryList.add(new CountryItem("Slit & Kaba 2pc", 19.00, 21.00, 10.00));
        countryList.add(new CountryItem("Slit & Kaba 3pc", 21.00, 28.00, 15.00));
        countryList.add(new CountryItem("Kente Cloth for Men", 35.00, 50.00, 18.00));
        countryList.add(new CountryItem("Ord. Kente Cloth", 20.00, 29.00, 12.00));
        countryList.add(new CountryItem("Kente Long Dress", 25.00, 29.00, 15.00));
        countryList.add(new CountryItem("Smock", 24.00, 29.00, 20.00));
        countryList.add(new CountryItem("Pullover", 12.00, 19.00, 9.00));
        countryList.add(new CountryItem("Scalf", 4.00, 6.00, 2.00));
        countryList.add(new CountryItem("Socks", 2.00, 5.00, 0));
        countryList.add(new CountryItem("Tie", 7.00, 7.00, 3.00));
        countryList.add(new CountryItem("Jacket", 21.00, 21.00, 15.00));
        countryList.add(new CountryItem("Jeans", 9.00, 14.00, 6.00));
        countryList.add(new CountryItem("Ladies Jacket", 19.00, 19.00, 12.00));
        countryList.add(new CountryItem("Winter Jacket", 25.00, 25.00, 15.00));
        countryList.add(new CountryItem("Face Towel", 4.00, 5.00, 3.00));
        countryList.add(new CountryItem("Towel (Large)", 12.00, 0, 0));
        countryList.add(new CountryItem("Towel (Medium)", 9.00, 0, 0));
        countryList.add(new CountryItem("Towel (Small)", 6.00, 0, 0));
        countryList.add(new CountryItem("Floor Towel", 13.00, 0, 0));
        countryList.add(new CountryItem("Bed Sheet (King Size)", 18.00, 0, 0));
        countryList.add(new CountryItem("Bed Sheet (Queen Size)", 14.00, 0, 0));
        countryList.add(new CountryItem("Bed Sheet (Small)", 9.00, 0, 0));
        countryList.add(new CountryItem("Bed Cover", 18.00, 0, 7.00));
        countryList.add(new CountryItem("Bed Spread", 22.00, 0, 0));
        countryList.add(new CountryItem("Duvet (King Size)", 41.00, 0, 0));
        countryList.add(new CountryItem("Duvet (Queen Size)", 35.00, 0, 0));
        countryList.add(new CountryItem("Duvet (Small)", 29.00, 0, 0));
        countryList.add(new CountryItem("Duvet Cover", 25.00, 0, 15.00));
        countryList.add(new CountryItem("Blanket", 29.00, 0, 18.00));
        countryList.add(new CountryItem("Pillow Case", 4.00, 0, 2.00));
        countryList.add(new CountryItem("Pillow  ", 0, 12.00, 0));
        countryList.add(new CountryItem("Napkin", 4.00, 0, 0));
        countryList.add(new CountryItem("Apron ", 7.00, 0, 0));
//        countryList.add(new CountryItem("", , , ));


    }

    void updateItem(){

        if (type.equals("Wash & Fold")) {
            amount = edtWeight.getText().toString();
            Toast.makeText(this, amount, Toast.LENGTH_SHORT).show();
        } else {
            amount = edtQuantity.getText().toString();
            Toast.makeText(this, amount, Toast.LENGTH_SHORT).show();
        }
        progressDialog.show();
        App.supportService.updateInventory(item.getId(),actGarment.getText().toString(),
                amount,type,"UpdateInventory", currentPrice).enqueue(new Callback<Inventory>() {
            @Override
            public void onResponse(Call<Inventory> call, Response<Inventory> response) {
                progressDialog.dismiss();
                if (response.isSuccessful())
                    if (response.body().success==1){
                       setResult(Activity.RESULT_OK);
                        finish();
                    }else{
                        Toast.makeText(EditInventoryActivity.this,"Update Failed",Toast.LENGTH_SHORT).show();
                    }
                else Toast.makeText(EditInventoryActivity.this, "Update failed! Please try again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Inventory> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditInventoryActivity.this, "Update failed! Please check intenet connection", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
