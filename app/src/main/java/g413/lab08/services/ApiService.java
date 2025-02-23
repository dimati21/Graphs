package g413.lab08.services;
import java.util.List;

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
}
