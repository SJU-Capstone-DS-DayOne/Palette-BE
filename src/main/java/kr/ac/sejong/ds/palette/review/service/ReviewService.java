package kr.ac.sejong.ds.palette.review.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.sejong.ds.palette.common.exception.member.NotFoundMemberException;
import kr.ac.sejong.ds.palette.common.exception.restaurant.NotFoundRestaurantException;
import kr.ac.sejong.ds.palette.common.exception.review.NotFoundReviewException;
import kr.ac.sejong.ds.palette.common.exception.review.NotMatchingReviewException;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import kr.ac.sejong.ds.palette.restaurant.repository.RestaurantRepository;
import kr.ac.sejong.ds.palette.review.dto.model.SimilarMemberReviewModelResponse;
import kr.ac.sejong.ds.palette.review.dto.request.ReviewCreateRequest;
import kr.ac.sejong.ds.palette.review.dto.request.ReviewUpdateRequest;
import kr.ac.sejong.ds.palette.review.dto.response.ReviewResponse;
import kr.ac.sejong.ds.palette.review.entity.Review;
import kr.ac.sejong.ds.palette.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    @Value("${server-info.model.url}")
    private String modelUrl;

    public List<ReviewResponse> getAllReviewByRestaurant(Long restaurantId, Pageable pageable){
        // 레스토랑 존재 여부 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundRestaurantException::new);

        // 해당 레스토랑의 리뷰 + 멤버 정보를 가져옴
        List<ReviewResponse> reviewResponseList = reviewRepository.findAllWithMemberByRestaurantIdOrderByCreatedAtDesc(restaurantId, pageable)
                .stream().map(ReviewResponse::of).collect(Collectors.toList());
        return reviewResponseList;
    }

    public List<ReviewResponse> getAllSimilarMemberReviewByRestaurant(Long memberId, Long restaurantId){
        // 멤버 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        // 레스토랑 존재 여부 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundRestaurantException::new);

        // 모델 서버에 '유사 취향 유저의 리뷰 조회' GET 요청
        WebClient webClient = WebClient.create(modelUrl);

        String response = webClient.get().uri(
                uriBuilder -> uriBuilder.path("/review/sort")
                        .queryParam("user_id", memberId)
                        .queryParam("restaurant_id", restaurantId)
                        .build()
        ).retrieve().bodyToMono(String.class).block();

        ObjectMapper objectMapper = new ObjectMapper();
        SimilarMemberReviewModelResponse similarMemberReviewModelResponse = null;
        try {
            similarMemberReviewModelResponse = objectMapper.readValue(response, SimilarMemberReviewModelResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        List<Long> reviewIdList = similarMemberReviewModelResponse.review_ids();

        // 해당 리뷰들을 멤버 정보와 함께 가져옴
        List<Review> reviewList = reviewRepository.findTop30WithMemberByRestaurantIdOrderByIds(
                restaurantId,
                reviewIdList,
                reviewIdList.stream().map(String::valueOf).collect(Collectors.joining(","))
        );

        List<ReviewResponse> reviewResponseList = reviewList.stream().map(ReviewResponse::of).toList();
        return reviewResponseList;
    }

    @Transactional
    public void createReview(ReviewCreateRequest reviewCreateRequest, Long memberId, Long restaurantId) {
        // 멤버 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        // 레스토랑 존재 여부 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundRestaurantException::new);

        // 리뷰 생성
        Review review = new Review(reviewCreateRequest.content(), member, restaurant);
        reviewRepository.save(review);

        // 레스토랑의 리뷰 개수 증가 -> 트리거로 처리함
        // restaurant.increaseReviewCount();
    }

    @Transactional
    public void updateReview(ReviewUpdateRequest reviewUpdateRequest, Long memberId, Long reviewId){
        // 멤버 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(NotFoundReviewException::new);

        // 본인의 리뷰인지 검증
        if(review.getMember().getId() != memberId)
            throw new NotMatchingReviewException();

        review.updateReview(reviewUpdateRequest.content());
    }

    @Transactional
    public void deleteReview(Long memberId, Long reviewId){
        // 멤버 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(NotFoundReviewException::new);

        // 본인의 리뷰인지 검증
        if(review.getMember().getId() != memberId)
            throw new NotMatchingReviewException();

        // 리뷰 삭제
        reviewRepository.delete(review);

        // 레스토랑의 리뷰 개수 감소 -> 트리거로 처리함
        // review.getRestaurant().decreaseReviewCount();
    }

}
