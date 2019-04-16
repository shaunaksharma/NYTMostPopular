package shaunaksharma.app.nytmostpopular;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private ArrayList<String> mAllTitles;
    private ArrayList<String> mAllImages;
    private ArrayList<String> mAllAbstracts;
    private ArrayList<String> mAllBylines;
    private ArrayList<String> mAllDates;
    private Context mContext;

    public RecyclerAdapter(ArrayList<String> allTitles, ArrayList<String> allImages, ArrayList<String> allAbstracts, ArrayList<String> allBylines, ArrayList<String> allDates, Context context) {
        this.mAllTitles = allTitles;
        this.mAllImages = allImages;
        this.mAllBylines = allBylines;
        this.mAllAbstracts = allAbstracts;
        this.mAllDates = allDates;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(mContext)
                .asBitmap()
                .load(mAllImages.get(i))
                .centerCrop()
                .into(viewHolder.articleImage);

        viewHolder.articleTitle.setText(mAllTitles.get(i));

        viewHolder.articleByline.setText(mAllBylines.get(i));

        viewHolder.articleAbstract.setText(mAllAbstracts.get(i));

        viewHolder.articleDate.setText(mAllDates.get(i));

        viewHolder.listItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Ayo " + i, Toast.LENGTH_SHORT).show();
                //Later versions must open the browser with link to the whole article.
            }
        });

        if(i%2 == 0) { viewHolder.listItemLayout.setBackgroundColor(Color.parseColor("#a8f4ec")); }
    }

    @Override
    public int getItemCount() {
        return mAllTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView articleImage;
        TextView articleTitle;
        TextView articleByline;
        TextView articleAbstract;
        TextView articleDate;
        ConstraintLayout listItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImage = itemView.findViewById(R.id.list_image);
            articleTitle = itemView.findViewById(R.id.list_title);
            articleAbstract = itemView.findViewById(R.id.list_abstract);
            articleByline = itemView.findViewById(R.id.list_byline);
            articleDate = itemView.findViewById(R.id.list_date);
            listItemLayout = itemView.findViewById(R.id.list_single_layout);
        }
    }
}
