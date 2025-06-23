package kr.ac.sejong.ds.palette.restaurant.dto.response;

import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import kr.ac.sejong.ds.palette.restaurant.entity.RestaurantType;

public record RestaurantForDateCourseResponse(
        Long id,
        String name,
        RestaurantType restaurantType,
        String district,
        String address
) {
    public static RestaurantForDateCourseResponse of(Restaurant restaurant){
        return new RestaurantForDateCourseResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getRestaurantType(),
                restaurant.getDistrict(),
                restaurant.getAddress()
        );
    }
}
