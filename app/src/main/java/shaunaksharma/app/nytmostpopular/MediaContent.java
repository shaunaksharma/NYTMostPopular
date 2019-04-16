package shaunaksharma.app.nytmostpopular;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MediaContent
{
    public String getCaption() {
        return caption;
    }

    public String getCopyright() {
        return copyright;
    }

    public List<MediaThumbnails> getMedia_metadata() {
        return mediametadata;
    }

    private String caption;
    private String copyright;

    @SerializedName("media-metadata")
    private List<MediaThumbnails> mediametadata;
}
