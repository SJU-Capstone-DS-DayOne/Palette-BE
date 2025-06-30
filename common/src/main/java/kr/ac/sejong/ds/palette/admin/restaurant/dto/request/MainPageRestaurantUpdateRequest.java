package kr.ac.sejong.ds.palette.admin.restaurant.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MainPageRestaurantUpdateRequest(
        @NotNull(message = "레스토랑 ID 리스트는 필수입니다.")
        List<Long> restaurantIdList
) {}