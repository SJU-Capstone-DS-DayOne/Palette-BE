package kr.ac.sejong.ds.palette.restaurant.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.sejong.ds.palette.common.exception.restaurant.NotFoundRestaurantException;
import kr.ac.sejong.ds.palette.menu.entity.Menu;
import kr.ac.sejong.ds.palette.menu.repository.MenuRepository;
import kr.ac.sejong.ds.palette.restaurant.dto.response.RestaurantOverviewResponse;
import kr.ac.sejong.ds.palette.restaurant.dto.response.RestaurantResponse;
import kr.ac.sejong.ds.palette.restaurant.dto.response.model.RecommendedRestaurantResponse;
import kr.ac.sejong.ds.palette.restaurant.entity.Category;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import kr.ac.sejong.ds.palette.restaurant.entity.RestaurantCategory;
import kr.ac.sejong.ds.palette.restaurant.repository.CategoryRepository;
import kr.ac.sejong.ds.palette.restaurant.repository.RestaurantCategoryRepository;
import kr.ac.sejong.ds.palette.restaurant.repository.RestaurantRepository;
import kr.ac.sejong.ds.palette.review.entity.Review;
import kr.ac.sejong.ds.palette.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;

    @Value("${server-info.model.url}")
    private String modelUrl;

    // 1. 이거 랜덤으로? 아니면 2. 리뷰 많은 레스토랑?
    // 1. 랜덤 + 메뉴 사진 있는 것만 가져오게 하고 싶은데, 쿼리가 너무 복잡해져서
    // 만약 1로 하려면 레스토랑 테이블에 대표메뉴 여부(사진 여부)를 담아둬야할 듯
    public List<RestaurantOverviewResponse> getRestaurantListForNewMember(){
        return null;
    }

    public List<RestaurantOverviewResponse> getRecommendedRestaurantListByMember(Long memberId) {

//        WebClient webClient = WebClient.create(modelUrl);
//
//
//        String response = webClient.get().uri(
//                uriBuilder -> uriBuilder.path("/predict").queryParam("user", memberId).build()
//        ).retrieve().bodyToMono(String.class).block();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            RecommendedRestaurantResponse recommendedRestaurantResponse = objectMapper.readValue(response, RecommendedRestaurantResponse.class);
//            List<Long> recommendedRestaurants = recommendedRestaurantResponse.getResult();
//            List<Restaurant> restaurants = restaurantService.findRestaurantsByIds(recommendedRestaurants);
//            List<RestaurantResponseDto> restaurantResponseDto = restaurantService.findRestaurantsByIds(recommendedRestaurants)
//                    .stream().map(RestaurantResponseDto::new).collect(Collectors.toList());
//            return restaurantResponseDto;
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
        return null;
    }

    public RestaurantResponse getRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundRestaurantException::new);
        List<Category> categoryList = restaurantCategoryRepository.findAllCategoryByRestaurantId(restaurantId);
        List<Menu> menuList = menuRepository.findAllByRestaurantId(restaurantId);
//        List<Review> reviewList = reviewRepository.findAllWithMemberByRestaurantId(restaurantId);
        return RestaurantResponse.of(restaurant, categoryList, menuList);
    }

    // 여기에는 단순히 리뷰 많은 레스토랑?
    public List<RestaurantOverviewResponse> getPopularRestaurantList() {
        return null;
    }
}
