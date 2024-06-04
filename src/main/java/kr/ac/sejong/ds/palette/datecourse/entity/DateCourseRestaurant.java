package kr.ac.sejong.ds.palette.datecourse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.sejong.ds.palette.common.entity.BaseEntity;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import kr.ac.sejong.ds.palette.review.entity.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateCourseRestaurant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_course_restaurant_id")
    private Long id;

    @NotNull
    private boolean reviewYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_course_id")
    private DateCourse dateCourse;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public DateCourseRestaurant(DateCourse dateCourse, Restaurant restaurant){
        this.reviewYn = false;
        this.dateCourse = dateCourse;
        this.restaurant = restaurant;
        this.review = null;
    }

    public void reviewCreated(Review review){
        this.reviewYn = true;
        this.review = review;
    }
}
