package kr.ac.sejong.ds.palette.restaurant.dto.response.model;

import java.util.List;

public record RecommendedRestaurantModelResponse(
    List<Long> RST,
    List<Long> CAFE,
    List<Long> BAR
) {
}
