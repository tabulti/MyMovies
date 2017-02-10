package com.paulo.joao.mymovies.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paulo.joao.mymovies.model.MyMovie;
import com.paulo.joao.mymovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joao Paulo Ribeiro on 07/02/2017.
 */

public class CoverFlowAdapter extends BaseAdapter {

    private List<MyMovie> data;
    private Context context;

    public CoverFlowAdapter(List<MyMovie> movies, Context context) {
        this.data = movies;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_flow_view, null, false);

            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //TODO: verify cases where image will not able
        Picasso.with(this.context).load(data.get(position).getPosterUrl()).into(viewHolder.movieImage);

//        viewHolder.movieImage.setImageResource(data.get(position).getPosterUrl());

//        viewHolder.movieName.setText(data.get(position).getTitle());

        view.setOnClickListener(onMovieClickListener(position));

        return view;
    }

    private View.OnClickListener onMovieClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);

                dialog.setContentView(R.layout.dialog_movie_info);
                dialog.setCancelable(true);
                dialog.setTitle(R.string.selected_movie_dialog_title);

                TextView textView = (TextView) dialog.findViewById(R.id.name);
//                textView.setText(data.get(position).getTitle());

                ImageView imageView = (ImageView) dialog.findViewById(R.id.image_diolog);

                //TODO: verify cases where image will not able
//                Picasso.with(context).load(data.get(position).getPosterUrl()).into(imageView);
//                imageView.setImageResource(data.get(position).getPosterUrl());

                dialog.show();
            }
        };
    }


    private static class ViewHolder {

        private TextView movieName;
        private ImageView movieImage;

        public ViewHolder(View view) {
            movieName = (TextView) view.findViewById(R.id.name);
            movieImage = (ImageView) view.findViewById(R.id.image);
        }
    }
}
