
package me.oraclebox.facebook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Education {

    @SerializedName("school")
    @Expose
    public School school;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("year")
    @Expose
    public Year year;
    @SerializedName("id")
    //@Expose
    public String id;
    @SerializedName("concentration")
    @Expose
    public List<Concentration> concentration = new ArrayList<Concentration>();
    @SerializedName("degree")
    @Expose
    public Degree degree;

}
