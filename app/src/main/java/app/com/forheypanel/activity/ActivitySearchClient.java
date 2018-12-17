package app.com.forheypanel.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import app.com.forheypanel.R;
import app.com.forheypanel.model.Client;
import app.com.forheypanel.tools.AdapterSearchClient;
import app.com.forheypanel.tools.App;
import app.com.forheypanel.tools.SimpleDividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nayram on 12/4/15.
 */
public class ActivitySearchClient extends BaseActivity {

    @Bind(R.id.etSearch)
    EditText etSearch;

    @Bind(R.id.recyclerview)
    RecyclerView rv;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;

    String TAG=getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search_user);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_action_ic_arrow_back_white_48dp);

        setSupportActionBar(toolbar);

        etSearch.addTextChangedListener(new MyTextWatcher(etSearch));
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setHasFixedSize(true);

        rv.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (!etSearch.getText().toString().trim().isEmpty()) {
                        progressWheel.setVisibility(View.VISIBLE);
                        App.supportService.getClientdata("SearchUserByName",etSearch.getText().toString()).enqueue(new Callback<Client.RawClientData>() {
                            @Override
                            public void onResponse(Call<Client.RawClientData> call, Response<Client.RawClientData> response) {
                                AdapterSearchClient adapterSearchClient=new AdapterSearchClient(response.body().client,ActivitySearchClient.this);
                                adapterSearchClient.notifyDataSetChanged();
                                rv.setAdapter(adapterSearchClient);
                                progressWheel.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<Client.RawClientData> call, Throwable t) {
                                progressWheel.setVisibility(View.GONE);
                                Toast.makeText(ActivitySearchClient.this, "Sorry, failed to load data", Toast.LENGTH_SHORT).show();

                            }
                        });


                    } else {
                    }
                    handled = true;
                }
                return handled;
            }
        });

    }

    void loadUser(String search_param){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            Log.e(TAG, "Editable " + editable.toString());


            App.supportService.getClientdata("SearchUserByName",editable.toString()).enqueue(new Callback<Client.RawClientData>() {
                @Override
                public void onResponse(Call<Client.RawClientData> call, Response<Client.RawClientData> response) {
                    AdapterSearchClient adapterSearchClient=new AdapterSearchClient(response.body().client,ActivitySearchClient.this);
                    adapterSearchClient.notifyDataSetChanged();
                    rv.setAdapter(adapterSearchClient);
                    progressWheel.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<Client.RawClientData> call, Throwable t) {
                    progressWheel.setVisibility(View.GONE);
                    Toast.makeText(ActivitySearchClient.this, "Sorry, failed to load data", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }


}
