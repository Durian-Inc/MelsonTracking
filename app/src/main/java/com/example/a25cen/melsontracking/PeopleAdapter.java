package com.example.a25cen.melsontracking;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {

    private List<PersonCard> people;
    private Context context;
    private int lastPos = -1;

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
        final DatabaseHelper db = new DatabaseHelper(context);
        final PersonCard personCard = people.get(position);
        final String[] name = personCard.getName();
        final long PID = personCard.getPID();
        holder.personName.setText(name[0] + " " + name[1]);
        //TODO
        //Get the right awards and movies for each person
        holder.personAwardsWon.setText("Awards Won: " + db.getCount("Won", PID));
        holder.personAwardsNominated.setText("Awards Nominated: "+db.getCount("Nominated", PID));
        holder.personMovieStars.setText("Stars in: "+db.getCount("Stars", PID)+" Movies");
        holder.personMovieDirects.setText("Directs: "+db.getCount("Directs", PID)+" Movies");
        holder.personMovieWrites.setText("Wrote: "+ db.getCount("Writes", PID)+" Movies");
        setAnimation(holder.itemView, position);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> awardsWon = db.getNamesOfAwards("Won", PID);
                ArrayList<String> awardsNominated = db.getNamesOfAwards("Nominated", PID);
                Toast.makeText(context, name[0]+" "+name[1], Toast.LENGTH_SHORT).show();
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_person_expanded);
                dialog.setTitle(name[0]+" "+name[1]);
                TextView personName = dialog.findViewById(R.id.personExpandedName);
                TextView personGender = dialog.findViewById(R.id.personExpandedGender);
                personGender.setText("Gender: "+db.getGender(PID));
                personName.setText(name[0]+" "+name[1]);
                final Button closing = dialog.findViewById(R.id.personExpandedButton);
                closing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                LinearLayout wonLinear = dialog.findViewById(R.id.personExpandedWonLinearLayout);
                LinearLayout nominatedLinear = dialog.findViewById(R.id.personExpandedNominatedLinearLayout);
                for(String award: awardsWon){
                    TextView newAward = new TextView(context);
                    newAward.setText(award);
                    newAward.setTextSize(20);
                    newAward.setTextColor(context.getResources().getColor(R.color.textViewColor));
                    wonLinear.addView(newAward, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                for(String award: awardsNominated){
                    TextView newAward = new TextView(context);
                    newAward.setText(award);
                    newAward.setTextSize(20);
                    newAward.setTextColor(context.getResources().getColor(R.color.textViewColor));
                    nominatedLinear.addView(newAward, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                //Setting the size of the dialog window to be a decent size
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                Window dialogWindow = dialog.getWindow();
                layoutParams.copyFrom(dialogWindow.getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialogWindow.setAttributes(layoutParams);
                dialog.show();

            }
        });
        db.close();
        //TODO Make animation slower
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public static class PeopleViewHolder extends RecyclerView.ViewHolder{
        TextView personName, personMovieStars, personAwardsNominated, personMovieDirects, personMovieWrites,
        personAwardsWon;
        CardView cardView;
        public PeopleViewHolder(View view) {
            super(view);
            personAwardsNominated = view.findViewById(R.id.textPersonAwardsNominated);
            personMovieStars = view.findViewById(R.id.textPersonCardStars);
            personName = view.findViewById(R.id.textPersonName);
            personMovieDirects = view.findViewById(R.id.textPersonCardDirects);
            personMovieWrites = view.findViewById(R.id.textPersonCardWrites);
            personAwardsWon = view.findViewById(R.id.textPersonAwardsWon);
            cardView = view.findViewById(R.id.cardView);
        }
    }

    private void setAnimation(View viewToAnimate, int pos){
        if(pos > lastPos){
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(),
                    android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPos = pos;
        }
    }
}
