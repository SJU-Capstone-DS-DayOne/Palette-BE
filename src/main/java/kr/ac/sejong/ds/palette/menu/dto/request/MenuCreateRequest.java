package kr.ac.sejong.ds.palette.menu.dto.request;

import jakarta.validation.constraints.NotNull;
import kr.ac.sejong.ds.palette.menu.entity.Menu;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;

public record MenuCreateRequest(
    @NotNull String name,
    String imageUrl,
    Integer ranking,
    @NotNull int price
) {
    public Menu toEntity(Restaurant restaurant) {
        return new Menu(
                name,
                imageUrl,
                ranking,
                price,
                restaurant
        );
    }
}
