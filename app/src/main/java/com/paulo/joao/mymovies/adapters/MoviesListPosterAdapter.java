package com.paulo.joao.mymovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.paulo.joao.mymovies.model.MyMovie;
import com.paulo.joao.mymovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joao Paulo Ribeiro on 09/02/2017.
 */

public class MoviesListPosterAdapter extends BaseAdapter implements Filterable{
    private Context context;
    private MovieFilter movieFilter;
    private List<MyMovie> movies;
    private List<MyMovie> moviesFiltered;

    private String url = "https://images-na.ssl-images-amazon.com/images/M/MV5BZGEzZTExMDEtNjg4OC00NjQxLTk5NTUtNjRkNjA3MmYwZjg1XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg";

    public MoviesListPosterAdapter(Context context, List movies) {
        this.context = context;
        this.movies = movies;
        this.moviesFiltered = movies;
    }

    @Override
    public int getCount() {
        return moviesFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        return moviesFiltered.get(position);
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
        viewHolder.movieYear.setText(movies.get(position).getYear());

        Picasso.with(context).load(movies.get(position).getPosterUrl()).into(viewHolder.moviePoster);

        return view;
    }

    @Override
    public Filter getFilter() {
        if (movieFilter == null) {
            movieFilter = new MovieFilter();
        }
        return movieFilter;
    }


    private class MovieFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            if (charSequence != null && charSequence.length() > 0) {
                ArrayList<MyMovie> moviesList = new ArrayList<>();

                for (MyMovie movie : movies) {
                    if (movie.getTitle().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        moviesFiltered.add(movie);
                    }
                }

                filterResults.count = moviesFiltered.size();
                filterResults.values = moviesFiltered;
            } else {
                filterResults.count = movies.size();
                filterResults.values = movies;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            moviesFiltered = (ArrayList<MyMovie>) filterResults.values;
            notifyDataSetChanged();
        }
    }



    private static class ViewHolder {
        private TextView movieTitle;
        private TextView movieYear;
        private ImageView moviePoster;

        public ViewHolder(View view) {
            movieTitle = (TextView) view.findViewById(R.id.movie_list_poster_item_title);
            movieYear = (TextView) view.findViewById(R.id.movie_list_poster_item_year);
            moviePoster = (ImageView) view.findViewById(R.id.movie_list_poster_item_image);
        }
    }
}
