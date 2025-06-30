package kr.ac.sejong.ds.palette.restaurant.entity;

import jakarta.persistence.*;
import kr.ac.sejong.ds.palette.common.entity.BaseEntity;
import kr.ac.sejong.ds.palette.menu.entity.Menu;
import kr.ac.sejong.ds.palette.review.entity.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends BaseEntity {
    @Id
    @Column(name = "restaurant_id")
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private RestaurantType restaurantType;

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
    private Set<RestaurantCategory> restaurantCategoryList = new HashSet<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviewList = new ArrayList<>();

    public Restaurant(Long id, String name, RestaurantType restaurantType, String summary, String district, String address, Double lat, Double lng, String distFromStation, String openingHours, String phone, int reviewCount) {
        this.id = id;
        this.name = name;
        this.restaurantType = restaurantType;
        this.summary = summary;
        this.district = district;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.distFromStation = distFromStation;
        this.openingHours = openingHours;
        this.phone = phone;
        this.reviewCount = reviewCount;
    }

    // 모든 정보 업데이트
    public void update(String name, RestaurantType restaurantType, String summary, String district, String address, Double lat, Double lng, String distFromStation, String openingHours, String phone, int reviewCount) {
        this.name = name;
        this.restaurantType = restaurantType;
        this.summary = summary;
        this.district = district;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.distFromStation = distFromStation;
        this.openingHours = openingHours;
        this.phone = phone;
        this.reviewCount = reviewCount;
    }

    public void increaseReviewCount(){
        this.reviewCount++;
    }

    public void decreaseReviewCount(){
        if (this.reviewCount > 0)
            this.reviewCount--;
    }
}
