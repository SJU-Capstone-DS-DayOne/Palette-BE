package kr.ac.sejong.ds.palette.datecourse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.ac.sejong.ds.palette.datecourse.dto.request.DateCourseRequest;
import kr.ac.sejong.ds.palette.datecourse.dto.response.DateCourseResponse;
import kr.ac.sejong.ds.palette.datecourse.service.DateCourseService;
import kr.ac.sejong.ds.palette.jwt.dto.CustomUserDetails;
import kr.ac.sejong.ds.palette.review.dto.request.ReviewCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "데이트 코스 (Date Course)")
@RestController
@RequiredArgsConstructor
public class DateCourseController {

    private final DateCourseService dateCourseService;

    @Operation(summary = "데이트 코스 조회")
    @GetMapping("/date-courses")
    public ResponseEntity<List<DateCourseResponse>> getDateCourses(Authentication authentication){
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();

        List<DateCourseResponse> dateCourseResponseList = dateCourseService.getDateCourseList(memberId);
        return ResponseEntity.ok().body(dateCourseResponseList);
    }

    @Operation(summary = "데이트 코스 생성")
    @PostMapping("/date-courses")
    public ResponseEntity<Void> createDateCourse(Authentication authentication, @RequestBody DateCourseRequest dateCourseRequest) {
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();

        dateCourseService.createDateCourse(memberId, dateCourseRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "데이트 코스 리뷰 생성", description = "추천된 데이트 코스 레스토랑에 대한 리뷰를 생성함 (추천된 레스토랑에 대해서는 한 번만 리뷰를 작성할 수 있기 때문에 별도로 필요함)")
    @PostMapping("/date-course-restaurant/{dateCourseRestaurantId}/reviews")
    public ResponseEntity<Void> createDateCourseRestaurantReview(Authentication authentication, @PathVariable(name = "dateCourseRestaurantId") Long dateCourseRestaurantId, @RequestBody @Valid ReviewCreateRequest reviewCreateRequest) {
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();

        dateCourseService.createReviewOfDateCourse(memberId, dateCourseRestaurantId, reviewCreateRequest);
        return ResponseEntity.ok().build();
    }
}
