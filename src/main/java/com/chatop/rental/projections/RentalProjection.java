package com.chatop.rental.projections;

import java.time.LocalDate;

public interface RentalProjection {
    Long getId();
    String getName();
    Double getSurface();
    Double getPrice();
    String getPicture();
    String getDescription();
    Long getOwnerId(); // You might have to customize this part
    LocalDate getCreatedAt();
    LocalDate getUpdatedAt();
}
