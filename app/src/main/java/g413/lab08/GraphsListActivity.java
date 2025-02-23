package g413.lab08;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import java.io.IOException;
import java.util.List;

import g413.lab08.services.ApiService;
import g413.lab08.services.Graphs;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GraphsListActivity extends AppCompatActivity {
    ApiService service;
    String token;
    ListView list;
    ArrayAdapter<Graphs> adapter;
    int selected = -1;
    List<Graphs> graphs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_graphs_list);
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
                .baseUrl("http://192.168.0.101:5000/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(ApiService.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            token = extras.getString("token");
        }

        list = findViewById(R.id.list_graphs);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);
        updateList();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                Graphs tmp = adapter.getItem(selected);
                makeToast("Graph selected: " + tmp.toString());
            }
        });
    }

    public void onLogout_ListGraphs(View v) {
        closeSession();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeSession();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(requestCode == 1) {
            updateList();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onDelete_ListGraphs(View v) {
        Graphs tmp = adapter.getItem(selected);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Вы уверены?");
        dialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Call<String> delGraphs = service.delete_graph(token, tmp.getId());
                try {
                    String r = delGraphs.execute().body();
                    makeToast(r);
                    updateList();
                } catch (JsonIOException | IOException e) {
                    Log.e("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH", e.toString());
                }
            }
        });
        dialog.setNegativeButton("Нет", null);
        dialog.show();
    }

    public void onLoad_ListGraphs(View v){
        if(selected == -1)
            makeToast("Select graph");
        else {
            Graphs tmp = adapter.getItem(selected);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("graphId", tmp.getId());
            startActivityForResult(intent, 1);
        }
    }

    public void onEdit_ListGraphs(View v){
        Graphs tmp = adapter.getItem(selected);
        Intent intent = new Intent(this, CreateGraphActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("action", "edit");
        intent.putExtra("id", tmp.getId());
        intent.putExtra("name", tmp.toString());
        startActivityForResult(intent, 1);
    }
    public void onAdd_ListGraphs(View v){
        Intent intent = new Intent(this, CreateGraphActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("action", "new");
        startActivityForResult(intent, 1);
    }


    public void updateList() {
        adapter.clear();
        Call<List<Graphs>> getGraphs = service.get_graphs(token);
        try {
            List<Graphs> r = getGraphs.execute().body();
            graphs = r;
            adapter.addAll(r);
            adapter.notifyDataSetChanged();
        } catch (JsonIOException | IOException e) {
            Log.e("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH", e.toString());
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
        }
    }

    void closeSession() {
        Call<String> signOut = service.sign_out(token);
        try {
            String r = signOut.execute().body();
            Toast.makeText(this, r, Toast.LENGTH_SHORT).show();
            finish();
        } catch (JsonIOException | IOException e) {
            Log.e("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH", e.toString());
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
        }
    }

    public void makeToast(String str){
        Toast toast = Toast.makeText(this, str,Toast.LENGTH_LONG);
        toast.show();
    }
}
