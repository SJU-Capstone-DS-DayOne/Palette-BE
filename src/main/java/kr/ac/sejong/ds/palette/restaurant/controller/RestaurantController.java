package kr.ac.sejong.ds.palette.restaurant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.ac.sejong.ds.palette.jwt.dto.CustomUserDetails;
import kr.ac.sejong.ds.palette.restaurant.dto.request.RestaurantPreferenceRequest;
import kr.ac.sejong.ds.palette.restaurant.dto.response.RecommendedRestaurantResponse;
import kr.ac.sejong.ds.palette.restaurant.dto.response.RestaurantPreviewResponse;
import kr.ac.sejong.ds.palette.restaurant.dto.response.RestaurantResponse;
import kr.ac.sejong.ds.palette.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "레스토랑 (Restaurant")
@RestController
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // 회원가입 시 선호 레스토랑 선택을 위한 레스토랑 목록 응답
    @Operation(summary = "신규 유저 선호 레스토랑 후보 목록 조회")
    @GetMapping("/join/restaurants")
    public ResponseEntity<List<RestaurantPreviewResponse>> getRestaurantCandidates(){
        List<RestaurantPreviewResponse> restaurantListForNewMember = restaurantService.getRestaurantListForNewMember();
        return ResponseEntity.ok().body(restaurantListForNewMember);
    }


    @Operation(summary = "커플 레스토랑 추천")
    @GetMapping("/recommended-restaurants")
    public ResponseEntity<RecommendedRestaurantResponse> getRecommendedRestaurants(
            Authentication authentication,
            @RequestParam(name = "district") String district,
            @RequestParam(name = "rst", defaultValue = "false") Boolean rst,
            @RequestParam(name = "cafe", defaultValue = "false") Boolean cafe,
            @RequestParam(name = "bar", defaultValue = "false") Boolean bar
    ) {
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getMemberId();
        Map<String, Boolean> restaurantTypeMap = Map.of("rst", rst, "cafe", cafe, "bar", bar);
        RecommendedRestaurantResponse recommendedRestaurantResponse = restaurantService.getRecommendedRestaurantListByMemberAndDistrict(memberId, district, restaurantTypeMap);
        return ResponseEntity.ok().body(recommendedRestaurantResponse);
    }

    @Operation(summary = "레스토랑 상세 조회")
    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<RestaurantResponse> getRestaurantInfo(@PathVariable(name = "restaurantId") Long restaurantId){
        RestaurantResponse restaurant = restaurantService.getRestaurant(restaurantId);
        return ResponseEntity.ok().body(restaurant);
    }

    @Operation(summary = "메인 페이지 레스토랑 제안")
    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantPreviewResponse>> getPopularRestaurants(){
        List<RestaurantPreviewResponse> restaurantPreviewResponseList = restaurantService.getPopularRestaurantList();
        return ResponseEntity.ok().body(restaurantPreviewResponseList);
    }
}
