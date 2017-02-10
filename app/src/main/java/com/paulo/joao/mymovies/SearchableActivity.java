package com.paulo.joao.mymovies;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.paulo.joao.mymovies.model.MyMovie;

/**
 * Created by Joao Paulo Ribeiro on 09/02/2017.
 */

public class SearchableActivity extends AppCompatActivity {
    private ListView listResults;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_results);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }

        listResults = (ListView) findViewById(R.id.movies_searched_list_view);

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            listResults.setAdapter(new ArrayAdapter<MyMovie>(this, R.layout.fragment_movies_list));
        }
    }

}
