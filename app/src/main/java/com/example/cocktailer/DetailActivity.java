package com.example.cocktailer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String id  = "";
        if(intent != null) {
            if(intent.hasExtra("id")) {
                id = intent.getStringExtra("id");
            }
        }

        String url = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=" + id;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> jsonToCocktail(response),
                error -> Log.i("error", error.getMessage())
        );
        queue.add(jsonObjectRequest);



    }

    private void fillListViewIngredients(List<String> list) {
        ListView listViewCocktail = findViewById(R.id.ListViewIngredients);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.simple_item_small, list);
        listViewCocktail.setAdapter(arrayAdapter);
    }

    private void fillListViewQuantity(List<String> list) {
        ListView listViewCocktail = findViewById(R.id.ListViewQuantity);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.simple_item_small, list);
        listViewCocktail.setAdapter(arrayAdapter);
    }

    private void jsonToCocktail(JSONObject response) {
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = response.getJSONArray("drinks");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error array", Toast.LENGTH_SHORT).show();
        }
        Cocktail cocktail = new Cocktail();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                cocktail.setName(jsonObject.getString("strDrink"));
                cocktail.setDiscribtion(jsonObject.getString("strInstructions"));
                cocktail.setImg(jsonObject.getString("strDrinkThumb"));
                cocktail.setIngredients(new String[]{jsonObject.getString("strIngredient1"),
                        jsonObject.getString("strIngredient2"),
                        jsonObject.getString("strIngredient3"),
                        jsonObject.getString("strIngredient4"),
                        jsonObject.getString("strIngredient5")});
                cocktail.setQuantitys(new String[]{jsonObject.getString("strMeasure1"),
                        jsonObject.getString("strMeasure2"),
                        jsonObject.getString("strMeasure3"),
                        jsonObject.getString("strMeasure4"),
                        jsonObject.getString("strMeasure5")});
            } catch (JSONException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }

        TextView textView = findViewById(R.id.textViewCocktailName);
        textView.setText(cocktail.getName());

        TextView describtion = findViewById(R.id.textViewDiscribtion);
        describtion.setText(cocktail.getDiscribtion());

        ImageView imageView = findViewById(R.id.imageView);
        Picasso.get().load(cocktail.getImg()).into(imageView);

        fillListViewIngredients(Arrays.asList(cocktail.getIngredients()));
        fillListViewQuantity(Arrays.asList(cocktail.getQuantitys()));
    }

    public void back(View view) {
        finish();
    }
}