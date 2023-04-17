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
