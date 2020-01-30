package com.wyattpath.hobbyspot.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wyattpath.hobbyspot.R;

import java.util.List;

import androidx.annotation.NonNull;

public class CardArrayAdapter extends ArrayAdapter<Card> {
    Context context;
    private String[] randomPics = {
            "https://i.pinimg.com/originals/08/61/b7/0861b76ad6e3b156c2b9d61feb6af864.jpg",
            "https://i.imgur.com/dOx2wRl.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSpV2udHUjjVHp610e6nrtsLU-NK9XGd89lk76Ml3EeV5Bz0lAJQA&s",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQbz5gmggoERzWj4QCNAUhUhm71eyQhgquZaGerfWgaOdE4Bh9fdw&s",
            "https://media.wired.com/photos/5cdefc28b2569892c06b2ae4/master/w_2560%2Cc_limit/Culture-Grumpy-Cat-487386121-2.jpg",
            "https://i.kym-cdn.com/entries/icons/original/000/014/285/sideeyechloe.jpg",
            "https://img.bleacherreport.net/img/images/photos/002/625/710/o-SEA-OTTER-BASKETBALL-facebook_crop_exact.jpg?w=1200&h=1200&q=75",
            "https://www.sportvideos.tv/wp-content/uploads/2017/11/uDQ-xAzcWo.jpg",
            "https://i.pinimg.com/236x/0a/a4/a7/0aa4a7d89f0c081b344f959e65958562--golf-humor-sport-humor.jpg",
            "https://i.ytimg.com/vi/9pTiPlcSp_Q/hqdefault.jpg",
            "https://www.punchline-gloucester.com/images/user/11041_bulldog.jpg",
            "https://i.pinimg.com/236x/bb/1c/c2/bb1cc2c07bff46d042fc5639da357246--sports-basketball-teddy-bears.jpg",
            "https://acegif.com/wp-content/uploads/funny-faces-42-gap.jpg"
    };

    public CardArrayAdapter(@NonNull Context context, int resourceId, List<Card> items) {
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Card card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        name.setText(card_item.getName());
        switch (card_item.getProfileImageUrl()) {
            case "default":
                Glide.with(convertView.getContext()).load(randomPics[(int) Math.floor(Math.random() * randomPics.length)]).placeholder(R.mipmap.ic_launcher).into(image);
                break;
            default:
                Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;
        }

        return convertView;
    }
}
