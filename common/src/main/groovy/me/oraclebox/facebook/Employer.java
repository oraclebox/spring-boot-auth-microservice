
package me.oraclebox.facebook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Employer {

    @SerializedName("id")
    //@Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;

}
