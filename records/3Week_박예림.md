# [3Week] 박예림

## 할일

- [x] 호감에 대한 수정/삭제 쿨타임
    - [x] 호감 표시 , 호감 수정 시에 modifyUnlockTime 갱신 (쿨타임이 끝나는 시간)
    - [x] 호감 사유 변경시 likeableCoolTime 갱신
    - [x] UI에서 쿨타임 안차면 수정/삭제 못 하도록
    - [x] LikeablePersonService::canDelete, LikeablePersonService::canLike 에 쿨타임 체크 추가
- [x] 페이스북 로그인 구현
  - [x] 테스트유저에 한해서 인스타 연결
- [x] ip주소로 접속 가능한 배포
    -[x] 네이버 클라우드 서버에 도커, DB, git, JDK ..etc 설치
    -[x] 서버에서 프로젝트 빌드
    -[x] 이미지 생성
    -[x] 도메인 관리 권한 위임 후, A 레코드 설정
- [ ]


## 접근 방법

**[Issue]**

- 쿨타임 구현
  - AppConfig 파일에 3시간이 지나 수정과 삭제가 가능해지는 시간을 저장하는 필드를 생성한 뒤, setLikeablePersonModifyCoolTime 메서드로 쿨타임을 3시간 뒤로 설정하고 genLikeablePersonUnlockCoolTime 메서드로 현재 시간에서 쿨타임이 끝나는 시간을 계산해 반환한다.
  - LikeablePersonService 에서 호감표시를 할 경우 genLikeablePersonUnlockCoolTime 메서드로 쿨타임이 끝나는 시간을 modifyUnlockDate 필드에 저장한다.
  - isModifyUnlocked 메서드로 현재 시간이 쿨타임이 지난 시간인지를 확인한다. 지났다면 True 반환, True일 경우 list.html에서 버튼을 활성화 시켜준다.
  - getModifyUnlockTimeRemain 메서드를 통해 modifyUnlockTime 의 시와 분을 가져와 쿨타임이 끝나는 시간을 UI에 띄운다.
---

- 배포
  - 도커, DB, git, JDK를 설치 후 프로젝트를 clone 해온다. 이때 반드시 리포지터리 주소 뒤에 '.' 을 붙여야 한다. src/main/resources 에서 application-secret.yml 파일을 생성해서 클라이언트 아이디와 시크릿을 추가해준다. 붙여넣을 때는 a 를 누른 뒤 insert 상태에서 해야한다. 바로 붙여넣을 경우 앞글자가 지워진 상태로 붙여넣어진다.
  - gradlew 을 소유자가 실행 가능하게끔 모드를 변경해준 뒤 빌드를 한다. 여기서 테스트에러가 발생해 인텔리제이를 확인해봤더니 테스트케이스에서 에러가 떴다. 코드를 재정비한 뒤 다시 깃헙에 푸시 후, 클론했더니 해결되었다.
  - 빌드 후 생성된 jar 파일을 실행시켜보니 다시 에러가 발생했다. 에러코드를 확인해보니 DB쪽에서 문제가 생긴 것 같은데 원인을 찾을 수가 없었다. 강사님의 리포지터리를 확인해본 결과 application-prod.yml 파일에서 db의 username과 password가 이전에 사용하던 아이디와 비번인 것을 확인했다. DB 설치 후 생성했던 계정인 lldjlocal로 접속하니 에러없이 연결됐다.

---

## 특이사항

- 배포 도중 장소 이동으로 IP 주소가 바뀌었다. 다시 서버에 접속하려니 에러가 뜨며 접속이 불가능했는데 바뀐 IP 주소로 서버에 접속하려고 해서 발생한거였다. 멘토님께서 IP가 바뀌었으니 다시 설정해줘야된다고 하셔서 찾아보았다. 처음엔 포트포워딩부분을 수정해줘야되는 줄 알았는데 생각해보니까 ACG를 생성할 때 myIP 포트 설정을 해줬던 게 생각나서 ACG에 현재 IP와 22번 포트를 추가했더니 접속이 가능해졌다.
