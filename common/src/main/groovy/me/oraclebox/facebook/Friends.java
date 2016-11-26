
package me.oraclebox.facebook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Friends {

    @SerializedName("data")
    @Expose
    public List<Object> data = new ArrayList<Object>();
    @SerializedName("summary")
    @Expose
    public Summary summary;

}
