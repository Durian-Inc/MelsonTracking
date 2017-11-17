package com.example.a25cen.melsontracking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Innocent on 11/7/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<MovieCard> movies;
    private Context context;

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public MovieAdapter(Context context, List<MovieCard> movies) {
        this.movies = movies; this.context = context;
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        DatabaseHelper db = new DatabaseHelper(context);
        MovieCard movieCard = movies.get(position);
        holder.title.setText(movieCard.getTitle());
        try{
            holder.song.setText(db.getMovieSong(position+1));
        }
        catch (Exception ex) {
            holder.song.setText("");
        }
        holder.director.setText(db.getAllPeopleInvolved("Director", position+1));

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .card_movie, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);

        return movieViewHolder;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder{
        TextView title, song, director;
        public MovieViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.movieTitle);
            song = view.findViewById(R.id.textSong);
            director = view.findViewById(R.id.textDirectors);

        }
    }
}
