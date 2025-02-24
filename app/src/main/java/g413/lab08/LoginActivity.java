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

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    ApiService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
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


        username = findViewById(R.id.txtLoginAuth);
        password = findViewById(R.id.txtPasswordAuth);
    }


    public void onLogin_auth(View v) {
        Call<String> signin = service.sign_in(username.getText().toString(), password.getText().toString());
        try {
            String r = signin.execute().body();
            if (r.contains("no account")) {
                Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(this, GraphsListActivity.class);
                intent.putExtra("token", r);
                startActivity(intent);
            }
        } catch (JsonIOException | IOException e) {
            Log.e("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH", e.toString());
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRegister_auth(View v) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}