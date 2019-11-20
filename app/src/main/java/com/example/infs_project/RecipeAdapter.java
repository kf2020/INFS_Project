package com.example.infs_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs_project.model.Recipe;
import com.example.infs_project.ui.recipes.RecipeDetailActivity;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipesToAdapt;

    public void setData(List<Recipe> recipesToAdapt) {
        this.recipesToAdapt = recipesToAdapt;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recipe, parent, false);

        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(view);
        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        final Recipe recipeAtPosition = recipesToAdapt.get(position);

        // Contrast how I wrote this method with the method for ArticleAdapter. They both achieve
        // the same goal, but this way is cleaner. I defined my own method "bind" in the RecipeViewHolder
        // class, and all the assignment and setup is done in there instead.
        holder.bind(recipeAtPosition);
    }

    @Override
    public int getItemCount() {
        return recipesToAdapt.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView nameTextView;

        // This constructor is used in onCreateViewHolder
        public RecipeViewHolder(View v) {
            super(v);  // runs the constructor for the ViewHolder superclass
            view = v;
            nameTextView = v.findViewById(R.id.recipe_name);
        }

        // See comment in onBindViewHolder
        public void bind(final Recipe recipe) {
            nameTextView.setText(recipe.getTitle());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();

                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra("id", recipe.getId());
                    intent.putExtra("title", recipe.getTitle());
                    context.startActivity(intent);
                }
            });

        }
    }
}

