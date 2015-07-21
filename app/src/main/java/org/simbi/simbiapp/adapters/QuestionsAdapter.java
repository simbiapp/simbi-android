package org.simbi.simbiapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.utils.Question;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    Context context;
    List<Question> questions;

    public QuestionsAdapter(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @Override
    public QuestionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.questions_list_item_view,
                parent, false);
        TextView question = (TextView) v.findViewById(R.id.question_text_view);
        ImageView textDrawableImage = (ImageView) v.findViewById(R.id.text_drawable_imageview);
        return new ViewHolder(v, question, textDrawableImage);
    }

    @Override
    public void onBindViewHolder(QuestionsAdapter.ViewHolder holder, int position) {
        Question question = questions.get(position);

        holder.question.setText(question.getQuestion());

        ColorGenerator generator = ColorGenerator.MATERIAL;
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(question.getQuestion().charAt(0)),
                        generator.getColor(position));

        holder.textDrawable.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        if (questions != null && questions.size() != 0)
            return questions.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView question;
        ImageView textDrawable;

        public ViewHolder(View itemView, TextView question, ImageView textDrawable) {
            super(itemView);
            this.question = question;
            this.textDrawable = textDrawable;
        }

        @Override
        public void onClick(View view) {

        }
    }
}
