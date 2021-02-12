package com.example.gark.entites;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class User implements Parcelable, Serializable {

    String id;
    String firstName;
    String lastName;
    String email;
    String address;
    String password;
    String phone;
    String photo;
    Date last_login_date;
    Date birth_date;
    String signed_up_with;
    Date sign_up_date;
    Boolean completedInformation;
    Boolean activation;

    public Boolean getActivation() {
        return activation;
    }

    public void setActivation(Boolean activation) {
        this.activation = activation;
    }

    public Boolean getCompletedInformation() {
        return completedInformation;
    }

    public void setCompletedInformation(Boolean completedInformation) {
        this.completedInformation = completedInformation;
    }

    public Date getLast_login_date() {
        return last_login_date;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLast_login_date(Date last_login_date) {
        this.last_login_date = last_login_date;
    }


    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public String getSigned_up_with() {
        return signed_up_with;
    }

    public void setSigned_up_with(String signed_up_with) {
        this.signed_up_with = signed_up_with;
    }

    public Date getSign_up_date() {
        return sign_up_date;
    }

    public void setSign_up_date(Date sign_up_date) {
        this.sign_up_date = sign_up_date;
    }

    public User(){}

    public User(String firstName, String lastName, String email, String address, String phone,String photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.phone = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(email);
        parcel.writeString(address);
        parcel.writeString(password);
        parcel.writeString(phone);
        parcel.writeString(photo);
        parcel.writeString(signed_up_with);
        parcel.writeSerializable(last_login_date);
        parcel.writeSerializable(birth_date);
        parcel.writeSerializable(sign_up_date);
    }

    public static final Creator<User> CREATOR = new Creator<User>(){
        @Override
        public User createFromParcel(Parcel source) {
            return new User();
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}