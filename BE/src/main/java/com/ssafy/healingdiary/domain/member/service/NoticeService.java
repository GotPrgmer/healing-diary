package com.ssafy.healingdiary.domain.member.service;

import com.ssafy.healingdiary.domain.member.domain.CheckStatus;
import com.ssafy.healingdiary.domain.member.domain.DeleteStatus;
import com.ssafy.healingdiary.domain.member.domain.Member;
import com.ssafy.healingdiary.domain.member.domain.Notice;
import com.ssafy.healingdiary.domain.member.dto.DeleteNoticeId;
import com.ssafy.healingdiary.domain.member.dto.NoticeListResponse;
import com.ssafy.healingdiary.domain.member.repository.MemberRepository;
import com.ssafy.healingdiary.domain.member.repository.NoticeRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;
    public List<NoticeListResponse> searchNoticeAll() {
        Member member = memberRepository.findById(1L).get();
        List<NoticeListResponse> list = noticeRepository.findByMemberAndDeleteStatus(member,
                DeleteStatus.UNDELETED)
            .stream().map(NoticeListResponse::of)
            .collect(Collectors.toList());
        return list;
    }

    public void changeNoticeStatus(Long noticeId) {
        Notice notice = noticeRepository.getReferenceById(noticeId);
        notice.changeCheckStatus(CheckStatus.CHECKED);
        noticeRepository.save(notice);
    }

    public DeleteNoticeId deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.getReferenceById(noticeId);
        notice.changeDeleteStatus(DeleteStatus.DELETED);
        noticeRepository.save(notice);
        return DeleteNoticeId.builder().noticeId(noticeId).build();
    }
}
