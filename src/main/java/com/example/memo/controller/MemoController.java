package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 데이터를 항상 JSON 형태로 통신하기 위해 @RestController 사용
@RestController
// prefix
@RequestMapping("/memos")
public class MemoController {

  // DB를 사용하지 않기 때문에 Map 자료구조로 DB 대체 활용
  private final Map<Long, Memo> memoList = new HashMap<>();

  // 클라이언트로부터 JSON 데이터를 요청받았을 때 파라미터로 바로 바인딩하기 위해 @RequestBody 사용
  // 생성이기 때문에 @PostMapping
  @PostMapping
  public MemoResponseDto createMemo(@RequestBody MemoRequestDto dto) {

    // 식별자(id)가 1씩 증가하도록 만듦
    Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;

    // 요청받은 데이터로 Memo 객체 생성
    Memo memo = new Memo(memoId, dto.getTitle(), dto.getContents());

    // Inmemory에 Memo 저장
    // Inmemory: 실제 DB가 아닌 Map 자료구조를 사용함
    // 그렇기 때문에 없어지는 데이터라는 의미
    memoList.put(memoId, memo);

    return new MemoResponseDto(memo);

  }

  // 조회이기 때문에 @GetMapping 사용
  // @RequestMapping으로 이미 "/memos" prefix가 정해져있기 때문에 식별자만 지정
  @GetMapping("/{id}")
  // 식별자를 파라미터로 바인딩할 때 @PathVariable 사용
  public MemoResponseDto findMemoById(@PathVariable Long id){

    Memo memo = memoList.get(id);
    return new MemoResponseDto((memo));

//    return new MemoResponseDto(memoList.get(id));

  }

}
