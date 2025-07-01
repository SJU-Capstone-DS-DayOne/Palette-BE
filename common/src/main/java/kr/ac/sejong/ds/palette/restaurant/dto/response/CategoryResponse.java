package kr.ac.sejong.ds.palette.restaurant.dto.response;

import kr.ac.sejong.ds.palette.restaurant.entity.Category;

public record CategoryResponse(
        Long id,
        String name
) {
    public static CategoryResponse of(Category category){
        return new CategoryResponse(category.getId(), category.getName());
    }
}
