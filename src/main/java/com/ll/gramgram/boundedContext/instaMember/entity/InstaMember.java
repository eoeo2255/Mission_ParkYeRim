package com.ll.gramgram.boundedContext.instaMember.entity;

import com.ll.gramgram.base.BaseEntity;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
public class InstaMember extends BaseEntity {
    @Column(unique = true)
    private String username;
    @Setter
    private String gender;

    @OneToMany(mappedBy = "fromInstaMember", cascade = {CascadeType.ALL})
    @OrderBy("id desc") // 정렬
    @Builder.Default
    private List<LikeablePerson> myLikeableList = new ArrayList<>();


    @OneToMany(mappedBy = "toInstaMember", cascade = {CascadeType.ALL})
    @OrderBy("id desc") // 정렬
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default // @Builder 가 있으면 ` = new ArrayList<>();` 가 작동하지 않는다. 그래서 이걸 붙여야 한다.
    private List<LikeablePerson> whoLikesMe = new ArrayList<>();

    public void addMyLikeableList(LikeablePerson likeablePerson) {
        myLikeableList.add(0, likeablePerson);
    }

    public void addWhoLikesMe(LikeablePerson likeablePerson) {
        whoLikesMe.add(0, likeablePerson);
    }
}
