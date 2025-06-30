package kr.ac.sejong.ds.palette.review.entity;

import jakarta.persistence.*;
import kr.ac.sejong.ds.palette.common.entity.BaseEntity;
import kr.ac.sejong.ds.palette.member.entity.Member;
import kr.ac.sejong.ds.palette.restaurant.entity.Restaurant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public Review(String content, Member member, Restaurant restaurant) {
        this.content = content;
        this.member = member;
        this.restaurant = restaurant;
    }

    public void updateReview(String content){
        this.content = content;
    }
}