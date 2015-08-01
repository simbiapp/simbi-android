package org.simbi.simbiapp.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.api.models.Response.Doctor;
import org.simbi.simbiapp.ui.DoctorProfileFragment;
import org.simbi.simbiapp.utils.SimbiConstants;

import java.util.ArrayList;
import java.util.List;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ViewHolder> {

    Context context;
    List<Doctor> doctors = new ArrayList<>();

    public DoctorListAdapter(Context context, List<Doctor> doctors) {
        this.context = context;
        this.doctors = doctors;
    }

    @Override
    public DoctorListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vet_list_item_card_view,
                parent, false);
        TextView doctorName = (TextView) v.findViewById(R.id.doctor_name);
        TextView branch = (TextView) v.findViewById(R.id.doctor_branch);
        TextView location = (TextView) v.findViewById(R.id.doctor_location);
        ImageView image = (ImageView) v.findViewById(R.id.doctor_image);

        return new ViewHolder(v, doctorName, branch, location, image);
    }

    @Override
    public void onBindViewHolder(DoctorListAdapter.ViewHolder holder, int position) {
        final Doctor doctor = doctors.get(position);

        holder.doctorNameTextView.setText("Dr Jane Doe");
        holder.doctorBranchTextView.setText(doctor.getSpecialization());
        holder.doctorLocationTextView.setText("Seattle");
        Picasso.with(context)
                .load(doctor.getPhoto())
                .into(holder.doctorImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DoctorProfileFragment fragment = new DoctorProfileFragment();

                Bundle bundle = new Bundle();
                bundle.putString(SimbiConstants.BUNDLE_DOC_ID,
                        String.valueOf(doctor.getId()));
                fragment.setArguments(bundle);

                ((FragmentActivity) context).getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.main_activity_container, fragment)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (doctors.size() != 0 && doctors != null)
            return doctors.size();
        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView doctorNameTextView;
        public TextView doctorBranchTextView;
        public TextView doctorLocationTextView;
        public ImageView doctorImageView;

        public ViewHolder(View itemView, TextView name, TextView branch, TextView location,
                          ImageView image) {
            super(itemView);
            doctorNameTextView = name;
            doctorBranchTextView = branch;
            doctorLocationTextView = location;
            doctorImageView = image;
        }

    }

}
