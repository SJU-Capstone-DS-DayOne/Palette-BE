package kr.ac.sejong.ds.palette.datecourse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.sejong.ds.palette.common.entity.BaseEntity;
import kr.ac.sejong.ds.palette.couple.entity.Couple;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public DateCourse(Couple couple) {
        this.couple = couple;
    }
}
