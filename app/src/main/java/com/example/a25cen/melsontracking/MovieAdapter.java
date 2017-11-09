package com.example.a25cen.melsontracking;

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

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public MovieAdapter(List<MovieCard> movies) {
        this.movies = movies;
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        MovieCard movieCard = movies.get(position);
        holder.title.setText(movieCard.getTitle());
        holder.song.setText("Bees");
        holder.director.setText("Innocent Niyibizi");

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
