package kr.ac.sejong.ds.palette.restaurant.dto.response;

import kr.ac.sejong.ds.palette.menu.dto.response.RankedMenuResponse;
import kr.ac.sejong.ds.palette.menu.entity.Menu;
import kr.ac.sejong.ds.palette.restaurant.entity.Category;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;

import java.util.List;
import java.util.Set;

public record RestaurantPreviewResponse(
        Long id,
        String name,
        List<CategoryResponse> categoryResponseList,
        RankedMenuResponse rankedMenuResponse
) {
    public static RestaurantPreviewResponse of(Restaurant rst, Set<Category> categoryList, Menu menu) {
        return new RestaurantPreviewResponse(
                rst.getId(), rst.getName(),
                categoryList.stream().map(CategoryResponse::of).toList(),
                RankedMenuResponse.of(menu)
        );
    }
}
