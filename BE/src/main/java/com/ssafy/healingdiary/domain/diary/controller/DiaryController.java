package com.ssafy.healingdiary.domain.diary.controller;


import com.ssafy.healingdiary.domain.diary.dto.CalendarResponse;
import com.ssafy.healingdiary.domain.diary.dto.DiaryCreateRequest;
import com.ssafy.healingdiary.domain.diary.dto.DiaryDetailResponse;
import com.ssafy.healingdiary.domain.diary.dto.DiarySimpleResponse;
import com.ssafy.healingdiary.domain.diary.dto.EmotionStatisticResponse;
import com.ssafy.healingdiary.domain.diary.service.DiaryService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping
    public Slice<DiarySimpleResponse> getDiaryList(
//        Authentication authentication,
        @RequestParam(required = false) Long clubId,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String tag,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer day,
        Pageable pageable
    ){
//        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return diaryService.getDiaryList(clubId, keyword, tag, year, month, day, pageable);
    }

    @GetMapping("/{diaryId}")
    public DiaryDetailResponse getDiaryDetail(
//        Authentication authentication,
        @PathVariable Long diaryId){
//        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return diaryService.getDiaryDetail(diaryId);
    }

    @PostMapping
    public Map<String, Object> createDiary(
//        Authentication authentication,
        @RequestBody DiaryCreateRequest diaryCreateRequest){
//        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return diaryService.createDiary(diaryCreateRequest);
    }

    @DeleteMapping("/{diaryId}")
    public void deleteDiary(
//        Authentication authentication,
        @RequestParam Long memberId,
        @PathVariable Long diaryId){
//        UserDetails principal = (UserDetails) authentication.getPrincipal();
        diaryService.deleteDiary(memberId, diaryId);
    }

    @GetMapping("/calendar")
    public List<CalendarResponse> getCalendar(
//        Authentication authentication,
        @RequestParam Long memberId,
        @RequestParam int year,
        @RequestParam int month){
//        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return diaryService.getCalendar(memberId, year, month);
    }

    @GetMapping("/emotion")
    public List<EmotionStatisticResponse> getEmotionStatistics(
//        Authentication authentication,
        @RequestParam Long memberId,
        @RequestParam int year,
        @RequestParam int month){
//        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return diaryService.getEmotionStatistics(memberId, year, month);
    }
}
