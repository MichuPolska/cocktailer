package com.example.cocktailer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=alcoholic";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> updateView(response),
                error -> Log.i("error", error.getMessage())
        );
        queue.add(jsonArrayRequest);

        String[] spinnerItems = {"Alcoholic", "Non Alcoholic"};
        Spinner spinnerListCocktail= findViewById(R.id.spinnerCocktail);
        spinnerListCocktail.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spinnerItems));

        ListView listView = findViewById(R.id.listViewCocktail);
        listView.setOnItemClickListener(((parent, view, position, id) -> clickOnListItem(parent, position, view)));
    }

    public void clickOnListItem(AdapterView<?> parent, int position, View view) {
        Cocktail cocktail = (Cocktail)parent.getAdapter().getItem(position);

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("id", cocktail.getId());
        startActivity(intent);
    }

    private void updateView(JSONObject response) {
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = response.getJSONArray("drinks");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<Cocktail> cocktailList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Cocktail cocktail = jsonToCocktail(jsonObject);
                cocktailList.add(cocktail);
            }
            catch (JSONException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
        fillListView(cocktailList);
    }

    private void fillListView(List<Cocktail> list) {
        ListView listViewCocktail = findViewById(R.id.listViewCocktail);
        ArrayAdapter<Cocktail> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listViewCocktail.setAdapter(arrayAdapter);
        Toast.makeText(this, "done", Toast.LENGTH_LONG).show();
    }

    private Cocktail jsonToCocktail(JSONObject jsonObject) {
        Cocktail cocktail = new Cocktail();
        try {
            cocktail.setId(jsonObject.getString("idDrink"));
            cocktail.setName(jsonObject.getString("strDrink"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cocktail;

    }

}