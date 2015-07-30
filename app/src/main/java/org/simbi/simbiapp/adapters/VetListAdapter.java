package org.simbi.simbiapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.activities.VetProfileActivity;
import org.simbi.simbiapp.api.models.Response.Doctor;
import org.simbi.simbiapp.utils.SimbiConstants;

import java.util.ArrayList;
import java.util.List;

public class VetListAdapter extends RecyclerView.Adapter<VetListAdapter.ViewHolder> {

    Context context;
    List<Doctor> doctors = new ArrayList<>();

    public VetListAdapter(Context context, List<Doctor> doctors) {
        this.context = context;
        this.doctors = doctors;
    }

    @Override
    public VetListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vet_list_item_card_view,
                parent, false);
        TextView doctorName = (TextView) v.findViewById(R.id.doctor_name);
        TextView branch = (TextView) v.findViewById(R.id.doctor_branch);
        TextView location = (TextView) v.findViewById(R.id.doctor_location);
        ImageView image = (ImageView) v.findViewById(R.id.doctor_image);

        int id = 0;// This is used for sending the doctor id of the selected doctor to next activity
        return new ViewHolder(v, doctorName, branch, location, image, id);
    }

    @Override
    public void onBindViewHolder(VetListAdapter.ViewHolder holder, int position) {
        Doctor doctor = doctors.get(position);

        holder.doctorId = doctor.getId(); //retrieve the doctor id and assign it to its copy in ViewHolder
        holder.doctorNameTextView.setText("Dr Jane Doe");
        holder.doctorBranchTextView.setText(doctor.getSpecialization());
        holder.doctorLocationTextView.setText("Seattle");
        Picasso.with(context)
                .load(doctor.getPhoto())
                .into(holder.doctorImageView);
    }

    @Override
    public int getItemCount() {
        if (doctors.size() != 0 && doctors != null)
            return doctors.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView doctorNameTextView;
        public TextView doctorBranchTextView;
        public TextView doctorLocationTextView;
        public ImageView doctorImageView;
        public int doctorId;

        public ViewHolder(View itemView, TextView name, TextView branch, TextView location,
                          ImageView image, int id) {
            super(itemView);
            itemView.setOnClickListener(this);
            doctorNameTextView = name;
            doctorBranchTextView = branch;
            doctorLocationTextView = location;
            doctorImageView = image;
            doctorId = id;
        }

        @Override
        public void onClick(View view) {
            Intent mIntent = new Intent(view.getContext(), VetProfileActivity.class);
            mIntent.putExtra(SimbiConstants.BUNDLE_DOC_ID,
                    String.valueOf(doctorId));//sending the id of the selected doctor to next activity
            view.getContext().startActivity(mIntent);
        }
    }

}
