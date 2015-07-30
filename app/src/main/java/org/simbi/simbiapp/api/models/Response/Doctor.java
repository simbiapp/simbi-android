package org.simbi.simbiapp.api.models.Response;

import com.google.gson.annotations.SerializedName;

import org.simbi.simbiapp.utils.SimbiConstants;


/**
 * POJO implementation for Doctor Object
 */
public class Doctor {

    public static String GENDER_MALE = "M";
    public static String GENDER_FEMALE = "F";
    public static String GENDER_OTHER = "O";

    @SerializedName("id")
    private int id;

    @SerializedName("email")
    private String email;

    @SerializedName("degree")
    private String degree;

    @SerializedName("specialization")
    private String specialization;

    @SerializedName("gender")
    private String genderChoice; //'M' or 'F' or 'O'

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("date_of_birth")
    private String dateOfBirth;

    @SerializedName("years_of_experience")
    private String experience;

    @SerializedName("photo")
    private String photo;

    @SerializedName("added_on")
    private String addedOn;

    @SerializedName("last_updated_on")
    private String lastUpdatedOn;

    @SerializedName("user_profile")
    private String userProfile; // All Doctors must also be our users

    @SerializedName("clinic")
    private String clinic;

    @SerializedName("language")
    private String language;

    public int getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public String getClinic() {
        return clinic;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getDegree() {
        return degree;
    }

    public String getEmail() {
        return email;
    }

    public String getExperience() {
        return experience;
    }

    public String getGenderChoice() {
        return genderChoice;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhoto() {
        return SimbiConstants.baseApiUrl + photo;
    }

    public String getSpecialization() {
        return specialization;
    }
}
