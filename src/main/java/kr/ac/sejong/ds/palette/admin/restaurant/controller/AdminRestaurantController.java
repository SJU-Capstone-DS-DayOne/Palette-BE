package kr.ac.sejong.ds.palette.admin.restaurant.controller;

import jakarta.validation.Valid;
import kr.ac.sejong.ds.palette.admin.restaurant.dto.request.MainPageRestaurantUpdateRequest;
import kr.ac.sejong.ds.palette.admin.restaurant.dto.request.RestaurantCreateRequest;
import kr.ac.sejong.ds.palette.admin.restaurant.dto.request.RestaurantUpdateRequest;
import kr.ac.sejong.ds.palette.admin.restaurant.service.AdminRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminRestaurantController {

    private final AdminRestaurantService adminRestaurantService;

    @PostMapping("/restaurants")
    public ResponseEntity<Void> createRestaurant(@RequestBody @Valid RestaurantCreateRequest request) {
        adminRestaurantService.createRestaurant(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/restaurants")
    public ResponseEntity<Void> updateRestaurant(@RequestBody @Valid RestaurantUpdateRequest request) {
        adminRestaurantService.updateRestaurant(request);
        return ResponseEntity.noContent().build();
    }
}