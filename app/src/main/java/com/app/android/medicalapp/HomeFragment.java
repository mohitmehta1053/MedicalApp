package com.app.android.medicalapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

EditText editTextSearch;
RecyclerView recyclerView;
DatabaseReference databaseReference;
FirebaseUser firebaseUser;
RecyclerView.LayoutManager layoutManager;
DividerItemDecoration dividerItemDecoration;
ArrayList<String> medicine_name;
ArrayList<String> med_company_name;
SearchAdapter searchAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View myFragmentView=inflater.inflate(R.layout.fragment_home,container,false);

        editTextSearch=(EditText) myFragmentView.findViewById(R.id.medicine_search);
        recyclerView=(RecyclerView) myFragmentView.findViewById(R.id.recyclerViewSearch);
        layoutManager=new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        dividerItemDecoration=new DividerItemDecoration(this.getActivity(),LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        medicine_name=new ArrayList<>();
        med_company_name=new ArrayList<>();

        recyclerView.setHasFixedSize(true);

       editTextSearch.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty())
                {
                    setAdapter(editable.toString());
                }
                else {
                    medicine_name.clear();
                    med_company_name.clear();
                    recyclerView.removeAllViews();
                }
           }
       });

        return myFragmentView;


    }

    private void setAdapter( final String searchedString) {

        databaseReference.child("medicine").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                medicine_name.clear();
                med_company_name.clear();
                recyclerView.removeAllViews();

                int counter=0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    String uid=snapshot.getKey();
                    String med_name=snapshot.child("name").getValue(String.class);
                    String company_name=snapshot.child("company_name").getValue(String.class);

                    if(med_name.toLowerCase().contains(searchedString.toLowerCase()))
                    {
                            medicine_name.add(med_name);
                            med_company_name.add(company_name);
                            counter++;

                    }else if(company_name.toLowerCase().contains(searchedString.toLowerCase()))
                    {
                        medicine_name.add(med_name);
                        med_company_name.add(company_name);
                        counter++;
                    }

                    if(counter==15)
                            break;




                }
                searchAdapter =new SearchAdapter(getActivity(), medicine_name,med_company_name);
                recyclerView.setAdapter(searchAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
