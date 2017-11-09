package com.example.a25cen.melsontracking;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.AttributeSet;
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
        holder.personName.setText("Innocent Niyibizi");
        holder.personAwards.setText("All of them");
        holder.personMovies.setText("Bee Movie & Shrek");

    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public static class PeopleViewHolder extends RecyclerView.ViewHolder{
        TextView personName, personMovies, personAwards;
        public PeopleViewHolder(View view) {
            super(view);
            personAwards = view.findViewById(R.id.textAwards);
            personMovies = view.findViewById(R.id.textRecentMovie);
            personName = view.findViewById(R.id.textName);

        }
    }
}
