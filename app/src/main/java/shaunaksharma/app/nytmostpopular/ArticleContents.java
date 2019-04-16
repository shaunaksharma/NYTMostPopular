package shaunaksharma.app.nytmostpopular;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleContents
{

    private String url;
    private String byline;
    private String title;

    @SerializedName("abstract")
    private String _abstract;
    private String published_date;
    private List<MediaContent> media;


    public String getUrl() {
        return url;
    }

    public String getByline() {
        return byline;
    }

    public String getTitle() {
        return title;
    }

    public String get_abstract() {
        return _abstract;
    }

    public String getPublished_date() {
        return published_date;
    }

    public List<MediaContent> getMedia() {return media;}
}
