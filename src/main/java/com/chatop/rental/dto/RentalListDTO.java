package com.chatop.rental.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RentalListDTO extends RentalDTO{
    @JsonProperty("picture")
    private String picture;

    @JsonIgnore
    @Override
    public String[] getPictures() {
        return super.getPictures();
    }

    @JsonIgnore
    @Override
    public void setPictures(String[] pictures) {
        super.setPictures(pictures);
        this.picture = pictures != null && pictures.length > 0 ? pictures[0] : "";
    }

    public String getPicture() {
        return picture != null ? picture : "";
    }

    public void setPicture(String picture) {
        this.picture = picture != null ? picture : "";
    }
}
