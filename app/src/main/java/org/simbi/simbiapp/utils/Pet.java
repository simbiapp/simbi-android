package org.simbi.simbiapp.utils;

import com.google.gson.annotations.SerializedName;

/**
 * DTO implementation for Pet Object
 */
public class Pet {

    public static String PET_TYPE_CAT = "Cat";
    public static String PET_TYPE_DOG = "Dog";
    public static String PET_TYPE_HAMSTER = "Hamster";
    public static String PET_TYPE_RABBIT = "Rabbit";
    public static String PET_TYPE_OTHER = "Other";

    @SerializedName("pet_name")
    private String petName;

    @SerializedName("pet_type")
    private String petType;

    @SerializedName("gender")
    private String petGender;

    @SerializedName("date_of_birth")
    private String dateOfBirth;

    @SerializedName("age")
    private int age;

    @SerializedName("weight")
    private String weight;

    @SerializedName("photo_url")
    private String photoUrl;

    public int getAge() {
        return age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPetGender() {
        return petGender;
    }

    public String getPetName() {
        return petName;
    }

    public String getPetType() {
        return petType;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getWeight() {
        return weight;
    }
}
