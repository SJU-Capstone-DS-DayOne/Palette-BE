package kr.ac.sejong.ds.palette.restaurant.entity;

import jakarta.persistence.*;
import kr.ac.sejong.ds.palette.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    private String summary;

    private String district;

    private String address;

    private Double lat;

    private Double lng;

    private String distFromStation;

    private String openingHours;

    private String phone;

    private int reviewCount;

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantCategory> restaurantCategoryList = new ArrayList<>();

    public void increaseReviewCount(){
        this.reviewCount++;
    }

    public void decreaseReviewCount(){
        this.reviewCount--;  // 리뷰가 존재할 때 실행되므로 음수가 될 수 없음
    }
}
