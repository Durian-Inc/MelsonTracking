package com.example.a25cen.melsontracking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<MovieCard> movies;
    private Context context;
    private int lastPos = -1;

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public MovieAdapter(Context context, List<MovieCard> movies) {
        this.movies = movies; this.context = context;
    }


    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        final DatabaseHelper db = new DatabaseHelper(context);
        final MovieCard movieCard = movies.get(position);
        final long MID = movieCard.getMID();
        holder.title.setText(movieCard.getTitle()+" ("+movieCard.getReleaseYear()+")");
        try{
            holder.song.setText("Song: "+db.getMovieSong(MID));
        }
        catch (SQLiteException ex) {
            holder.song.setText("");
        }
        holder.director.setText("Director: "+db.getAllPeopleInvolved("Director", MID));
        holder.stars.setText("Stars: "+ db.getAllPeopleInvolved("Stars", MID));
        holder.delteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation delteAnimation = AnimationUtils.loadAnimation(view.getContext(),
                        android.R.anim.slide_out_right);
                Toast.makeText(context, "Deleting "+movieCard.getTitle(), Toast.LENGTH_SHORT).show();
                db.deleteMovie(MID);
                db.close();
                movies.remove(position);
                view.startAnimation(delteAnimation);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, movies.size());
            }
        });
        setAnimation(holder.itemView, position);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .card_movie, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);

        return movieViewHolder;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder{
        TextView title, song, director, stars;
        Button delteButton;
        public MovieViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.movieTitle);
            song = view.findViewById(R.id.textSong);
            director = view.findViewById(R.id.textDirectors);
            stars = view.findViewById(R.id.textStars);
            delteButton = view.findViewById(R.id.deleteMovie);

        }
    }

    private void  setAnimation(View viewToAnimate, int pos){
        if(pos > lastPos){
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(),
                    android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPos = pos;
        }
    }
}
