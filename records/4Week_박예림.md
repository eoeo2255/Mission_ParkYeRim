# [4Week] 박예림

## 할일

- [x] 받은 호감 리스트에서 성별 필터링 구현
  - [x] 특정 성별을 가진 사람에게서 받은 호감만 필터링해서 표시
- [x] 네이버 클라우드 플랫폼 배포, 도메인, HTTPS 까지 적용
#### 선택미션
- [x] 받은 호감 리스트에서 호감사유 필터링 구현
  - [x] 2순위 정렬조건은 최신순
- [ ] 받은 호감리스트에서 정렬 기능 구현
  - [x] 최신순이 기본
  - [ ] 날짜순 : 가장 오래 된 호감을 우선으로 표시
  - [ ] 인기 많은 순 : 나에게 호감표시를 한 사람들 중, 받은 호감 표시가 가장 많은 사람 순으로 표시
  - [ ] 인기 적은 순 : 나에게 호감표시를 한 사람들 중, 받은 호감 표시가 가장 적은 사람 순으로 표시
  - [ ] 성별 순 : 여성 > 남성 순으로 표시, 2순위 정렬 조건은 최신 순
  - [ ] 호감사유 순 : 외모 > 성격 > 능력 순으로 표시, 2순위 정렬조건은 최신순
-[ ] 젠킨스를 통한 배포 자동화
  - [ ] 젠킨스를 통해, 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동으로 배포가 진행

## 접근 방법

- 받은 호감 리스트에서 성별 필터링 구현하기
    - showToList 메서드를 통해 gender 쿼리스트링이 null이 아닌지 확인한다. null이 아닐 경우, 필터링을 했다는 뜻이기 때문에 service의 toListGenderFilter 메서드를 실행시킨다
    - 해당 instaMember 를 좋아하는 사람들의 목록 중 넘어온 gender 쿼리스트링과 일치하는 instaMember만  새 List에 넣은 뒤, 해당 List를 반환한다.
---

- 받은 호감 리스트에서 호감사유 필터링 구현하기
    - toListGenderFilter 와 마찬가지로 구현했다.
---
- 최신순으로 정렬하기
  - https://developer-talk.tistory.com/642 참고 자료
  - 이 부분은 구현은 했지만 공부가 필요

---

## 특이사항
**[Issue]**
- showToList 메서드를 구현할 때, 해당 부분에서 막혔었다. 변수 likeablePeople에 재할당 하는 것을 잊고 메서드를 호출만 해놓고 왜 안되지? 고민하고 있었다.. 이런 실수는 하지 말자...!
```java
if (gender != null) {
     likeablePersonService.toListGenderFilter(likeablePeople, gender);
  }
```
- url 에서 'gender=' 의 경우 gender의 값이 '공백'으로 들어가기 때문에 뒤의 if 문에서 attractiveTypeCode 가 제대로 작동하지 않았다. 코드를 아래처럼 수정하자 attractiveTypeCode 필터링 또한 제대로 동작했다.
```java
if (gender != null && gender.length() > 0) {
      likeablePeople = likeablePersonService.toListGenderFilter(likeablePeople, gender);
    }
```