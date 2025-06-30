package kr.ac.sejong.ds.palette.datecourse.dto.request;

public record DateCourseRequest(
        Long rstRestaurantId,
        Long cafeRestaurantId,
        Long barRestaurantId
) {
}
