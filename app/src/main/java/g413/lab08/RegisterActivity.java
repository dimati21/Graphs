package g413.lab08;

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

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    ApiService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        username = findViewById(R.id.txtLoginReg);
        password = findViewById(R.id.txtPasswordReg);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.131:5000/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(ApiService.class);
    }
    public void onRegister_reg(View v)
    {
        Call<String> createacc = service.register(username.getText().toString(), password.getText().toString());
        try {
            String r = createacc.execute().body();
            if (r.equals("Account has been created")){
                Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
                Toast.makeText(this, "Account already exists", Toast.LENGTH_SHORT).show();
        } catch (JsonIOException | IOException e) {
            Log.e("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH", e.toString());
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
        }
    }

    public void onLogin_reg(View v)
    {
        finish();
    }
}