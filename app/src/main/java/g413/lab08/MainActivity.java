package g413.lab08;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
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
import java.util.ArrayList;
import java.util.List;

import g413.lab08.sampledata.Link;
import g413.lab08.sampledata.Node;
import g413.lab08.services.ApiService;
import g413.lab08.services.Graphs;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    ApiService service;

    String token, graphId;
    List<Node> nodes;
    List<Link> links;
    GraphView gv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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
                .baseUrl("https://84aadc26-3088-43c1-b2b0-4a5cebf88fcb-00-3oxzq4o504g05.pike.replit.dev/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(ApiService.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            token = extras.getString("token");
            graphId = extras.getString("graphId");
        }

        gv = findViewById(R.id.graphView);

        gv.add_nodes_list(get_nodes_from_server());
        gv.add_links_list(get_links_from_server());
        gv.maxid = getMaxId() + 1;
    }

    public void onAddNode_Main(View v)
    {
        gv.add_node();
    }
    public void onAddLink_Main(View v)
    {
        gv.link_selected_nodes();
    }
    public void onDeleteNode_Main(View v)
    {
        gv.remove_selected_node();
    }
    public void onDeleteLink_Main(View v)
    {
        gv.remove_selected_link();
    }

    List<Node> tempOldNodes = new ArrayList<>();
    List<Node> tempNewNodes = new ArrayList<>();
    List<Link> tempOldLinks = new ArrayList<>();
    List<Link> tempNewLinks = new ArrayList<>();
    public void onSave_Main(View v)
    {

        if (nodes == null && gv.g.node == null){
            return;
        }
        try
        {
            for (Node old : nodes) {
                tempOldNodes.add(old);
            }
        }catch (Exception e){
            Log.e("dddddddddddddd", e.toString());
        }
        try
        {
            for (Node nw : gv.g.node){
                tempNewNodes.add(nw);
            }
        }catch (Exception e){
            Log.e("dddddddddddddd", e.toString());
        }
        for (Node old : nodes){
            for (Node nw : gv.g.node){
                if (old.getId() == nw.getId()){
                    tempOldNodes.remove(old);
                    tempNewNodes.remove(nw);
                    updNode(nw);
                }
            }
        }
        try {
            if (!tempOldNodes.isEmpty() && !(tempOldNodes == null)){
                for (Node n : tempOldNodes){
                    delNode(n);
                }
            }
        }catch (Exception e){
            Log.e("dddddddddddddd", e.toString());
        }

        try {
            if (!tempNewNodes.isEmpty() && !(tempNewNodes == null)){
                for (Node n : tempNewNodes){
                    addNode(n);
                }
            }
        }catch (Exception e){
            Log.e("dddddddddddddd", e.toString());
        }

        try {
            tempOldLinks.addAll(links);
        }catch (Exception e) { Log.e("dddddddddddddddddddd", e.toString()); }
        try {
            tempNewLinks.addAll(gv.g.link);
        } catch (Exception e) { Log.e("dddddddddddddddddddd", e.toString()); }
        for (Link l : links){
            for (Link nw : gv.g.link){
                if (l.getId() == nw.getId()){
                    tempOldLinks.remove(l);
                    tempNewLinks.remove(nw);
                }
            }
        }
        try {
            if (!tempOldLinks.isEmpty()){
                for (Link n : tempOldLinks){
                    delLink(n);
                }
            }

        } catch (Exception e) { Log.e("dddddddddddddddddddd", e.toString()); }
        try {
            if (!tempNewLinks.isEmpty()){
                for (Link n : tempNewLinks){
                    addLink(n);
                }
            }
        } catch (Exception e) { Log.e("dddddddddddddddddddd", e.toString()); }
        finish();
    }

    public void onBack_Main(View v)
    {
        finish();
    }

    public List<Node> get_nodes_from_server()
    {
        Call<List<Node>> getNodes = service.get_nodes(token, graphId);
        try {
            List<Node> r = getNodes.execute().body();
            nodes = r;
            return r;
        } catch (JsonIOException | IOException e) {
            Log.e("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH", e.toString());
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public List<Link> get_links_from_server()
    {
        Call<List<Link>> getLinks = service.get_links(token, graphId);
        try {
            List<Link> r = getLinks.execute().body();
            links = r;
            return r;
        } catch (JsonIOException | IOException e) {
            Log.e("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH", e.toString());
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    public void updNode(Node n)
    {
        Call<String> updNode = service.update_node(token, graphId, n.id, n.x, n.y, n.name);
        try {
            String r = updNode.execute().body();
            Toast.makeText(this, r, Toast.LENGTH_SHORT).show();
        } catch (JsonIOException | IOException e) {
            Log.e("ddddddd", "dddddddddddd");
        }
    }

    public void delNode(Node n)
    {
        Call<String> delNode = service.delete_node(token, n.getId());
        try {
            String r = delNode.execute().body();
            Toast.makeText(this, r, Toast.LENGTH_SHORT).show();
        } catch (JsonIOException | IOException e) {
            Log.e("ddddddd", "dddddddddddd");
        }
    }

    public void addNode(Node n)
    {
        Call<String> addNode = service.add_node(token, graphId, n.x, n.y, n.name);
        try {
            String r = addNode.execute().body();
            Toast.makeText(this, r, Toast.LENGTH_SHORT).show();
        } catch (JsonIOException | IOException e) {
            Log.e("ddddddd", "dddddddddddd");
        }
    }



    public void delLink(Link l)
    {
        Call<String> delLink = service.delete_link(token, l.getId());
        try {
            String r = delLink.execute().body();
            Toast.makeText(this, r, Toast.LENGTH_SHORT).show();
        } catch (JsonIOException | IOException e) {
            Log.e("ddddddd", "dddddddddddd");
        }
    }
    public void addLink(Link l)
    {
        Call<String> addLink = service.add_link(token, graphId, l.source, l.target, l.value);
        try {
            String r = addLink.execute().body();
            Toast.makeText(this, r, Toast.LENGTH_SHORT).show();
        } catch (JsonIOException | IOException e) {
            Log.e("ddddddd", "dddddddddddd");
        }
    }

    public int getMaxId(){
        Call<Integer> maxid = service.max_nodeid();
        try {
            return maxid.execute().body();
        } catch (JsonIOException | IOException e) {
            Log.e("ddddddd", "dddddddddddd");
        }
        return -1;
    }


}