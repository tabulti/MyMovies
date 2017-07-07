package com.paulo.joao.mymovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paulo.joao.mymovies.model.MyMovie;
import com.paulo.joao.mymovies.R;
import com.paulo.joao.mymovies.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joao Paulo Ribeiro on 09/02/2017.
 */

public class MoviesListPosterAdapter extends BaseAdapter {
    private Context context;
    private List<MyMovie> movies;

    public MoviesListPosterAdapter(Context context, List movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.movies_list_poster_item, null, false);

            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.movieTitle.setText(movies.get(position).getTitle());
//        viewHolder.movieYear.setText(Utils.formatYear(movies.get(position).getYear()));

        Picasso.with(context).load(Utils.formatImageUrlString(movies.get(position).getPoster_path())).into(viewHolder.moviePoster);

        return view;
    }

    public void setMovies(List<MyMovie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        private TextView movieTitle;
        private TextView movieYear;
        private ImageView moviePoster;

        public ViewHolder(View view) {
            movieTitle = (TextView) view.findViewById(R.id.movie_list_poster_item_title);
            movieYear = (TextView) view.findViewById(R.id.movie_list_poster_item_year);
            moviePoster = (ImageView) view.findViewById(R.id.movie_banner);
        }
    }
}
