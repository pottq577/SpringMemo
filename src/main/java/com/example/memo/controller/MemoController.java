package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memos")
public class MemoController {

  private final Map<Long, Memo> memoList = new HashMap<>();

  @PostMapping
  // 상태 코드를 같이 반환하기 위해 ResponseEntity<> 사용
  public ResponseEntity<MemoResponseDto> createMemo(@RequestBody MemoRequestDto dto) {

    Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;
    Memo memo = new Memo(memoId, dto.getTitle(), dto.getContents());

    memoList.put(memoId, memo);

    return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.CREATED);

  }

  @GetMapping("/{id}")
  public MemoResponseDto findMemoById(@PathVariable Long id) {

    Memo memo = memoList.get(id);
    return new MemoResponseDto((memo));

  }

  @PutMapping("/{id}")
  public MemoResponseDto updateMemoById(
      @PathVariable Long id,
      @RequestBody MemoRequestDto dto
  ) {
    Memo memo = memoList.get(id);

    memo.update(dto);

    return new MemoResponseDto(memo);

  }

  @DeleteMapping("/{id}")
  public void deleteMemo(@PathVariable Long id) {
    memoList.remove(id);
  }

}
