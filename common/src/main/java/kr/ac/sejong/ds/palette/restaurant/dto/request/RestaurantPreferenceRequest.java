package kr.ac.sejong.ds.palette.restaurant.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RestaurantPreferenceRequest(
        @NotNull List<Long> restaurantIds
) {
}
