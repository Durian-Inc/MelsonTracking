package com.example.a25cen.melsontracking;

import android.content.Context;
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

    //Constructor
    public MovieAdapter(Context context, List<MovieCard> movies) {
        this.movies = movies; this.context = context;
    }


    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        //Giving all the views of the card their appropriate values
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
        //Defining the actions that are performed when the delete button is pressed.
        //When pressed the movie will be removed from the database as well as the recycler view. The card will slide to the right as well
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
        db.close();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflating the view with the movie card dialog and setting the viewholder to be an instance of the MovieViewHolder class
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .card_movie, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);

        return movieViewHolder;
    }

    //Creating the class for the MovieViewHolder
    //This class will define all of the views in the card
    public static class MovieViewHolder extends RecyclerView.ViewHolder{
        TextView title, song, director, stars;
        Button delteButton;
        //Constructor for the view holder
        public MovieViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.movieTitle);
            song = view.findViewById(R.id.textSong);
            director = view.findViewById(R.id.textDirectors);
            stars = view.findViewById(R.id.textStars);
            delteButton = view.findViewById(R.id.deleteMovie);

        }
    }

    /*
        setAnimation
        Function that sets the animation for the movie cards
        Pre: viewToAnimate --> The card that will be animated
             pos --> The position of the view
        Post: None
     */
    private void setAnimation(View viewToAnimate, int pos){
        //Checking to see if the position is greater than the lastPos
        //If it is greater than -1 then the card will slide in from the left
        //lastPos will be set to the current position to keep track of which card has been animated
        if(pos > lastPos){
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(),
                    android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPos = pos;
        }
    }
}
