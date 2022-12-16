package com.example.handynotes;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ViewHolder extends  androidx.recyclerview.widget.RecyclerView.ViewHolder{

        ImageView pdf;
        TextView subjectName;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            pdf = itemView.findViewById(R.id.pdf);
            subjectName = itemView.findViewById(R.id.subjectName);
        }


}
