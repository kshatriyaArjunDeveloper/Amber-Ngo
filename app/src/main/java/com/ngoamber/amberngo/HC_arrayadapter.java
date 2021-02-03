package com.ngoamber.amberngo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class HC_arrayadapter extends ArrayAdapter<Map> {

    StorageReference storageReference;
    String hc_id;

    public HC_arrayadapter(@NonNull Context context, @NonNull ArrayList<Map> objects, String hc_id) {
        super(context, 0, objects);
        this.hc_id = hc_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(/* view for each item in list */R.layout.lv_hc_item, parent, false);
        }

        // Get the data of the object for this position
        Map<String, Object> healthcard_member = getItem(position);

        // Lookup view for data population......matlab wo sari cheeze jo display honi hai list ki ek element ki

        TextView name_tv = convertView.findViewById(R.id.name_tv);
        TextView age_tv = convertView.findViewById(R.id.age_tv);
        TextView fat_hus_name_tv = convertView.findViewById(R.id.fat_hus_name_tv);
        TextView mothers_name_tv = convertView.findViewById(R.id.mothers_name_tv);
        TextView address_tv = convertView.findViewById(R.id.address_tv);
        TextView mobile_no_tv = convertView.findViewById(R.id.mobile_no_tv);
        ImageView imageView_dp = convertView.findViewById(R.id.imageView_dp);
        ImageView imageView_ip = convertView.findViewById(R.id.imageView_ip_hcm);

        // Reference to an image file in Cloud Storage
        storageReference = FirebaseStorage.getInstance().getReference("HEALTHCARDS/"+ hc_id  +"/"
                + healthcard_member.get("NAME").toString() + "/PROFILE_IMAGE.jpg");

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        Log.i("IMAGE","HEALTHCARDS/"+ hc_id  +"/"
                + healthcard_member.get("NAME").toString() + "/PROFILE_IMAGE.jpg");
        GlideApp.with(getContext() /* context */)
                .load(storageReference)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView_dp);

        // Reference to an image file in Cloud Storage
        storageReference = FirebaseStorage.getInstance().getReference("HEALTHCARDS/"+ hc_id +"/"
                + healthcard_member.get("NAME").toString() + "/IDENTITY_IMAGE.jpg");

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        GlideApp.with(getContext() /* context */)
                .load(storageReference)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView_ip);

        imageView_dp.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView_ip.setScaleType(ImageView.ScaleType.FIT_XY);


        // Populate the data into the template view using the data object........items ki value set krna

        //noinspection ConstantConditions
        name_tv.setText(healthcard_member.get("NAME").toString());
        age_tv.setText(healthcard_member.get("AGE").toString());
        fat_hus_name_tv.setText(healthcard_member.get("F_NAME").toString());
        mothers_name_tv.setText(healthcard_member.get("M_NAME").toString());
        address_tv.setText(healthcard_member.get("ADDRESS").toString());
        mobile_no_tv.setText(healthcard_member.get("M_NUMBER").toString());


        // Return the completed view to render on screen
        return convertView;
    }
}

