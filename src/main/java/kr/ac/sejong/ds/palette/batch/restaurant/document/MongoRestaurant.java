package kr.ac.sejong.ds.palette.batch.restaurant.document;

import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.menu.entity.Menu;
import kr.ac.sejong.ds.palette.restaurant.entity.Category;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import kr.ac.sejong.ds.palette.restaurant.entity.RestaurantCategory;
import kr.ac.sejong.ds.palette.restaurant.entity.RestaurantType;
import kr.ac.sejong.ds.palette.review.entity.Review;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Document
public class MongoRestaurant {

    @Id
    private String id;
    
    @Field("restaurant_id")
    private Long restaurantId;
    
    private String address;
    private String name;
    
    @Field("opening_hours")
    private String openingHours;
    
    private String phone;
    private String type;
    private Double lat;
    private Double lng;
    
    @Field("review_count")
    private int reviewCount;
    
    private String summary;
    private String region;

    @Field("dist_from_station")
    private String distFromStation;

    @Field("categories")
    private List<MongoRestaurantCategory> mongoRestaurantCategoryList;

    @Field("menus")
    private List<MongoMenu> mongoMenuList;

    @Field("reviews")
    private List<MongoReview> mongoReviewList;

    @Getter
    public static class MongoRestaurantCategory {

        @Field("category_id")
        private Long categoryId;

        public RestaurantCategory toRestaurantCategory(Restaurant restaurant, Category category) {
            return new RestaurantCategory(restaurant, category);
        }
    }

    @Getter
    public static class MongoMenu {

        private String name;

        @Field("image_url")
        private String imageUrl;

        private Integer ranking;
        private int price;

        public Menu toMenu(Restaurant restaurant) {
            return new Menu(name, imageUrl, ranking, price, restaurant);
        }
    }

    @Getter
    public static class MongoReview {

        private String content;
        
        @Field("member_id")
        private Long memberId;

        public Review toReview(Restaurant restaurant, Member member) {
            return new Review(content, member, restaurant);
        }
    }

    public Restaurant toRestaurant() {
        return new Restaurant(
                restaurantId,
                name,
                RestaurantType.valueOf((type)), // Type 변환 필요
                summary,
                region,
                address,
                lat,
                lng,
                distFromStation,
                openingHours,
                phone,
                reviewCount
        );
    }
}