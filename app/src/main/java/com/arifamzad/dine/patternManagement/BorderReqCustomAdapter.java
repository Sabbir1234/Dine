package com.arifamzad.dine.patternManagement;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arifamzad.dine.DineProfileActivity;
import com.arifamzad.dine.ManagerActivity;
import com.arifamzad.dine.R;
import com.arifamzad.dine.managerfragments.PendingBorderFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BorderReqCustomAdapter extends RecyclerView.Adapter<BorderReqCustomAdapter.ReqViewHolder> {

    private List<BorderReq>bList = new ArrayList<>();
    private Context context;
    private DatabaseReference myBorderDatabase, requestDatabase, myManagerDatabase;
    private FirebaseAuth mAuth;
    String managerId;
    RecyclerView recyclerView;

    public BorderReqCustomAdapter(List<BorderReq>bList,Context context){
        this.bList= bList;
        this.context=context;
    }

    @NonNull
    @Override
    public ReqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.pattern_request,parent,false);

        mAuth = FirebaseAuth.getInstance();
        managerId= mAuth.getCurrentUser().getUid();
        myBorderDatabase = FirebaseDatabase.getInstance().getReference().child("manager").child(managerId).child("my_border");
        requestDatabase = FirebaseDatabase.getInstance().getReference().child("manager").child(managerId).child("request");
        myManagerDatabase = FirebaseDatabase.getInstance().getReference().child("border");



        return new ReqViewHolder(view);
    }

    //set taken data from firebase to own layout
    @Override
    public void onBindViewHolder(@NonNull ReqViewHolder reqViewHolder, int position) {

        BorderReq borderReq = bList.get(position);

        //reqViewHolder.textViewName.setText(borderReq.name);
        //reqViewHolder.textViewPhone.setText(borderReq.phone);
        //reqViewHolder.textUid.setText(borderReq.uid);

        reqViewHolder.forExpand(borderReq);

        reqViewHolder.itemView.setOnClickListener(v -> {

            boolean expanded = borderReq.isExpanded();
            borderReq.setExpanded(!expanded);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return bList.size();
    }

    public class ReqViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewName;
        TextView textViewPhone;
        TextView textUid;
        Button acceptBtn;
        Button declineBtn;

        public ReqViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.reqShow_name);
            textViewPhone = itemView.findViewById(R.id.reqShow_phone);
            acceptBtn = itemView.findViewById(R.id.accept);
            declineBtn = itemView.findViewById(R.id.cancel_req);
            textUid = itemView.findViewById(R.id.invisible_uid);

            acceptBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    BorderReq borderReq = bList.get(getAdapterPosition());
                    String name = borderReq.name;
                    String phone =borderReq.phone;
                    String uid = borderReq.uid;

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
                    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

                    final String time = simpleDateFormat.format(calendar.getTime());
                    final int date = Integer.parseInt(dateFormat.format(calendar.getTime()));
                    final int month = Integer.parseInt(monthFormat.format(calendar.getTime()));
                    final int year = Integer.parseInt(yearFormat.format(calendar.getTime()));


                    DatabaseReference newRef = myBorderDatabase.child(uid);

                    int a = 0;

                    newRef.child("name").setValue(name);
                    newRef.child("phone").setValue(phone);
                    newRef.child("uid").setValue(uid);
                    newRef.child("start_from").setValue(time);
                    newRef.child("date").setValue(date);
                    newRef.child("days_on").setValue(a);
                    newRef.child("off_varriable").setValue(a);
                    newRef.child("paid").setValue(a);
                    newRef.child("status").setValue("ON");


                    //newRef.child("month").setValue(month);
                    //newRef.child("year").setValue(year);
                    //newRef.child("days_off").setValue("0");

                    myManagerDatabase.child(uid).child("my_manager").setValue(managerId);

                    requestDatabase.child(uid).removeValue();
                    deleteItem(getAdapterPosition());
                    notifyDataSetChanged();


                    Toast.makeText(context,"You have accepted "+name,Toast.LENGTH_LONG).show();
                    getItemCount();
                }
            });

            itemView.setOnClickListener(this);

        }

        //for deleting an item from list
        private void deleteItem(int position) {
            bList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(), bList.size());
        }

        public void addNewItem(ArrayList<BorderReq> newContent) {
            int start = bList.size();
            int end = newContent.size();
            bList.addAll(newContent);
            notifyDataSetChanged();
        }

        private void forExpand(BorderReq borderReq){

            boolean expanded = borderReq.isExpanded();
            //textUid.setVisibility(expanded ? View.VISIBLE : View.GONE);
            acceptBtn.setVisibility(expanded ? View.VISIBLE : View.GONE);
            declineBtn.setVisibility(expanded ? View.VISIBLE : View.GONE);

            textViewName.setText(borderReq.name);
            textViewPhone.setText(borderReq.phone);
            textUid.setText(borderReq.uid);
        }
        //default click option per item
        @Override
        public void onClick(View view) {
/*
            Intent intent = new Intent(context, DineProfileActivity.class);

            intent.putExtra("name", borderReq.name);
            intent.putExtra("phone",borderReq.phone);
            intent.putExtra("uid",borderReq.uid);

            context.startActivity(intent);
            */
        }
    }
}