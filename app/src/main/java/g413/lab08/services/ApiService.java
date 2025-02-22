package g413.lab08.services;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;
public interface ApiService {
    @PUT("account/register")
    Call<String> register(@Query("username") String username, @Query("password") String password);

    @PUT("/account/sign-in")
    Call<String> sign_in(@Query("username") String username, @Query("password") String password);



}
