# [2Week] 박예림

## 할일

- [x] 한 명의 인스타 회원에게 중복 호감표시 불가능
    - [x] 이미 호감표시를 한 유저에게는 다시 호감표시 불가능 (rq.historyBack)
    - [x] 다른 사유로 호감표시를 등록할 경우에는 기존 내용에서 수정되어야 함 (resultCode=S-2)
- [x] 호감상대는 11명 이상 등록 할 수 없음
    - [x] 11명 째 호감 버튼을 눌렀을 경우 (rq.historyBack)
    - [ ] 하드코딩이 아닌, yml에서 10명 제한 걸기
-  [x] 네이버 로그인
    - [x] 네이버 로그인으로 가입한 회원의 providerTypeCode : NAVER
    - [x] NAVER__{"id": 2731659195, "gender": "M", "name": "홍길동"}의 형태로 나오면 안됨

---

## 접근 방법

**[Issue]**

- 로그인한 유저의 likeablePersonList 의 size 가 10일 경우, 저장이 되지 않게끔 구현하는 과정에서 if문 안의 size 범위를 'getMyLikeableList().size() > 10' 로
  입력했다. 해당 코드에서 size 는 '11'로 호감표시가 이미 10개를 넘어섰다. if문 안에서 범위를 설정 할 때, 종종 있는 실수였다. 더 꼼꼼하게 확인해야겠다.
- 같은 인스타회원에게 다른 호감표시를 할 경우 likeablePerson 에서 attractiveTypeCode 의 값을 수정해야되기 때문에 likeablePerson 파일에 @Setter 를 붙였다.
- for each 문을 통해서 로그인한 유저의 likeablePersonList 를 하나씩 비교한다.
- 이중 if 문을 통해서 입력 받은 toInstaMember 와 attractiveTypeCode 가 둘 다 같을 경우, 이미 등록된 호감표시로 처리했다. 만약 toInstaMember 만 같을 경우, 해당
  likeablePerson의 attractiveTypeCode 를 입력 받은 값으로 수정한다.

---

- 네이버 로그인을 구현하는 과정에서 네이버는 'client-authentication-method: POST' 가 필요 없다는 걸 늦게 확인했다. chatGPT가 써 준 코드를 참고 했는데 다른 사람들이 구현한
  코드 자료와 비교해 확인 할 생각을 안해서 그랬다. 자료의 정확성에 조금 더 집착할 필요가 있어 보인다..
- 네이버의 응답값은 json 으로 되어있다. 따라서 id값만 가져오기 위해선 map을 사용하는 게 편리하다. "response" 안의 "id"의 value를 가져와야 했기 때문에, oauthId를 변수 하나로
  한번에 가져오는 게 아니라 map을 선언해서 값을 두번에 나눠 가져왔다.
- oauthId 는 String이기 때문에 가져온 'Object id' 를 (String) 로 강제 형변환해주었다. toString()는 'id' 가 만에 하나 다른 숫자, 리스트 등을 가지고 있을 경우 확인이
  어렵기 때문이다.

---

## 특이사항

**[Refactoring]**

    [x] 데이지UI로 프론트 수정
    [ ] 인스타 계정을 한 번 연결 하면, 인스타 ID 등록을 또 클릭 했을 때 인스타 ID 수정으로 바꾸기
    [ ] 