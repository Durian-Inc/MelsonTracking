package com.example.a25cen.melsontracking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

/**
 * Created by Innocent on 11/9/17.
 */

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {

    private List<PersonCard> people;
    private Context context;

    public PeopleAdapter(Context context, List<PersonCard> people) {
        this.people = people;
        this.context = context;
    }

    @Override
    public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_person, parent, false);
        PeopleViewHolder peopleViewHolder = new PeopleViewHolder(view);
        return peopleViewHolder;
    }

    @Override
    public void onBindViewHolder(PeopleViewHolder holder, int position) {
        DatabaseHelper db = new DatabaseHelper(context);
        PersonCard personCard = people.get(position);
        String[] name = personCard.getName();
        long PID = personCard.getPID();
        holder.personName.setText(name[0] + " " + name[1]);
        //TODO
        //Get the right awards and movies for each person
        holder.personAwardsWon.setText("Awards Won: " + db.getCount("Won", PID));
        holder.personAwardsNominated.setText("Awards Nominated: "+db.getCount("Nominated", PID));
        holder.personMovieStars.setText("Stars in: "+db.getCount("Stars", PID)+" Movies");
        holder.personMovieDirects.setText("Directs: "+db.getCount("Directs", PID)+" Movies");
        holder.personMovieWrites.setText("Wrote: "+ db.getCount("Writes", PID)+" Movies");

    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public static class PeopleViewHolder extends RecyclerView.ViewHolder{
        TextView personName, personMovieStars, personAwardsNominated, personMovieDirects, personMovieWrites,
        personAwardsWon;
        public PeopleViewHolder(View view) {
            super(view);
            personAwardsNominated = view.findViewById(R.id.textPersonAwardsNominated);
            personMovieStars = view.findViewById(R.id.textPersonCardStars);
            personName = view.findViewById(R.id.textPersonName);
            personMovieDirects = view.findViewById(R.id.textPersonCardDirects);
            personMovieWrites = view.findViewById(R.id.textPersonCardWrites);
            personAwardsWon = view.findViewById(R.id.textPersonAwardsWon);
        }
    }
}
