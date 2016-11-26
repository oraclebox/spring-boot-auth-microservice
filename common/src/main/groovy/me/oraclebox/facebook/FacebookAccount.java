
package me.oraclebox.facebook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class FacebookAccount {

    @SerializedName("about")
    @Expose
    public String about;
    @SerializedName("age_range")
    @Expose
    public AgeRange ageRange;
    @SerializedName("birthday")
    @Expose
    public String birthday;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("name")
    @Expose
    public String name;
//    @SerializedName("work")
//    @Expose
//    public List<Work> work = new ArrayList<Work>();
    @SerializedName("location")
    @Expose
    public Location location;
    @SerializedName("locale")
    @Expose
    public String locale;
    @SerializedName("languages")
    @Expose
    public List<Language> languages = new ArrayList<Language>();
    @SerializedName("timezone")
    @Expose
    public Integer timezone;
    @SerializedName("updated_time")
    @Expose
    public String updatedTime;
    @SerializedName("friends")
    @Expose
    public Friends friends;
//    @SerializedName("education")
//    @Expose
//    public List<Education> education = new ArrayList<Education>();

}
