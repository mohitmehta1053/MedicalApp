package com.app.android.medicalapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.support.v7.widget.LinearLayoutManager.VERTICAL;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

EditText editTextSearch;
RecyclerView recyclerView;
DatabaseReference databaseReference;
FirebaseUser firebaseUser;
RecyclerView.LayoutManager layoutManager;
DividerItemDecoration dividerItemDecoration;
ArrayList<String> medicine_name;
ArrayList<String> med_company_name;
SearchAdapter searchAdapter;

private StorageReference storageReference;


    private static final int PICK_IMAGE_REQUEST = 234;

    //Buttons
    private Button buttonChoose;
    private Button buttonUpload;

    //ImageView
    private ImageView imageView;

    //a Uri object to store file path
    private Uri filePath;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View myFragmentView=inflater.inflate(R.layout.fragment_home,container,false);
        Toast.makeText(getActivity(),"It is working",Toast.LENGTH_SHORT);
        editTextSearch=(EditText) myFragmentView.findViewById(R.id.medicine_search);
        recyclerView=(RecyclerView) myFragmentView.findViewById(R.id.recyclerViewSearch);

        buttonChoose=(Button) myFragmentView.findViewById(R.id.buttonChoose);
        buttonUpload=(Button) myFragmentView.findViewById(R.id.buttonUpload);

        imageView=(ImageView) myFragmentView.findViewById(R.id.pres_upload);

        storageReference= FirebaseStorage.getInstance().getReference();

       buttonChoose.setOnClickListener(this);
       buttonUpload.setOnClickListener(this);
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

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver() , filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    private void uploadFile()
    {
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this.getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = storageReference.child("images/prescription.jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getActivity().getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getActivity().getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
            Toast.makeText(getActivity().getApplicationContext(), "No file is present to upload", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View view) {
        //if the clicked button is choose
        if (view == buttonChoose) {
            showFileChooser();
        }
        //if the clicked button is upload
        else if (view == buttonUpload) {
            uploadFile();
        }
    }
}
