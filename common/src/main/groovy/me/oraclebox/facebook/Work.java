
package me.oraclebox.facebook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Work {

    @SerializedName("end_date")
    @Expose
    public String endDate;
    @SerializedName("employer")
    @Expose
    public Employer employer;
    @SerializedName("start_date")
    @Expose
    public String startDate;
    @SerializedName("id")
    //@Expose
    public String id;

}
