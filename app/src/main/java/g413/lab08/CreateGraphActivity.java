package g413.lab08;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import java.io.IOException;

import g413.lab08.services.ApiService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CreateGraphActivity extends AppCompatActivity {

    String token;
    String name;
    String id;
    String action;
    ApiService service;
    EditText txtName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_graph);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.131:5000/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(ApiService.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            token = extras.getString("token");
            action = extras.getString("action");
            id = extras.getString("id");
            name = extras.getString("name");
        }

        txtName = findViewById(R.id.txtNameCreateGraph);

        if (action.contains("edit")){
            txtName.setText(name);
        }
    }


    public void onSave_createGraph(View v){
        if (action.contains("new")){
            Call<String> addGraph = service.add_graph(token, txtName.getText().toString());
            try {
                String r = addGraph.execute().body();
                if (r.contains("-1")){
                    Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Graph added successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            } catch (JsonIOException | IOException e) {
                Log.e("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH", e.toString());
                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        }

        else if (action.contains("edit")){
            Call<String> updGraph = service.update_graph(token, txtName.getText().toString(), id);
            try {
                String r = updGraph.execute().body();
                Toast.makeText(this, r, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } catch (JsonIOException | IOException e) {
                Log.e("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH", e.toString());
                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onBackPressed()
    {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onCancel_createGraph(View v){
        setResult(RESULT_CANCELED);
        finish();
    }
}