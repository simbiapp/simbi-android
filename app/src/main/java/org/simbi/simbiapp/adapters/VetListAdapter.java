package org.simbi.simbiapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.activities.VetProfileActivity;
import org.simbi.simbiapp.utils.Constants;

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
        TextView doctorName = (TextView) v.findViewById(R.id.doctor_name);
        TextView branch = (TextView) v.findViewById(R.id.doctor_branch);
        TextView location = (TextView) v.findViewById(R.id.doctor_location);
        return new ViewHolder(v, doctorName, branch, location);
    }

    @Override
    public void onBindViewHolder(VetListAdapter.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return items.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView doctorNameTextView;
        public TextView doctorBranchTextView;
        public TextView doctorLocationTextView;

        public ViewHolder(View itemView, TextView name, TextView branch, TextView location) {
            super(itemView);
            itemView.setOnClickListener(this);
            doctorNameTextView = name;
            doctorBranchTextView = branch;
            doctorLocationTextView = location;
        }

        @Override
        public void onClick(View view) {
            Intent mIntent = new Intent(view.getContext(), VetProfileActivity.class);
            mIntent.putExtra(Constants.DOC_NAME, doctorNameTextView.getText().toString());
            mIntent.putExtra(Constants.DOC_BRANCH, doctorBranchTextView.getText().toString());
            mIntent.putExtra(Constants.DOC_LOCATION, doctorLocationTextView.getText().toString());

            view.getContext().startActivity(mIntent);
        }
    }

}
