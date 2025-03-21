package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

  // 아무것도 없으면 @RequestMapping의 url이 mapping됨
  @GetMapping
  public List<MemoResponseDto> findAllMemos() {

    // 리스트 초기화
    List<MemoResponseDto> responseList = new ArrayList<>();

    // MashMap<Memo> -> 전체 조회 -> List<MemoResponseDto>
    // 1. 반복문 사용
    for (Memo memo : memoList.values()) {
      MemoResponseDto responseDto = new MemoResponseDto(memo);
      responseList.add(responseDto);
    }

    // 2. 스트림 사용
//    responseList = memoList.values().stream()
//                                    .map(MemoResponseDto::new)
//                                    .toList();

    return responseList;

  }


  @GetMapping("/{id}")
  public ResponseEntity<MemoResponseDto> findMemoById(@PathVariable Long id) {

    Memo memo = memoList.get(id);

    // 해당 식별자를 가진 메모가 없으면 404 NOT FOUND 반환
    if (memo == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);

  }

  @PutMapping("/{id}")
  public ResponseEntity<MemoResponseDto> updateMemoById(
      @PathVariable Long id,
      @RequestBody MemoRequestDto dto
  ) {
    Memo memo = memoList.get(id);

    // 해당 식별자를 가진 메모가 없으면 404 NOT FOUND 반환
    if (memo == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // 제목이나 내용이 없으면 400 BAD REQUEST 반환
    if (dto.getTitle() == null || dto.getContents() == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    memo.update(dto);

    return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);

  }

  // 일부 수정이기 때문에 @PatchMapping 사용
  @PatchMapping("/{id}")
  public ResponseEntity<MemoResponseDto> updateTitle(
      @PathVariable Long id,
      @RequestBody MemoRequestDto dto
  ) {

    Memo memo = memoList.get(id);

    // 해당 식별자를 가진 메모가 없으면 404 NOT FOUND 반환
    if (memo == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // 내용이 있거나, 제목이 null이면 400 BAD REQUEST 반환
    if (dto.getTitle() == null || dto.getContents() != null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    memo.updateTitle(dto);

    return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);

  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMemo(@PathVariable Long id) {

    // memoList의 key가 id를 포함하고 있다면 삭제
    if (memoList.containsKey(id)) {
      memoList.remove(id);

      return new ResponseEntity<>(HttpStatus.OK);
    }

    return new ResponseEntity<>(HttpStatus.NOT_FOUND);

  }

}
