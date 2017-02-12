package com.paulo.joao.mymovies.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.paulo.joao.mymovies.model.MyMovie;
import com.paulo.joao.mymovies.R;

import java.util.List;

/**
 * Created by Joao Paulo Ribeiro on 07/02/2017.
 */

public class MoviesListAdapter extends BaseAdapter {

    private Context context;
    private List<MyMovie> movies;

    public MoviesListAdapter(Context context, List movies) {
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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.movies_list_item, null, false);

            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.movieTitle.setText(movies.get(position).getTitle());
        viewHolder.movieYear.setText(movies.get(position).getYear());

        return view;
    }

    private static class ViewHolder {
        private TextView movieTitle;
        private TextView movieYear;

        public ViewHolder(View view) {
            movieTitle = (TextView) view.findViewById(R.id.movie_list_item_title);
            movieYear = (TextView) view.findViewById(R.id.movie_list_item_year);
        }
    }
}
