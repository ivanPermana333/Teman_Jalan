package com.example.temanjalan.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.temanjalan.Constants;
import com.example.temanjalan.R;
import com.example.temanjalan.model.Match;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.temanjalan.activities.SignInActivity.ID;
import static com.example.temanjalan.activities.SignInActivity.SHARED_PREFS;

public class RequestActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {
    public static final String TAG = RequestActivity.class.getSimpleName();
    private EditText edtDate, edtNameTeam;
    private Button btnReq;
    private Spinner spinner;
    private String date, nameTeam, idField;
    private Toolbar toolbar;
    private TextView mTitle;
    private ArrayList<Match> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        toolbar = findViewById(R.id.toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        edtDate = findViewById(R.id.edt_date);
        edtNameTeam = findViewById(R.id.edt_name_team);
        spinner = findViewById(R.id.spinner);
        btnReq = findViewById(R.id.btn_req);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String title = getIntent().getStringExtra("Title");
        mTitle.setText(title);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(ID, "");

        getField();

        btnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameTeam = edtNameTeam.getText().toString().trim();
                System.out.println("Send data : " + date + " " + id + " " + nameTeam + " " + idField);
                AndroidNetworking.post(Constants.BASE_URL + "/api/match")
                        .addBodyParameter("date", date)
                        .addBodyParameter("user_id", id)
                        .addBodyParameter("field_id", idField)
                        .addBodyParameter("teamReq", nameTeam)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                                System.out.println("Respone : " + response);
                                Intent intent = new Intent(RequestActivity.this, MyMatchActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                                System.out.println("ANError " + error);
                            }
                        });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Match field = (Match) parent.getSelectedItem();
        String name = field.getName();
        idField = field.getIdField();
//        Toast.makeText(this, name + "\n" + idField, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

//    public void getDate(View view) {
//        DialogFragment datePicker = new DatePickerFragment();
//        datePicker.show(getSupportFragmentManager(), "date picker");
//    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = year + "-" + (month + 1) + "-" + dayOfMonth;
        edtDate.setText(date);
    }

    public void getField() {
        AndroidNetworking.get(Constants.BASE_URL + "/api/fieldAll")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                Match field = new Match();
                                field.setIdField(item.getString("id"));
                                field.setName(item.getString("name"));
                                list.add(field);
                            }
                            showSpinner();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        if (error.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            // get parsed error object (If ApiError is your class)
                            // ApiError apiError = error.getErrorAsObject(ApiError.class);
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });
    }

    public void showSpinner() {
        ArrayAdapter<Match> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
