package com.ssafy.healingdiary.domain.diary.domain;

import com.ssafy.healingdiary.domain.tag.domain.Tag;
import com.ssafy.healingdiary.global.common.domain.BaseEntity;

import javax.persistence.*;
import javax.persistence.criteria.Fetch;

@Entity
public class DiaryTag extends BaseEntity {

    @Column(name = "diary_tag_id")
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;


}
