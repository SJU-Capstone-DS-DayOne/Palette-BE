package kr.ac.sejong.ds.palette.restaurant.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.sejong.ds.palette.common.exception.couple.NotCoupleMemberException;
import kr.ac.sejong.ds.palette.common.exception.member.NotFoundMemberException;
import kr.ac.sejong.ds.palette.common.exception.restaurant.FailToSaveRestaurantPreferenceException;
import kr.ac.sejong.ds.palette.common.exception.restaurant.NotFoundRestaurantException;
import kr.ac.sejong.ds.palette.couple.entity.Couple;
import kr.ac.sejong.ds.palette.couple.repository.CoupleRepository;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.member.repository.MemberRepository;
import kr.ac.sejong.ds.palette.menu.entity.Menu;
import kr.ac.sejong.ds.palette.menu.repository.MenuRepository;
import kr.ac.sejong.ds.palette.restaurant.dto.request.RestaurantPreferenceRequest;
import kr.ac.sejong.ds.palette.restaurant.dto.response.RecommendedRestaurantResponse;
import kr.ac.sejong.ds.palette.restaurant.dto.response.RestaurantOverviewResponse;
import kr.ac.sejong.ds.palette.restaurant.dto.response.RestaurantPreviewResponse;
import kr.ac.sejong.ds.palette.restaurant.dto.response.RestaurantResponse;
import kr.ac.sejong.ds.palette.restaurant.dto.response.model.RecommendedRestaurantModelResponse;
import kr.ac.sejong.ds.palette.restaurant.dto.response.model.RestaurantPreferenceModelRequest;
import kr.ac.sejong.ds.palette.restaurant.entity.Category;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import kr.ac.sejong.ds.palette.restaurant.entity.RestaurantCategory;
import kr.ac.sejong.ds.palette.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final MenuRepository menuRepository;
    private final MemberRepository memberRepository;
    private final CoupleRepository coupleRepository;
    private final RestaurantCandidateRepository restaurantCandidateRepository;
    private final RestaurantSuggestionRepository restaurantSuggestionRepository;

    @Value("${server-info.model.url}")
    private String modelUrl;

    // 신규 멤버 선호 레스토랑 후보 리스트 응답
    public List<RestaurantPreviewResponse> getRestaurantListForNewMember(){
        List<Long> restaurantCandidateIdList = restaurantCandidateRepository.findAllRestaurantId();
        List<Restaurant> restaurantList = restaurantRepository.findAllByIdInWithMenuAndCategory(restaurantCandidateIdList);


        return restaurantList.stream().map(
                restaurant -> RestaurantPreviewResponse.of(
                        restaurant,
                        restaurant.getRestaurantCategoryList().stream().map(RestaurantCategory::getCategory).collect(Collectors.toSet()),
                        Menu.get1stRankMenu(restaurant.getMenuList())
                )
        ).toList();
    }

    // 신규 멤버 선호 레스토랑을 통한 임베딩 생성 (feat. 모델 서버)
    @Transactional
    public void createNewMemberEmbeddings(Long memberId, RestaurantPreferenceRequest restaurantPreferenceRequest){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        RestaurantPreferenceModelRequest restaurantPreferenceModelRequest = RestaurantPreferenceModelRequest.of(restaurantPreferenceRequest.restaurantIds());

        // 모델 서버에 '선호 레스토랑' POST 요청
        WebClient webClient = WebClient.create(modelUrl);

        String response = webClient.post().
                uri(
                        uriBuilder -> uriBuilder.path("/coldstart")
                                .queryParam("new_user", memberId)
                                .build()
                ).body(Mono.just(restaurantPreferenceModelRequest), RestaurantPreferenceModelRequest.class)
                .exchangeToMono(clientResponse -> {
                    if(!clientResponse.statusCode().is2xxSuccessful()) {
                        throw new FailToSaveRestaurantPreferenceException();
                    }
                    return clientResponse.bodyToMono(String.class);
                })
                .block();

        member.completedPreferenceSelection();
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
            List<Restaurant> rstRestaurantList = restaurantRepository.findAllByIdsOrderByIdsWithMenuAndCategory(
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
            List<Restaurant> cafeRestaurantList = restaurantRepository.findAllByIdsOrderByIdsWithMenuAndCategory(
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
            List<Restaurant> barRestaurantList = restaurantRepository.findAllByIdsOrderByIdsWithMenuAndCategory(
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

    public List<RestaurantPreviewResponse> getPopularRestaurantList() {
        List<Long> restaurantSuggestionIdList = restaurantSuggestionRepository.findAllRestaurantId();
        List<Restaurant> restaurantList = restaurantRepository.findAllByIdInWithMenuAndCategory(restaurantSuggestionIdList);

        return restaurantList.stream().map(
                restaurant -> RestaurantPreviewResponse.of(
                        restaurant,
                        restaurant.getRestaurantCategoryList().stream().map(RestaurantCategory::getCategory).collect(Collectors.toSet()),
                        Menu.get1stRankMenu(restaurant.getMenuList())
                )
        ).toList();
    }
}
