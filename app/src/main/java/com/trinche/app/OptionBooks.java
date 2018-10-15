package com.trinche.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.trinche.app.adapters.RVbooks;

public class OptionBooks extends AppCompatActivity {

    RecyclerView RVoptionBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_books);

        RVoptionBooks = (RecyclerView) findViewById(R.id.RVoptionBooks);
        RVoptionBooks.setLayoutManager(new LinearLayoutManager(this));

        RVbooks adapterBooks = new RVbooks(this);
        RVoptionBooks.setAdapter(adapterBooks);
    }
}
