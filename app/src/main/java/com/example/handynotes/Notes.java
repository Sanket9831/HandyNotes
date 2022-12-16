package com.example.handynotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

//import android.support.annotation.NonNull;

public class Notes extends AppCompatActivity {

     RecyclerView recView;
    FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter;
//    myadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        recView =findViewById(R.id.recview);
//        recView.setHasFixedSize(true);
        recView.setLayoutManager(new LinearLayoutManager(this));

//        adapter = new myadapter(options);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(FirebaseDatabase.getInstance().getReference().child("Physics"), Model.class).build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model,ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder( @NotNull ViewHolder holder, int position,  @NotNull Model model) {
                        holder.subjectName.setText(model.getFilename());

                        holder.subjectName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(holder.subjectName.getContext(), viewpdf.class);
                                intent.putExtra("filename", model.getFilename());
                                intent.putExtra("fileurl", model.getFileurl());

                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                holder.subjectName.getContext().startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.row_layout,parent, false);
                        return new ViewHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recView.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
}