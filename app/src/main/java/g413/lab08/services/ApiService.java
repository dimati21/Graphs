package g413.lab08.services;
import java.util.List;

import g413.lab08.sampledata.Link;
import g413.lab08.sampledata.Node;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
public interface ApiService {
    @PUT("account/register")
    Call<String> register(@Query("username") String username,
                          @Query("password") String password);

    @PUT("/account/sign-in")
    Call<String> sign_in(@Query("username") String username,
                         @Query("password") String password);

    @DELETE("/account/sign-out")
    Call<String> sign_out(@Query("token") String token);

    @GET("/graphs/graph-list")
    Call<List<Graphs>> get_graphs(@Query("token") String token);

    @DELETE("/graphs/delete")
    Call<String> delete_graph(@Query("token") String token,
                              @Query("id") String id);

    @PUT("/graphs/add")
    Call<String> add_graph(@Query("token") String token,
                           @Query("name") String name);

    @POST("/graphs/update")
    Call<String> update_graph(@Query("token") String token,
                              @Query("name") String name,
                              @Query("id") String id);

    @GET("/nodes/node-list")
    Call<List<Node>> get_nodes(@Query("token") String token,
                               @Query("id") String id);

    @GET("/links/link-list")
    Call<List<Link>> get_links(@Query("token") String token,
                               @Query("id") String id);

    @POST("/nodes/update")
    Call<String> update_node(@Query("token") String token,
                            @Query("graphid") String graphid,
                            @Query("nodeid") int nodeid,
                            @Query("x") float x,
                            @Query("y") float y,
                            @Query("name") String name);

    @DELETE("/nodes/delete")
    Call<String> delete_node(@Query("token") String token,
                            @Query("id") int nodeid);

    @PUT("/nodes/add")
    Call<String> add_node(@Query("token") String token,
                         @Query("id") String graphid,
                         @Query("x") float x,
                         @Query("y") float y,
                         @Query("name") String name);

    @DELETE("/links/delete")
    Call<String> delete_link(@Query("token") String token,
                             @Query("id") int nodeid);

    @PUT("/links/add")
    Call<String> add_link(@Query("token") String token,
                          @Query("id") String graphid,
                          @Query("source") int source,
                          @Query("target") int target,
                          @Query("value") int value);

    @GET("/maxID")
    Call<Integer> max_nodeid();
}
