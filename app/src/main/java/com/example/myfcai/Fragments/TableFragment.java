package com.example.myfcai.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myfcai.Adapters.PostArrayAdapter;
import com.example.myfcai.Adapters.TableArrayAdapter;
import com.example.myfcai.Class.Course;
import com.example.myfcai.Class.Post;
import com.example.myfcai.Class.Table;
import com.example.myfcai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class TableFragment extends Fragment {

    String userID, type;
    ListView lvTable;
    ArrayList<String> tableList;
    TableArrayAdapter tableArrayAdapter;

    public TableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        lvTable = view.findViewById(R.id.lv_fragment_table);

        //must know: *(assert)* getArguments() != null;
        type = getArguments().getString("type");


        if (type.equals("t")) {
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User Info");
            tableList = new ArrayList<>();
            tableArrayAdapter = new TableArrayAdapter(getContext(), tableList, "t");
            lvTable.setAdapter(tableArrayAdapter);
            databaseReference.child(userID).child("courses").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tableList.clear();
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        tableList.add(snap.getValue(String.class));
                        tableArrayAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {

            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();///////
            tableList = new ArrayList<>();
            tableArrayAdapter = new TableArrayAdapter(getContext(), tableList, "s");
            lvTable.setAdapter(tableArrayAdapter);
            //tableList.clear();
            databaseReference.child("Attendance").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    databaseReference.child("User Info").child(userID).child("courses").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            tableList.clear();
                            for (DataSnapshot snap : dataSnapshot.getChildren()) {


                                String cn = snap.child("course name").getValue(String.class);
                                assert cn != null;
                                databaseReference.child("Attendance").child(cn).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        //tableList.clear();
                                        for (DataSnapshot snap : snapshot.getChildren()) {
                                            String x = snap.getKey();
                                            if (x.equals("last")) {
                                                String value = snap.getValue(String.class);
                                                if (!value.equals("")) {
                                                    if (!isExist(cn,tableList)){
                                                        tableList.add(cn);
                                                        tableArrayAdapter.notifyDataSetChanged();
                                                    }

                                                    //break;
                                                }
//attID=snap.getKey();


                                            }
                                            tableArrayAdapter.notifyDataSetChanged();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                                //start//
                            /*databaseReference.child(userID).child("courses").child(code).child("course name").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshotC) {
                                    String cn = snapshotC.getValue(String.class);
                                    FirebaseDatabase.getInstance().getReference().child("Attendance").child(cn).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                            for (DataSnapshot snap : snapshot.getChildren()) {
                                                String x = snap.getKey();
                                                if (x.equals("last")) {
                                                    String value = snap.getValue(String.class);
                                                    if (!value.equals("")) {

                                                        tableList.add(cn);
                                                        tableArrayAdapter.notifyDataSetChanged();

                                                    }

                                                }

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });


                                }

                                @Override
                                public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                                }
                            });*/
                                //end//
                                //tableArrayAdapter.notifyDataSetChanged();


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }


        return view;
    }
    boolean isExist(String preCode, ArrayList<String> sCourses) {
        boolean r = false;
        for (String s : sCourses) {
            if (preCode.equals(s)) {
                r = true;
                break;
            }
        }
        return r;
    }
}