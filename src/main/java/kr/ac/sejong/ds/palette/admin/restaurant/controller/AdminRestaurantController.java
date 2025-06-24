package kr.ac.sejong.ds.palette.admin.restaurant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.ac.sejong.ds.palette.admin.restaurant.dto.request.MainPageRestaurantUpdateRequest;
import kr.ac.sejong.ds.palette.admin.restaurant.dto.request.RestaurantCreateRequest;
import kr.ac.sejong.ds.palette.admin.restaurant.dto.request.RestaurantUpdateRequest;
import kr.ac.sejong.ds.palette.admin.restaurant.service.AdminRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 (Admin)")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminRestaurantController {

    private final AdminRestaurantService adminRestaurantService;

    @Operation(summary = "크롤링 데이터 기반 레스토랑 통합 배치 작업 실행")
    @PostMapping("/crawl/restaurants")
    public ResponseEntity<Void> executeRestaurantIntegrationBatch(@RequestParam(name = "district") String district, @RequestParam(name = "version") Integer version) {
        adminRestaurantService.executeRestaurantIntegrationBatch(district, version);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "레스토랑 생성")
    @PostMapping("/restaurants")
    public ResponseEntity<Void> createRestaurant(@RequestBody @Valid RestaurantCreateRequest request) {
        adminRestaurantService.createRestaurant(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "레스토랑 수정")
    @PutMapping("/restaurants")
    public ResponseEntity<Void> updateRestaurant(@RequestBody @Valid RestaurantUpdateRequest request) {
        adminRestaurantService.updateRestaurant(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "레스토랑 삭제")
    @DeleteMapping("/restaurants/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable(name = "restaurantId") Long restaurantId) {
        adminRestaurantService.deleteRestaurant(restaurantId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "메인페이지 레스토랑 수정")
    @PutMapping("/main-page-restaurants")
    public ResponseEntity<Void> updateMainPageRestaurants(@RequestBody MainPageRestaurantUpdateRequest request) {
        adminRestaurantService.updateMainPageRestaurants(request);
        return ResponseEntity.noContent().build();
    }
}