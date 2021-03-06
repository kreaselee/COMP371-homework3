package com.example.homework3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder> {

    private List<Character> characters;

    // pass this list into the constructor of the adapter
    public CharacterAdapter(List<Character> characters) {
        this.characters = characters;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View characterView = inflater.inflate(R.layout.item_character, parent, false);
        CharacterAdapter.ViewHolder viewHolder = new CharacterAdapter.ViewHolder(characterView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // populate data into the item through holder
        Character character = characters.get(position);

        // set the view based on the data and the view names
        holder.textView_name.setText(character.getName());
        Picasso.get().load(character.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_name;
        ImageView imageView;

        // create constructor
        public ViewHolder(View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.textView_ep_character);
            imageView = itemView.findViewById(R.id.imageView_ep_char);
        }
    }
}
