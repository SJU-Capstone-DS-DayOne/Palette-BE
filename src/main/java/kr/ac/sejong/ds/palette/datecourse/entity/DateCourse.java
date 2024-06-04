package kr.ac.sejong.ds.palette.datecourse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.sejong.ds.palette.common.entity.BaseEntity;
import kr.ac.sejong.ds.palette.couple.entity.Couple;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateCourse extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_course_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couple_id")
    private Couple couple;

    @OneToMany(mappedBy = "dateCourse", cascade = CascadeType.ALL)
    private List<DateCourseRestaurant> dateCourseRestaurants = new ArrayList<>();

    public DateCourse(Couple couple) {
        this.couple = couple;
    }

    public void setDateCourseRestaurants(DateCourseRestaurant rst, DateCourseRestaurant cafe, DateCourseRestaurant bar){
        this.dateCourseRestaurants.add(rst);
        this.dateCourseRestaurants.add(cafe);
        this.dateCourseRestaurants.add(bar);
    }
}
