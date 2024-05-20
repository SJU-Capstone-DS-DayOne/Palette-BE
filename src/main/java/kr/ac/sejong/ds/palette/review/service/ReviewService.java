package kr.ac.sejong.ds.palette.review.service;

import kr.ac.sejong.ds.palette.common.exception.member.NotFoundMemberException;
import kr.ac.sejong.ds.palette.common.exception.restaurant.NotFoundRestaurantException;
import kr.ac.sejong.ds.palette.common.exception.review.NotFoundReviewException;
import kr.ac.sejong.ds.palette.common.exception.review.NotMatchingReviewException;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import kr.ac.sejong.ds.palette.restaurant.repository.RestaurantRepository;
import kr.ac.sejong.ds.palette.review.dto.request.ReviewCreateRequest;
import kr.ac.sejong.ds.palette.review.dto.request.ReviewUpdateRequest;
import kr.ac.sejong.ds.palette.review.dto.response.ReviewResponse;
import kr.ac.sejong.ds.palette.review.entity.Review;
import kr.ac.sejong.ds.palette.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    public List<ReviewResponse> getAllReviewByRestaurant(Long restaurantId){
        List<ReviewResponse> reviewResponseList = reviewRepository.findAllWithMemberByRestaurantId(restaurantId)
                .stream().map(review -> ReviewResponse.of(review)).collect(Collectors.toList());
        return reviewResponseList;
    }
}
