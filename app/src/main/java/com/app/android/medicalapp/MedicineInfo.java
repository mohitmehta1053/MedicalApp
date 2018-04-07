package com.app.android.medicalapp;

/**
 * Created by Mohit Mehta on 4/7/2018.
 */

public class MedicineInfo {


    String name;
    String company_name;
    String id;

    public MedicineInfo(String name,String company_name,String id) {
        this.name = name;
        this.company_name=company_name;
        this.id=id;
    }

    MedicineInfo()
    {

    }

    public String getName() {
        return name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getId() {
        return id;
    }
}
