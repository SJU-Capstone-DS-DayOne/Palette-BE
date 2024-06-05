package kr.ac.sejong.ds.palette.datecourse.service;

import kr.ac.sejong.ds.palette.common.exception.couple.NotCoupleMemberException;
import kr.ac.sejong.ds.palette.common.exception.datecourse.NotFoundDateCourseRestaurantException;
import kr.ac.sejong.ds.palette.common.exception.member.NotFoundMemberException;
import kr.ac.sejong.ds.palette.common.exception.restaurant.NotFoundRestaurantException;
import kr.ac.sejong.ds.palette.couple.entity.Couple;
import kr.ac.sejong.ds.palette.couple.repository.CoupleRepository;
import kr.ac.sejong.ds.palette.datecourse.dto.request.DateCourseRequest;
import kr.ac.sejong.ds.palette.datecourse.dto.response.DateCourseResponse;
import kr.ac.sejong.ds.palette.datecourse.entity.DateCourse;
import kr.ac.sejong.ds.palette.datecourse.entity.DateCourseRestaurant;
import kr.ac.sejong.ds.palette.datecourse.repository.DateCourseRepository;
import kr.ac.sejong.ds.palette.datecourse.repository.DateCourseRestaurantRepository;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import kr.ac.sejong.ds.palette.restaurant.repository.RestaurantRepository;
import kr.ac.sejong.ds.palette.review.dto.request.ReviewCreateRequest;
import kr.ac.sejong.ds.palette.review.entity.Review;
import kr.ac.sejong.ds.palette.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DateCourseService {

    private final MemberRepository memberRepository;
    private final CoupleRepository coupleRepository;
    private final DateCourseRepository dateCourseRepository;
    private final RestaurantRepository restaurantRepository;
    private final DateCourseRestaurantRepository dateCourseRestaurantRepository;
    private final ReviewRepository reviewRepository;

    public List<DateCourseResponse> getDateCourseList(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Couple couple = coupleRepository.findByMaleIdOrFemaleId(memberId, memberId)
                .orElseThrow(NotCoupleMemberException::new);

        List<DateCourse> dateCourseList = dateCourseRepository.findAllByCoupleIdWithRestaurantAndReviewOrderByCreatedAtDesc(couple.getId());
        return dateCourseList.stream().map(DateCourseResponse::of).toList();
    }

    @Transactional
    public void createDateCourse(Long memberId, DateCourseRequest dateCourseRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Couple couple = coupleRepository.findByMaleIdOrFemaleId(memberId, memberId)
                .orElseThrow(NotCoupleMemberException::new);

        // 데이트 코스 생성
        DateCourse dateCourse = new DateCourse(couple);

        // 데이트 코스 레스토랑 생성
        if(dateCourseRequest.rstRestaurantId() != null) {
            Restaurant rstRestaurant = restaurantRepository.findById(dateCourseRequest.rstRestaurantId())
                    .orElseThrow(NotFoundRestaurantException::new);
            dateCourse.addDateCourseRestaurant(new DateCourseRestaurant(dateCourse, rstRestaurant));
        }
        if(dateCourseRequest.cafeRestaurantId() != null) {
            Restaurant cafeRestaurant = restaurantRepository.findById(dateCourseRequest.cafeRestaurantId())
                    .orElseThrow(NotFoundRestaurantException::new);
            dateCourse.addDateCourseRestaurant(new DateCourseRestaurant(dateCourse, cafeRestaurant));
        }
        if(dateCourseRequest.barRestaurantId() != null) {
            Restaurant barRestaurant = restaurantRepository.findById(dateCourseRequest.barRestaurantId())
                    .orElseThrow(NotFoundRestaurantException::new);
            dateCourse.addDateCourseRestaurant(new DateCourseRestaurant(dateCourse, barRestaurant));
        }

        dateCourseRepository.save(dateCourse);
    }


    @Transactional
    public void createReviewOfDateCourse(Long memberId, Long dateCourseRestaurantId, ReviewCreateRequest reviewCreateRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Couple couple = coupleRepository.findByMaleIdOrFemaleId(memberId, memberId)
                .orElseThrow(NotCoupleMemberException::new);

        DateCourseRestaurant dateCourseRestaurant = dateCourseRestaurantRepository.findById(dateCourseRestaurantId)
                .orElseThrow(NotFoundDateCourseRestaurantException::new);

        // 리뷰 생성
        Review review = new Review(
                reviewCreateRequest.content(),
                member,
                dateCourseRestaurant.getRestaurant()
        );
        reviewRepository.save(review);

        // 레스토랑의 리뷰 개수 증가 -> 트리거로 처리함
        // dateCourseRestaurant.getRestaurant().increaseReviewCount();

        // 데이트 코스 페이지에서 리뷰 작성 완료
        dateCourseRestaurant.reviewCreated(review);
    }
}
