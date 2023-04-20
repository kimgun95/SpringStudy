# SpringStudy
---
## spring lv.1

1. 서비스 요구사항
   - 전체 게시글 목록 조회 API
     - title, content, author, created_at 조회
     - created_at 기준 내림차순 정렬
   - 선택한 게시글 조회 API
   - 게시글 작성 API
     - title, content, author, password 작성 후 저장
     - 저장한 게시글 client로 반환
   - 선택한 게시글 수정 API
     - 수정할 데이터와 password를 요청으로 보내면 서버에서 password 일치 여부 확인
     - 수정된 게시글 client로 반환
   - 선택한 게시글 삭제 API
     - password를 요청으로 보내면 서버에서 일치 여부 확인
     - 게시글 삭제 후 성공 여부 응답
2. 고민한 부분
    - 수정, 삭제 API request 방식(param, query, body)
      - body를 통해 데이터를 보내기 위해 `@RequestBody`를 이용했다.
      - `@PathVariable`를 활용해 게시글의 id 값을 넘겨주었다.
      - param, query 방식이 둘 다 같은 것으로 알고 있는데 해당 방식은 url에 값이 노출되기에 안전하지 않다고 생각했다.
    - 어떤 상황에서 어떤 방식의 request를 써야 하는지
      - `@RequestParam` : 페이징 및 검색 정보를 전달할 때 사용
      - `@RequestBody` : xml, json 기반 데이터 요청시 사용
      - `@PathVariable` : REST API에서 값 호출시 사용
    - RESTful한 API 설계를 했는지
      - 완벽하진 않겠지만 어느정도 REST API에 만족하는 설계를 한 것 같음
      - URI에 대한 가이드(혹은 규칙)를 잘 따르며 설계를 했다.
      - [REST Resource Naming Guide](https://restfulapi.net/resource-naming/)
      - [7 Rules for REST API URI Design](https://dzone.com/articles/7-rules-for-rest-api-uri-design-1)
    - 적절한 관심사 분리를 했는지(controller, service, repository)
      - MVC 패턴을 떠올리며 controller와 model 간의 역할을 구분지었다.

---
## spring lv.2
1. 서비스 요구사항
   - 회원 가입 API
     - username, password 를 전달 받아 회원 등록
     - username : 4~10자, 알파벳 소문자 및 숫자 조합
     - password : 8~15자, 알파벳 대소문자 및 숫자
     - DB 레코드에 중복된 username은 존재할 수 없다
   - 로그인 API
     - username, password 를 전달 받아 로그인
     - 로그인 검증이 성공적으로 끝나면 JWT 토큰을 발급
2. 고민한 부분
   - ERD를 설계한 후 Entity를 개발했을 때 장점
     - 도메인 간의 연관관계에 대해 생각한 후 쉽게 코드로 적용할 수 있었음
     - pk, fk에 대해 생각하게 됨
   - JWT를 사용한 인증/인가 구현의 장점
     - 서버의 부하를 줄일 수 있음
     - 유저가 악의적으로 수정하여 접근 시도시 서버의 비밀키로 유효성 검증을 하면 됨
   - 반대로 JWT를 사용한 인증/인가의 한계점
     - 동일 유저가 여러 기기로 해당 서버에 접근하는 경우 막을 수 없음
     - 세션처럼 유저의 모든 정보를 서버가 관리하지 않음
   - 만약 댓글 기능이 있는 블로그에서 게시글을 삭제한다면 어떤 문제가 발생할까? DB 관점에서의 해결방법?
     - 게시글의 댓글도 모두 삭제되어 문제가 발생할 수도 있음
     - cascading을 통해 연관된 댓글도 자동적으로 관리되게 할 수 있음
   - IoC와 DI에 대한 간략한 설명
     - DI : 의존하고 있는 어떠한 객체를 외부에서 선언하고 이를 주입 받아 사용하는 것
     - IoC : 프로그램의 흐름을 개발자가 아닌 프레임워크가 제한하는 것