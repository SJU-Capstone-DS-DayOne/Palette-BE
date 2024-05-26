package kr.ac.sejong.ds.palette.restaurant.dto.response;

import java.util.List;

public record RecommendedRestaurantResponse(
        List<RestaurantOverviewResponse> rstRestaurantOverviewResponseList,
        List<RestaurantOverviewResponse> cafeRestaurantOverviewResponseList,
        List<RestaurantOverviewResponse> barRestaurantOverviewResponseList
) {
}
