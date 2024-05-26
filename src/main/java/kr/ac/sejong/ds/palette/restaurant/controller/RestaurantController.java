package kr.ac.sejong.ds.palette.restaurant.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.sejong.ds.palette.jwt.dto.CustomUserDetails;
import kr.ac.sejong.ds.palette.restaurant.dto.response.RestaurantOverviewResponse;
import kr.ac.sejong.ds.palette.restaurant.dto.response.RestaurantResponse;
import kr.ac.sejong.ds.palette.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "레스토랑 (Restaurant")
@RestController
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // 회원가입 시 선호 레스토랑 선택을 위한 무작위 레스토랑 목록 응답
//    @GetMapping("/join/restaurants")

    @Operation(summary = "레스토랑 추천")
    @GetMapping("/recommended-restaurants")
    public ResponseEntity<List<RestaurantOverviewResponse>> getRecommendedRestaurants(Authentication authentication){
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        List<RestaurantOverviewResponse> restaurantOverviewResponseList = restaurantService.getRecommendedRestaurantListByMember(memberId);
        return ResponseEntity.ok().body(restaurantOverviewResponseList);
    }

    @Operation(summary = "레스토랑 상세 조회")
    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<RestaurantResponse> getRestaurantInfo(@PathVariable(name = "restaurantId") Long restaurantId){
        RestaurantResponse restaurant = restaurantService.getRestaurant(restaurantId);
        return ResponseEntity.ok().body(restaurant);
    }

    @Operation(summary = "상위 레스토랑 조회")
    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantOverviewResponse>> getPopularRestaurants(){
        List<RestaurantOverviewResponse> restaurantOverviewResponseList = restaurantService.getPopularRestaurantList();
        return ResponseEntity.ok().body(restaurantOverviewResponseList);
    }
//
//
//
//    @Operation(summary = "모든 레스토랑 / 자치구별 레스토랑 조회")
//    @GetMapping("/api/restaurants")
//    public List<RestaurantResponseDto> getAllRestaurant(@Parameter(description = "자치구 (기본값: All)") @RequestParam(name = "borough", defaultValue = "All") String borough) {
//        List<RestaurantResponseDto> restaurantResponseDto;
//        if(borough.equals("All")){
//            restaurantResponseDto = restaurantService.findRestaurants()
//                    .stream().map(RestaurantResponseDto::new).collect(Collectors.toList());
//        } else{
//            restaurantResponseDto = restaurantService.findRestaurantsByBorough(borough)
//                    .stream().map(RestaurantResponseDto::new).collect(Collectors.toList());
//        }
//        return restaurantResponseDto;
//    }
//
//    @Operation(summary = "사용자 맞춤 레스토랑 조회")
//    @GetMapping("/api/users/{userId}/recommended-restaurants")
//    public List<RestaurantResponseDto> getRecommendedRestaurant(@Parameter(description = "사용자 id") @PathVariable(name = "userId") Long userId) {
//        final String modelUrl = "http://3.37.115.11:5000";
//
//        WebClient webClient = WebClient.create(modelUrl);
//        String response = webClient.get().uri(
//                uriBuilder -> uriBuilder.path("/predict").queryParam("user", userId).build()
//        ).retrieve().bodyToMono(String.class).block();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            RecommendedRestaurantDto recommendedRestaurantDto = objectMapper.readValue(response, RecommendedRestaurantDto.class);
//            List<Long> recommendedRestaurants = recommendedRestaurantDto.getResult();
//            List<Restaurant> restaurants = restaurantService.findRestaurantsByIds(recommendedRestaurants);
//            List<RestaurantResponseDto> restaurantResponseDto = restaurantService.findRestaurantsByIds(recommendedRestaurants)
//                    .stream().map(RestaurantResponseDto::new).collect(Collectors.toList());
//            return restaurantResponseDto;
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
