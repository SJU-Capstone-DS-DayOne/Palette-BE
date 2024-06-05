package kr.ac.sejong.ds.palette.datecourse.dto.response;

import kr.ac.sejong.ds.palette.datecourse.entity.DateCourseRestaurant;
import kr.ac.sejong.ds.palette.restaurant.dto.response.RestaurantForDateCourseResponse;
import kr.ac.sejong.ds.palette.review.dto.response.ReviewContentResponse;

public record DateCourseRestaurantResponse(
        Long dateCourseRestaurantId,
        RestaurantForDateCourseResponse restaurant,
        ReviewContentResponse review
) {
    public static DateCourseRestaurantResponse of(DateCourseRestaurant dateCourseRestaurant){
        return new DateCourseRestaurantResponse(
                dateCourseRestaurant.getId(),
                RestaurantForDateCourseResponse.of(dateCourseRestaurant.getRestaurant()),
                dateCourseRestaurant.getReview() != null ? ReviewContentResponse.of(dateCourseRestaurant.getReview()) : null
        );
    }
}
