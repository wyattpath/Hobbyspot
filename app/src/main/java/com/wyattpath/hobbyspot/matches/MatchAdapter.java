package com.wyattpath.hobbyspot.matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.wyattpath.hobbyspot.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Take care of populating each and every single item matches
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchViewHolder> {
    private List<Match> matchList;
    private Context context;

    public MatchAdapter(List<Match> matchList, Context context) {
        this.matchList = matchList;
        this.context = context;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        MatchViewHolder rcv = new MatchViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        holder.mMatchId.setText(matchList.get(position).getUserId());
        holder.mMatchName.setText(matchList.get(position).getName());
        if(!matchList.get(position).getProfileImageUrl().equals("default")) {
            Glide.with(context).load(matchList.get(position).getProfileImageUrl()).into(holder.mMatchImage);
        }
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }
}
