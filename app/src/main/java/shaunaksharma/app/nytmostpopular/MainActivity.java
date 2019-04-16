package shaunaksharma.app.nytmostpopular;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String API_KEY = "NBaj9QFr3Whp0aqQGXkV4DWTLATSveOM";
    Disposable MAIN_CALL_DISPOSABLE = null;
    Disposable SORTER_DISPOSABLE = null;
    int CALLS = 0;// keeps track of calls made.

    ArrayList<String> ALL_IMAGES = new ArrayList<>();
    ArrayList<String> ALL_TITLES = new ArrayList<>();
    ArrayList<String> ALL_BYLINES = new ArrayList<>();
    ArrayList<String> ALL_DATES = new ArrayList<>();
    ArrayList<String> ALL_ABSTRACTS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up retrofit builder.
        Retrofit.Builder nytBuilder = new Retrofit.Builder()
                .baseUrl("https://api.nytimes.com/svc/mostpopular/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        //Set up retrofit using the previously made builder and the RequestHandler class.
        Retrofit retroArticle= nytBuilder.build();
        final RequestHandler mainRequestHandler = retroArticle.create(RequestHandler.class);

        //Send a request using the parameters("viewed" for most viewed, "7" for time period of a week, and the API key).
        // Later versions should allow users to change the filter and time period parameters.
        // Assign a disposable in order to dispose when activity is destroyed to avoid memory leaks.
        MAIN_CALL_DISPOSABLE = mainRequestHandler.getArticles("viewed", "7", API_KEY)
                .subscribeOn(Schedulers.io())//must be done on a thread other than the UI/main thread.
                .flatMap(new Function<ResponseContent, ObservableSource<List<ArticleContents>>>() {
                    @Override
                    public ObservableSource<List<ArticleContents>> apply(ResponseContent responseContent) throws Exception {
                        return Observable.just(responseContent.getResults());//return an Observable of *list* of articles.
                    }
                })
                .subscribe(allArticles->parseAllArticles(allArticles), e->Log.d("ONE", e.getMessage()));

    }

    private void parseArticle(ArticleContents article)
    {
        ALL_TITLES.add(article.getTitle());
        ALL_BYLINES.add(article.getByline());
        ALL_ABSTRACTS.add(article.get_abstract());
        ALL_DATES.add(article.getPublished_date());
        ALL_IMAGES.add(article.getMedia().get(0).getMedia_metadata().get(2).getUrl());
        CALLS++;
        if(CALLS == 20){setupRecyclerView();}//when 20 calls are made, recycler view is set up.
    }

    private void parseAllArticles(List<ArticleContents> allArticles)
    {
        SORTER_DISPOSABLE = Observable.fromIterable(allArticles)//Release each article from the list as its own Observable.
                .subscribeOn(AndroidSchedulers.mainThread())//switch to main thread since parseArticle will eventually call setupRecyclerView which alters the UI.
                .subscribe(article -> parseArticle(article), e -> Log.d("TWO", e.getMessage()));
    }

    private void setupRecyclerView()
    {
        CALLS = 0;//reset calls. Shall be used in later versions to allow users to refresh.
        Log.d("RECYLCER_VIEW", "START " + ALL_TITLES.size());
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerAdapter adapter = new RecyclerAdapter(ALL_TITLES, ALL_IMAGES, ALL_ABSTRACTS, ALL_BYLINES, ALL_DATES, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("RECYLCER_VIEW", "DONE");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(MAIN_CALL_DISPOSABLE!= null && !MAIN_CALL_DISPOSABLE.isDisposed())
        {
            MAIN_CALL_DISPOSABLE.dispose();
        }
        if(SORTER_DISPOSABLE!= null && !SORTER_DISPOSABLE.isDisposed())
        {
            SORTER_DISPOSABLE.dispose();
        }
    }


}
