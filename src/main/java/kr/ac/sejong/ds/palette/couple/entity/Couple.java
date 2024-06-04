package kr.ac.sejong.ds.palette.couple.entity;

import jakarta.persistence.*;
import kr.ac.sejong.ds.palette.common.entity.BaseEntity;
import kr.ac.sejong.ds.palette.datecourse.entity.DateCourse;
import kr.ac.sejong.ds.palette.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Couple extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "couple_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "male_id")
    private Member male;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "female_id")
    private Member female;

    @OneToMany(mappedBy = "couple", cascade = CascadeType.REMOVE)
    private List<DateCourse> dateCourse;

    public Couple(Member male, Member female) {
        this.male = male;
        this.female = female;
    }
}
