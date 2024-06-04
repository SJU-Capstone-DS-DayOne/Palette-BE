package kr.ac.sejong.ds.palette.restaurant.dto.response;

import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import kr.ac.sejong.ds.palette.restaurant.entity.Type;

public record RestaurantForDateCourseResponse(
        Long id,
        String name,
        Type type,
        String district,
        String address
) {
    public static RestaurantForDateCourseResponse of(Restaurant restaurant){
        return new RestaurantForDateCourseResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getType(),
                restaurant.getDistrict(),
                restaurant.getAddress()
        );
    }
}
