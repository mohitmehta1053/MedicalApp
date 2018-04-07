package com.app.android.medicalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText medicine_name;
    EditText med_company_name;
    Button add_medicine;
    Button logout;

    DatabaseReference dbmedicine_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        medicine_name=findViewById(R.id.medicine_name);
        med_company_name=findViewById(R.id.med_company_name);
        add_medicine=findViewById(R.id.btn_add_medicine);
        logout=findViewById(R.id.btn_logout);
        dbmedicine_info= FirebaseDatabase.getInstance().getReference("medicine");

        add_medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    addMedicine();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=  new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//so that after user logs in after pressing back button he should not go back to the login page
                startActivity(intent);
                finish();
            }
        });


    }

    private void addMedicine()
    {
        String name=medicine_name.getText().toString().trim();
        String company_name=med_company_name.getText().toString().trim();

        if(!TextUtils.isEmpty(name) )
        {
               String id= dbmedicine_info.push().getKey();
               MedicineInfo medicineInfo=new MedicineInfo(name,company_name,id);
               dbmedicine_info.child(id).setValue(medicineInfo);

               Toast.makeText(this,"The medicine is added",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"The name is empty",Toast.LENGTH_SHORT).show();
        }
    }
}
