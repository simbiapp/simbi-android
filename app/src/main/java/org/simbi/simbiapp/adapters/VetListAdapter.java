package org.simbi.simbiapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.simbi.simbiapp.R;

/**
 * Created by rahul on 1/7/15.
 */
public class VetListAdapter extends RecyclerView.Adapter<VetListAdapter.ViewHolder> {
    //placeholder adapter
    Context context;

    //dummy data for now
    String[] items = new String[]{
            "Jane Doe",
            "John Doe",
            "Steve Harris",
            "Tony Iommi",
            "Hello World",
            "Jane Doe",
            "John Doe",
            "Steve Harris",
            "Tony Iommi",
            "Hello World"
    };

    public VetListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public VetListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vet_list_item_card_view,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VetListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
