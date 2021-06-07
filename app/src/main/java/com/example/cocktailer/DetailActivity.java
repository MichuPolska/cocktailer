package com.example.cocktailer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
                response -> updateView(response),
                error -> Log.i("error", error.getMessage())
        );
        queue.add(jsonObjectRequest);
    }

    private void updateView(JSONObject response) {
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
            } catch (JSONException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }

        TextView textView = findViewById(R.id.textViewCocktailName);
        textView.setText(cocktail.getName());

        TextView describtion = findViewById(R.id.textViewDiscribtion);
        describtion.setText(cocktail.getDiscribtion());

        ImageView imageView = findViewById(R.id.imageView);

//        try {
//            Bitmap bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
//            imageView.setImageBitmap(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Picasso.get().load(cocktail.getImg()).into(imageView);
    }

    public void back(View view) {
        finish();
    }
}