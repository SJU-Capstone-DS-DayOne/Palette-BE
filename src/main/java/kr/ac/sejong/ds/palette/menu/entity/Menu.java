package kr.ac.sejong.ds.palette.menu.entity;

import jakarta.persistence.*;
import kr.ac.sejong.ds.palette.common.entity.BaseEntity;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="menu_id")
    private Long id;

    private String name;

    private String imageUrl;

    private Integer ranking;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public static List<Menu> getRankedMenuList(List<Menu> menuList){
        return menuList.stream().filter(menu -> menu.getRanking() != null).toList();
    }

    public static Menu get1stRankMenu(List<Menu> menuList){
        return menuList.stream()
                .filter(menu -> menu.getRanking() != null && menu.getRanking() == 1)
                .findFirst()
                .orElse(null);
    }
}
