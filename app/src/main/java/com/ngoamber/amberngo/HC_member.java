package com.ngoamber.amberngo;

import android.net.Uri;

public class HC_member {

    private String name;
    private String age;
    private String fat_hus_name;
    private String mothers_name;
    private String address;
    private String mobile_number;
    private Uri person_image_uri = null;
    private Uri identity_image_uri = null;


    public HC_member() {
    }

    public String getName() {
        return name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getAge() {
        return age;
    }

    public String getFat_hus_name() {
        return fat_hus_name;
    }

    public String getMothers_name() {
        return mothers_name;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setFat_hus_name(String fat_hus_name) {
        this.fat_hus_name = fat_hus_name;
    }

    public void setMothers_name(String mothers_name) {
        this.mothers_name = mothers_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public Uri getPerson_image_uri() {
        return person_image_uri;
    }

    public void setPerson_image_uri(Uri person_image_uri) {
        this.person_image_uri = person_image_uri;
    }

    public Uri getIdentity_image_uri() {
        return identity_image_uri;
    }

    public void setIdentity_image_uri(Uri identity_image_uri) {
        this.identity_image_uri = identity_image_uri;
    }
}

