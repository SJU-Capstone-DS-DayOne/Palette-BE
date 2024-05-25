package kr.ac.sejong.ds.palette.restaurant.dto.response;

import kr.ac.sejong.ds.palette.menu.dto.response.RankedMenuResponse;
import kr.ac.sejong.ds.palette.menu.entity.Menu;
import kr.ac.sejong.ds.palette.restaurant.entity.Category;
import kr.ac.sejong.ds.palette.restaurant.entity.Type;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;

import java.util.List;

public record RestaurantOverviewResponse(
        Long id,
        String name,
        Type type,
        String summary,
        Double lat,
        Double lng,
        List<CategoryResponse> categoryResponseList,
        List<RankedMenuResponse> rankedMenuResponseList
) {
    public static RestaurantOverviewResponse of(Restaurant rst, List<Category> categoryList, List<Menu> menuList) {
        return new RestaurantOverviewResponse(
                rst.getId(), rst.getName(), rst.getType(), rst.getSummary(), rst.getLat(), rst.getLng(),
                categoryList.stream().map(category -> CategoryResponse.of(category)).toList(),
                menuList.stream().map(menu -> RankedMenuResponse.of(menu)).toList()
        );
    }
}
