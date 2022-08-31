package com.harry.presensikrama.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CacahKramaMipilGetResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private CacahKramaMipilPaginate cacahKramaMipilPaginate;
    @SerializedName("message")
    @Expose
    private String message;
}
