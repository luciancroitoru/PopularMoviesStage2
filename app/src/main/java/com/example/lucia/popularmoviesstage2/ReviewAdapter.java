package com.example.lucia.popularmoviesstage2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviewsList;
    private Context mContext;

    /**
     * ReviewAdapter constructor that takes the reviewList to display within context
     *
     * @param context     the context where reviewsList is displayed
     * @param reviewsList the list of reviews that are displayed
     */
    public ReviewAdapter(Context context, List<Review> reviewsList) {
        this.mContext = context;
        this.reviewsList = reviewsList;
    }

    /**
     * This method creates Views each time the RecyclerView needs it
     *
     * @param parent   the ViewGroup of the ViewHolder
     * @param viewType
     * @return a new ReviewViewHolder that has the View for each item
     */
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(itemView);
    }

    /**
     * This method is called to display information on a specific position and to register
     * a clickListener that opens a full review text for each review longer then 5 lines
     *
     * @param holder   this ReviewViewHolder is updated
     * @param position the position of the item within the adapter
     */
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        //get the position
        Review reviewItem = reviewsList.get(position);

        //get the holder that is updated for each review details
        TextView author = holder.authorOfReview;
        author.setText(reviewItem.getAuthor());
        final TextView content = holder.contentReview;
        content.setText(reviewItem.getContent());
        //OnClickListener to display only 5 lines of text, and when it is clicked to show
        // full text of the review
        final int maxLines = 5;
        content.setMaxLines(maxLines);
        content.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (content.getMaxLines() == maxLines) {
                    content.setMaxLines(content.length());
                } else {
                    content.setMaxLines(maxLines);
                }
            }
        });
    }

    /**
     * this method counts the number of items in the list
     *
     * @return the number of items available
     */
    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    /**
     * This method adds new objects to the listVew
     */
    public void addAll(List<Review> reviewsList) {
        this.reviewsList.clear();
        this.reviewsList.addAll(reviewsList);
        notifyDataSetChanged();
    }

    /**
     * This method clear the news list
     */
    public void clearAll() {
        this.reviewsList.clear();
        notifyDataSetChanged();
    }

    /**
     * This innerclass holds the views
     */
    class ReviewViewHolder extends RecyclerView.ViewHolder {
        //Variables that help set the information needed in the RecyclerView that display
        // the review list in the activity_detail.xml layout.
        TextView authorOfReview;
        TextView contentReview;

        //ViewHolder's constructor
        public ReviewViewHolder(View itemView) {
            super(itemView);

            //Find Views that display each item
            authorOfReview = itemView.findViewById(R.id.review_author_tv);
            contentReview = itemView.findViewById(R.id.review_content_tv);
        }
    }
}
