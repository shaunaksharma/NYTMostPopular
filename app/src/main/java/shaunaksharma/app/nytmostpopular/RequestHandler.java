package shaunaksharma.app.nytmostpopular;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestHandler
{
    @GET("{medium}/{period}.json")
    Observable<ResponseContent> getArticles(@Path("medium") String medium, @Path("period") String period, @Query("api-key") String key);

    @GET("{medium}/{period}.json")
    Call<ResponseContent> getArticlesCall(@Path("medium") String medium, @Path("period") String period, @Query("api-key") String key);
}
