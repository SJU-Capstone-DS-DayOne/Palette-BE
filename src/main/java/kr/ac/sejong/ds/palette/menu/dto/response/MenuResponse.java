package kr.ac.sejong.ds.palette.menu.dto.response;

import kr.ac.sejong.ds.palette.menu.entity.Menu;

public record MenuResponse(
        Long id,
        String name,
        String imageUrl,
        Integer ranking,
        int price
) {
    public static MenuResponse of(Menu menu){
        return new MenuResponse(menu.getId(), menu.getName(), menu.getImageUrl(), menu.getRanking(), menu.getPrice());
    }
}
