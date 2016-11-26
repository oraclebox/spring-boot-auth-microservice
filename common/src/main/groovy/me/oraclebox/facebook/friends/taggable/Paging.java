
package me.oraclebox.facebook.friends.taggable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Paging {

    @SerializedName("cursors")
    @Expose
    public Cursors cursors;
    @SerializedName("next")
    @Expose
    public String next;

}
