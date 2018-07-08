package com.example.lucia.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private List<Trailer> trailersList;
    private Context mContext;

    /**
     * TrailerAdapter constructor that takes the trailerList to display within context
     *
     * @param context      the context of the trailersList
     * @param trailersList the list of trailers that is displayed
     */
    public TrailerAdapter(Context context, List<Trailer> trailersList) {
        this.mContext = context;
        this.trailersList = trailersList;
    }

    /**
     * This method adds new objects to the listVew
     */
    public void addAll(List<Trailer> trailersList) {
        this.trailersList.clear();
        this.trailersList.addAll(trailersList);
        notifyDataSetChanged();
    }

    /**
     * This method clears the news list
     */
    public void clearAll() {
        this.trailersList.clear();
        notifyDataSetChanged();
    }

    /**
     * This method creates Views each time the RecyclerView needs it
     *
     * @param parent   the ViewGroup of the ViewHolder
     * @param viewType
     * @return a new TrailerViewHolder that has the View for each item
     */
    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(itemView);

    }

    /**
     * This method is called to display information on a specific position and to register
     * a clickListener that opens Youtube to play the trailer
     *
     * @param holder   this TrailerViewHolder is updated
     * @param position the position of the item within the adapter
     */
    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {

        //get the position
        final Trailer trailer = trailersList.get(position);

        //Button with OnClickListener that sends an intent to open Youtube for each trailer
        FloatingActionButton video = holder.videoPlayButton;
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getTrailerSite()));
                mContext.startActivities(new Intent[]{youtubeIntent});
            }
        });

        //get the holder that is updated for each trailer details
        TextView trailerName = holder.trailerName;
        trailerName.setText(trailer.getTrailerName());

    }

    /**
     * this method counts the number of items in the list displayed
     *
     * @return the number of items available
     */
    @Override
    public int getItemCount() {
        return trailersList.size();
    }

    /**
     * This innerclass helps hold the views
     */
    class TrailerViewHolder extends RecyclerView.ViewHolder {
        TextView trailerName;
        FloatingActionButton videoPlayButton;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            trailerName = itemView.findViewById(R.id.trailer_name);
            videoPlayButton = itemView.findViewById(R.id.video_play_button);
        }
    }
}

