package kr.ac.sejong.ds.palette.menu.dto.response;

import kr.ac.sejong.ds.palette.menu.entity.Menu;

public record RankedMenuResponse(
        Long id,
        String name,
        String imageUrl,
        Integer ranking
) {
    public static RankedMenuResponse of(Menu menu){
        return new RankedMenuResponse(menu.getId(), menu.getName(), menu.getImageUrl(), menu.getRanking());
    }
}
