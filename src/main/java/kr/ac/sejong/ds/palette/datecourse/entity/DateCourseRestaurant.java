package kr.ac.sejong.ds.palette.datecourse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateCourseRestaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_course_restaurant_id")
    private Long id;

    @NotNull
    private boolean reviewYn;

    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_course_id")
    private DateCourse dateCourse;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public DateCourseRestaurant(DateCourse dateCourse, Restaurant restaurant){
        this.reviewYn = false;
        this.reviewId = null;
        this.dateCourse = dateCourse;
        this.restaurant = restaurant;
    }
}
