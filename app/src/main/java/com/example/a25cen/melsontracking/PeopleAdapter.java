package com.example.a25cen.melsontracking;

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

    public PeopleAdapter(List<PersonCard> people) {
        this.people = people;
    }

    @Override
    public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_person, parent, false);
        PeopleViewHolder peopleViewHolder = new PeopleViewHolder(view);
        return peopleViewHolder;
    }

    @Override
    public void onBindViewHolder(PeopleViewHolder holder, int position) {
        PersonCard personCard = people.get(position);
        String[] name = personCard.getName();
        holder.personName.setText(name[0] + " " + name[1]);
        //TODO
        //Get the right awards and movies for each person
        holder.personAwards.setText("Awards: #");
        holder.personMovieStars.setText("Stars in: # Movies");
        holder.personMovieDirects.setText("Directs: # Movies");
        holder.personMovieWrites.setText("Wrote: # Movies");

    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public static class PeopleViewHolder extends RecyclerView.ViewHolder{
        TextView personName, personMovieStars, personAwards, personMovieDirects, personMovieWrites;
        public PeopleViewHolder(View view) {
            super(view);
            personAwards = view.findViewById(R.id.textPersonAwards);
            personMovieStars = view.findViewById(R.id.textPersonCardStars);
            personName = view.findViewById(R.id.textPersonName);
            personMovieDirects = view.findViewById(R.id.textPersonCardDirects);
            personMovieWrites = view.findViewById(R.id.textPersonCardWrites);
        }
    }
}
