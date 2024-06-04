package kr.ac.sejong.ds.palette.datecourse.dto.response;

import kr.ac.sejong.ds.palette.datecourse.entity.DateCourse;

import java.time.LocalDateTime;
import java.util.List;

public record DateCourseResponse(
        Long dateCourseId,
        LocalDateTime createdAt,
        List<DateCourseRestaurantResponse> dateCourseRestaurantList
) {
    public static DateCourseResponse of(DateCourse dateCourse){
        return new DateCourseResponse(
                dateCourse.getId(), dateCourse.getCreatedAt(),
                dateCourse.getDateCourseRestaurants().stream().map(DateCourseRestaurantResponse::of).toList()
        );
    }
}
