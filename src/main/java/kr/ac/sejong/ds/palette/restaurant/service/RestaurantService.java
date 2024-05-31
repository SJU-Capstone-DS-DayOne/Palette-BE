package kr.ac.sejong.ds.palette.restaurant.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.sejong.ds.palette.common.exception.couple.NotCoupleMemberException;
import kr.ac.sejong.ds.palette.common.exception.restaurant.NotFoundRestaurantException;
import kr.ac.sejong.ds.palette.couple.entity.Couple;
import kr.ac.sejong.ds.palette.couple.repository.CoupleRepository;
import kr.ac.sejong.ds.palette.menu.entity.Menu;
import kr.ac.sejong.ds.palette.menu.repository.MenuRepository;
import kr.ac.sejong.ds.palette.restaurant.dto.response.RecommendedRestaurantResponse;
import kr.ac.sejong.ds.palette.restaurant.dto.response.RestaurantOverviewResponse;
import kr.ac.sejong.ds.palette.restaurant.dto.response.RestaurantResponse;
import kr.ac.sejong.ds.palette.restaurant.dto.response.model.RecommendedRestaurantModelResponse;
import kr.ac.sejong.ds.palette.restaurant.entity.Category;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import kr.ac.sejong.ds.palette.restaurant.entity.RestaurantCategory;
import kr.ac.sejong.ds.palette.restaurant.repository.CategoryRepository;
import kr.ac.sejong.ds.palette.restaurant.repository.RestaurantCategoryRepository;
import kr.ac.sejong.ds.palette.restaurant.repository.RestaurantRepository;
import kr.ac.sejong.ds.palette.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;
    private final CoupleRepository coupleRepository;

    @Value("${server-info.model.url}")
    private String modelUrl;

    // 1. 이거 랜덤으로? 아니면 2. 리뷰 많은 레스토랑?
    // 1. 랜덤 + 메뉴 사진 있는 것만 가져오게 하고 싶은데, 쿼리가 너무 복잡해져서
    // 만약 1로 하려면 레스토랑 테이블에 대표메뉴 여부(사진 여부)를 담아둬야할 듯
    public List<RestaurantOverviewResponse> getRestaurantListForNewMember(){
        return null;
    }

    public RecommendedRestaurantResponse getRecommendedRestaurantListByMemberAndDistrict(Long memberId, String district, Map<String, Boolean> restaurantTypeMap) {

        Couple couple = coupleRepository.findByMaleIdOrFemaleId(memberId, memberId)
                .orElseThrow(NotCoupleMemberException::new);

        // 모델 서버에 '레스토랑 추천' GET 요청
        WebClient webClient = WebClient.create(modelUrl);

        String response = webClient.get().uri(
                uriBuilder -> uriBuilder.path("/recommend/couple")
                        .queryParam("user1", couple.getMale().getId())
                        .queryParam("user2", couple.getFemale().getId())
                        .queryParam("district", district)
                        .build()
        ).retrieve().bodyToMono(String.class).block();

        ObjectMapper objectMapper = new ObjectMapper();
        RecommendedRestaurantModelResponse recommendedRestaurantModelResponse = null;
        try {
            recommendedRestaurantModelResponse = objectMapper.readValue(response, RecommendedRestaurantModelResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        List<RestaurantOverviewResponse> rstRestaurantOverviewResponseList = null;
        List<RestaurantOverviewResponse> cafeRestaurantOverviewResponseList = null;
        List<RestaurantOverviewResponse> barRestaurantOverviewResponseList = null;

        if (restaurantTypeMap.get("rst")){
            List<Long> rstRestaurantIdList = recommendedRestaurantModelResponse.RST();
            List<Restaurant> rstRestaurantList = restaurantRepository.findAllByIdOrderByIdsWithMenuAndCategory(
                    rstRestaurantIdList, rstRestaurantIdList.stream().map(String::valueOf).collect(Collectors.joining(","))
            );
            rstRestaurantOverviewResponseList = rstRestaurantList.stream().map(
                    restaurant -> RestaurantOverviewResponse.of(
                            restaurant,
                            restaurant.getRestaurantCategoryList().stream().map(RestaurantCategory::getCategory).collect(Collectors.toSet()),
                            Menu.getRankedMenuList(restaurant.getMenuList().stream().distinct().toList())
                    )
            ).toList();
        }
        if (restaurantTypeMap.get("cafe")) {
            List<Long> cafeRestaurantIdList = recommendedRestaurantModelResponse.CAFE();
            List<Restaurant> cafeRestaurantList = restaurantRepository.findAllByIdOrderByIdsWithMenuAndCategory(
                    cafeRestaurantIdList, cafeRestaurantIdList.stream().map(String::valueOf).collect(Collectors.joining(","))
            );
            cafeRestaurantOverviewResponseList = cafeRestaurantList.stream().map(
                    restaurant -> RestaurantOverviewResponse.of(
                            restaurant,
                            restaurant.getRestaurantCategoryList().stream().map(RestaurantCategory::getCategory).collect(Collectors.toSet()),
                            Menu.getRankedMenuList(restaurant.getMenuList().stream().distinct().toList())
                    )
            ).toList();
        }
        if (restaurantTypeMap.get("bar")) {
            List<Long> barRestaurantIdList = recommendedRestaurantModelResponse.BAR();
            List<Restaurant> barRestaurantList = restaurantRepository.findAllByIdOrderByIdsWithMenuAndCategory(
                    barRestaurantIdList, barRestaurantIdList.stream().map(String::valueOf).collect(Collectors.joining(","))
            );
            barRestaurantOverviewResponseList = barRestaurantList.stream().map(
                    restaurant -> RestaurantOverviewResponse.of(
                            restaurant,
                            restaurant.getRestaurantCategoryList().stream().map(RestaurantCategory::getCategory).collect(Collectors.toSet()),
                            Menu.getRankedMenuList(restaurant.getMenuList().stream().distinct().toList())
                    )
            ).toList();
        }
        return new RecommendedRestaurantResponse(
                rstRestaurantOverviewResponseList, cafeRestaurantOverviewResponseList, barRestaurantOverviewResponseList
        );
    }

    public RestaurantResponse getRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundRestaurantException::new);
        List<Category> categoryList = restaurantCategoryRepository.findAllCategoryByRestaurantId(restaurantId);
        List<Menu> menuList = menuRepository.findAllByRestaurantId(restaurantId);
        return RestaurantResponse.of(restaurant, categoryList, menuList);
    }

    // 여기에는 단순히 리뷰 많은 레스토랑?
    public List<RestaurantOverviewResponse> getPopularRestaurantList() {
        return null;
    }
}
