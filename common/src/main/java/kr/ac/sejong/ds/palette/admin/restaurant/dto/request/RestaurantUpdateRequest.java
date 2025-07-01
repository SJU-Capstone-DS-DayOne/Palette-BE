package kr.ac.sejong.ds.palette.admin.restaurant.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kr.ac.sejong.ds.palette.menu.dto.request.MenuCreateRequest;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import kr.ac.sejong.ds.palette.restaurant.entity.RestaurantType;

import java.util.List;

public record RestaurantUpdateRequest(
        @NotNull Long id,
        @NotNull String name,
        @NotNull RestaurantType restaurantType,
        String summary,
        @NotNull String district,
        @NotNull String address,
        @NotNull Double lat,
        @NotNull Double lng,
        String distFromStation,
        String openingHours,
        String phone,
        @NotNull int reviewCount,
        @NotNull List<Long> categoryIdList,
        @NotNull @Valid List<MenuCreateRequest> menuCreateRequestList
) {
    public Restaurant toEntity() {
        return new Restaurant(
                id,
                name,
                restaurantType,
                summary,
                district,
                address,
                lat,
                lng,
                distFromStation,
                openingHours,
                phone,
                reviewCount
        );
    }
}