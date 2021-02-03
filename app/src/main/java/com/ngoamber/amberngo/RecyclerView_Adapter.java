package com.ngoamber.amberngo;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.widget.ImageView.ScaleType.CENTER_CROP;
import static android.widget.ImageView.ScaleType.FIT_XY;
import static com.ngoamber.amberngo.Create_HealthcardActivity.dp_fab_clicked;
import static com.ngoamber.amberngo.R.drawable.ic_image_black_24dp;
import static com.ngoamber.amberngo.R.drawable.ic_person_black_24dp;


public class RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerView_Adapter.MyViewHolder> {

    /**
     * HEALTHCARD AL THAT STORES ALL THE INFO TO BE UPLOADED
     */
    static ArrayList<HC_member> healthcard_members_al_main;
    private Context ctx;
    private LayoutInflater inflater;
    /**
     * LISTENER TO HAVE ONCLICK AND DO SOMETHING FROM ANOTHER ACTIVITY
     */
    private RV_ItemClickListener clickListener;


    RecyclerView_Adapter(Context ctx, ArrayList<HC_member> healthcard_members_al_main, RV_ItemClickListener clickListener) {
        this.ctx = ctx;
        inflater = LayoutInflater.from(ctx);
        RecyclerView_Adapter.healthcard_members_al_main = healthcard_members_al_main;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_hc_item,parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    /**
     * SETS ALL INFO TO LAYOUT VIEW OF RECYCLERVIEW
     * TIP : WHEN THERE IS A PROBLEM ADD HERE WHAT TO DO AND WHAT NOT TO
     */
    @Override
    public void onBindViewHolder(final RecyclerView_Adapter.MyViewHolder holder, final int position) {
        holder.editText_name.setText(healthcard_members_al_main.get(position).getName());
        holder.editText_address.setText(healthcard_members_al_main.get(position).getAddress());
        if (holder.editText_age.getText().toString().equals("null") || holder.editText_age == null) {
            holder.editText_age.setText("");
        }
        holder.editText_age.setText(String.valueOf(healthcard_members_al_main.get(position).getAge()));
        holder.editText_fat_hus_name.setText(healthcard_members_al_main.get(position).getFat_hus_name());
        holder.editText_mothers_name.setText(healthcard_members_al_main.get(position).getMothers_name());
        if (holder.editText_mobile_no.getText().toString().equals("null") || holder.editText_mobile_no == null) {
            holder.editText_mobile_no.setText("");
        }
        holder.editText_mobile_no.setText(String.valueOf(healthcard_members_al_main.get(position).getMobile_number()));
        holder.delete_member_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                healthcard_members_al_main.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, healthcard_members_al_main.size());
                Toast.makeText(ctx, "Member Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        if (healthcard_members_al_main.get(position).getPerson_image_uri() != null) {
            holder.imageView_dp.setImageURI(healthcard_members_al_main.get(position).getPerson_image_uri());
            holder.imageView_dp.setScaleType(CENTER_CROP);
        } else {
            holder.imageView_dp.setImageResource(ic_person_black_24dp);
            holder.imageView_dp.setAdjustViewBounds(true);

        }
        if (healthcard_members_al_main.get(position).getIdentity_image_uri() != null) {
            holder.imageView_ip.setImageURI(healthcard_members_al_main.get(position).getIdentity_image_uri());
            holder.imageView_ip.setScaleType(FIT_XY);
        } else {
            holder.imageView_ip.setImageResource(ic_image_black_24dp);
            holder.imageView_dp.setAdjustViewBounds(true);
        }
    }

    @Override
    public int getItemCount() {
        return healthcard_members_al_main.size();
    }


    /**
     * ALL VIEWS ARE REFERENCED AND CREATED
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        EditText editText_name;
        EditText editText_age;
        EditText editText_fat_hus_name;
        EditText editText_mothers_name;
        EditText editText_address;
        EditText editText_mobile_no;
        ImageButton delete_member_card;
        ImageView imageView_dp;
        ImageButton floatingActionButton_set_dp;
        ImageView imageView_ip;
        ImageButton floatingActionButton_set_ip;


        public MyViewHolder(View itemView) {
            super(itemView);
            editText_name = itemView.findViewById(R.id.editText_name);
            editText_age = itemView.findViewById(R.id.editText_age);
            editText_fat_hus_name = itemView.findViewById(R.id.editText_fat_hus_name);
            editText_mothers_name = itemView.findViewById(R.id.editText_mothers_name);
            editText_address = itemView.findViewById(R.id.editText_address);
            editText_mobile_no = itemView.findViewById(R.id.editText_mobile_no);
            delete_member_card = itemView.findViewById(R.id.imageButton_delete_card);
            imageView_dp = itemView.findViewById(R.id.imageView_dp);
            floatingActionButton_set_dp = itemView.findViewById(R.id.floatingActionButton);
            imageView_ip = itemView.findViewById(R.id.imageView_ip_hcm);
            floatingActionButton_set_ip = itemView.findViewById(R.id.floatingActionButton2);


            editText_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    healthcard_members_al_main.get(getAdapterPosition()).setName(editText_name.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            editText_age.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (editText_age.getText().toString().equals("null") || editText_age == null) {
                        editText_age.setText("");
                    }
                    healthcard_members_al_main.get(getAdapterPosition()).setAge(String.valueOf(editText_age.getText()));
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            editText_fat_hus_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    healthcard_members_al_main.get(getAdapterPosition()).setFat_hus_name(editText_fat_hus_name.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            editText_mothers_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    healthcard_members_al_main.get(getAdapterPosition()).setMothers_name(editText_mothers_name.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            editText_address.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    healthcard_members_al_main.get(getAdapterPosition()).setAddress(editText_address.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            editText_mobile_no.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (editText_mobile_no.getText().toString().equals("null") || editText_mobile_no == null) {
                        editText_mobile_no.setText("");
                    }
                    healthcard_members_al_main.get(getAdapterPosition()).setMobile_number(editText_mobile_no.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            floatingActionButton_set_dp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, getAdapterPosition());
                    dp_fab_clicked = true;
                }
            });

            floatingActionButton_set_ip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, getAdapterPosition());
                    dp_fab_clicked = false;
                }
            });


        }

    }
}
