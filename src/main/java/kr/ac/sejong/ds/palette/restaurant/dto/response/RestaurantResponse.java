package kr.ac.sejong.ds.palette.restaurant.dto.response;

import kr.ac.sejong.ds.palette.menu.dto.response.MenuResponse;
import kr.ac.sejong.ds.palette.menu.entity.Menu;
import kr.ac.sejong.ds.palette.restaurant.entity.Category;
import kr.ac.sejong.ds.palette.restaurant.entity.Type;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;

import java.util.List;

public record RestaurantResponse(
        Long id,
        String name,
        Type type,
        String summary,
        String address,
        Double lat,
        Double lng,
        String distFromStation,
        String openingHours,
        String phone,
        int reviewCount,
        List<CategoryResponse> categoryResponseList,
        List<MenuResponse> menuResponseList
) {
    public static RestaurantResponse of(Restaurant rst, List<Category> categoryList, List<Menu> menuList) {
        return new RestaurantResponse(
                rst.getId(), rst.getName(), rst.getType(), rst.getSummary(), rst.getAddress(), rst.getLat(), rst.getLng(), rst.getDistFromStation(), rst.getOpeningHours(), rst.getPhone(), rst.getReviewCount(),
                categoryList.stream().map(CategoryResponse::of).toList(),
                menuList.stream().map(MenuResponse::of).toList()
        );
    }
}