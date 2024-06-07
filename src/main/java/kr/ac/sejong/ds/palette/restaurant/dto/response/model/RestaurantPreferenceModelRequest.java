package kr.ac.sejong.ds.palette.restaurant.dto.response.model;

import java.util.List;

public record RestaurantPreferenceModelRequest(
        List<Long> restaurantids
) {
    public static RestaurantPreferenceModelRequest of(List<Long> restaurantIds) {
        return new RestaurantPreferenceModelRequest(restaurantIds);
    }
}
