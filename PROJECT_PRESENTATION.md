# 건양대학교 게시판 프로젝트 발표 정리

## 1. 프로젝트 한 줄 소개

이 프로젝트는 Spring Boot로 만든 건양대학교 게시판 웹 애플리케이션입니다. 사용자는 회원가입과 로그인을 할 수 있고, 게시판 카테고리별로 글을 조회, 작성, 상세보기, 수정, 삭제할 수 있습니다.

핵심 목표는 학교 커뮤니티처럼 여러 게시판을 나누고, 로그인한 사용자가 본인이 작성한 글만 관리할 수 있게 만드는 것입니다.

## 2. 사용 기술과 라이브러리

### 백엔드

- Java 21
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Spring Security

### 프론트엔드

- Thymeleaf
- HTML
- CSS
- Thymeleaf Spring Security Extras

### 데이터베이스

- MySQL
- JPA/Hibernate

### 개발 편의 라이브러리

- Lombok
- Maven

## 3. 전체 폴더 구조

```text
konyang1
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── kr.pe.jm.konyang1
│   │   │       ├── Konyang1Application.java
│   │   │       ├── config
│   │   │       │   └── SecurityConfig.java
│   │   │       ├── controller
│   │   │       │   ├── ArticleController.java
│   │   │       │   ├── MemberController.java
│   │   │       │   └── ViewController.java
│   │   │       ├── dto
│   │   │       │   └── ArticleForm.java
│   │   │       ├── entity
│   │   │       │   ├── Article.java
│   │   │       │   └── Member.java
│   │   │       ├── repository
│   │   │       │   ├── ArticleRepository.java
│   │   │       │   └── MemberRepository.java
│   │   │       └── service
│   │   │           └── MemberService.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── templates
│   │           ├── home.html
│   │           ├── login.html
│   │           ├── register.html
│   │           ├── board.html
│   │           ├── write.html
│   │           ├── article.html
│   │           └── edit.html
│   └── test
└── PROJECT_PRESENTATION.md
```

## 4. 계층별 역할

### Controller

사용자의 HTTP 요청을 받아서 어떤 화면을 보여줄지, 어떤 데이터를 저장하거나 삭제할지 결정합니다.

- `ViewController`: 홈, 로그인, 회원가입 화면을 보여줍니다.
- `MemberController`: 회원가입 요청을 처리합니다.
- `ArticleController`: 게시글 목록, 작성, 상세보기, 수정, 삭제를 처리합니다.

### Service

비즈니스 로직을 담당합니다.

- `MemberService`: 회원가입 처리, 비밀번호 암호화, Spring Security 로그인 사용자 조회를 담당합니다.

### Repository

데이터베이스와 직접 연결되는 계층입니다.

- `MemberRepository`: 회원 정보 조회, 아이디 중복 확인을 담당합니다.
- `ArticleRepository`: 게시글 저장, 조회, 삭제와 카테고리별 게시글 조회를 담당합니다.

### Entity

데이터베이스 테이블과 연결되는 Java 클래스입니다.

- `Member`: 회원 테이블과 연결됩니다.
- `Article`: 게시글 테이블과 연결됩니다.

### DTO

화면에서 넘어온 입력값을 컨트롤러와 엔티티 사이에서 전달합니다.

- `ArticleForm`: 글 작성/수정 폼의 `category`, `title`, `content` 값을 받습니다.

## 5. 주요 Entity 설명

### Member

`Member`는 회원 정보를 저장하는 엔티티입니다.

```text
id       : 회원 고유 번호
username : 로그인 아이디
password : 암호화된 비밀번호
nickname : 게시글 작성자로 표시되는 닉네임
```

특징은 `username`에 `unique = true`가 설정되어 있어 같은 아이디로 중복 가입할 수 없다는 점입니다.

### Article

`Article`은 게시글 정보를 저장하는 엔티티입니다.

```text
id        : 게시글 고유 번호
category  : 게시판 카테고리
title     : 제목
content   : 내용
author    : 작성자 닉네임
createdAt : 작성일
```

`@PrePersist`를 사용해서 게시글이 처음 저장되기 직전에 `createdAt`에 현재 시간이 자동으로 들어갑니다.

또한 `update()` 메서드를 통해 수정 화면에서 넘어온 카테고리, 제목, 내용을 기존 게시글에 반영합니다.

## 6. 핵심 기능

### 6.1 회원가입

사용자는 `register.html`에서 아이디, 닉네임, 비밀번호, 비밀번호 확인을 입력합니다.

동작 흐름은 다음과 같습니다.

```text
register.html
→ POST /register
→ MemberController
→ MemberService.register()
→ MemberRepository.save()
→ 회원가입 성공 후 /login?registered 로 이동
```

핵심 처리 내용:

- 비밀번호와 비밀번호 확인이 같은지 검사합니다.
- 이미 존재하는 아이디인지 검사합니다.
- 비밀번호는 `BCryptPasswordEncoder`로 암호화해서 저장합니다.

### 6.2 로그인과 로그아웃

로그인은 Spring Security가 처리합니다.

동작 흐름은 다음과 같습니다.

```text
login.html
→ POST /login
→ Spring Security
→ MemberService.loadUserByUsername()
→ 로그인 성공 시 / 로 이동
```

`MemberService`는 `UserDetailsService`를 구현하고 있어서, Spring Security가 로그인할 때 DB에서 회원 정보를 찾는 역할을 합니다.

로그아웃은 `POST /logout`으로 처리되고, 성공하면 홈 화면으로 이동합니다.

### 6.3 홈 화면

홈 화면은 `home.html`입니다.

주요 기능:

- 로그인하지 않은 사용자는 로그인, 회원가입 버튼을 봅니다.
- 로그인한 사용자는 닉네임과 로그아웃 버튼을 봅니다.
- 취업, 자유게시판, 건의사항 게시판으로 이동할 수 있습니다.

Thymeleaf Security Extras의 `sec:authorize`를 사용해서 로그인 여부에 따라 다른 화면을 보여줍니다.

### 6.4 카테고리별 게시판 목록

게시판 목록은 `board.html`에서 보여줍니다.

동작 흐름은 다음과 같습니다.

```text
/board/{category}
→ ArticleController.boardList()
→ ArticleRepository.findByCategory(category)
→ board.html
```

예시:

```text
/board/취업
/board/자유게시판
/board/건의사항
```

`findByCategory()`는 Spring Data JPA의 메서드 이름 규칙을 이용한 기능입니다. 메서드 이름만으로 `category` 값이 같은 게시글 목록을 조회합니다.

### 6.5 글쓰기

글쓰기 화면은 `write.html`입니다.

동작 흐름은 다음과 같습니다.

```text
/write?category=자유게시판
→ ArticleController.writeForm()
→ write.html
→ POST /articles/create
→ ArticleController.createArticle()
→ ArticleRepository.save()
→ 작성한 카테고리 게시판으로 이동
```

핵심 처리 내용:

- 로그인한 사용자만 글을 작성할 수 있습니다.
- 현재 로그인한 사용자의 닉네임을 찾아서 작성자로 저장합니다.
- 글 작성 후에는 해당 카테고리 게시판으로 돌아갑니다.
- CSRF 토큰을 함께 보내서 Spring Security의 보안 검사를 통과합니다.

### 6.6 게시글 상세보기

게시글 상세 화면은 `article.html`입니다.

동작 흐름은 다음과 같습니다.

```text
/articles/{id}
→ ArticleController.articleDetail()
→ ArticleRepository.findById(id)
→ article.html
```

상세 화면에서는 제목, 작성자, 작성일, 내용을 보여줍니다.

또한 현재 로그인한 사용자가 작성자인지 확인해서, 본인 글일 때만 수정하기와 삭제하기 버튼을 보여줍니다.

### 6.7 게시글 수정

게시글 수정 화면은 `edit.html`입니다.

동작 흐름은 다음과 같습니다.

```text
/articles/{id}/edit
→ 작성자 본인인지 검사
→ edit.html
→ POST /articles/{id}/update
→ 작성자 본인인지 다시 검사
→ Article.update()
→ ArticleRepository.save()
→ 상세 페이지로 이동
```

중요한 점은 화면에서 버튼을 숨기는 것만으로 끝내지 않고, 컨트롤러에서도 다시 작성자 검사를 한다는 것입니다. 이렇게 해야 다른 사용자가 주소를 직접 입력해도 남의 글을 수정할 수 없습니다.

### 6.8 게시글 삭제

삭제는 `article.html`의 삭제 버튼에서 `POST /articles/{id}/delete`로 요청합니다.

동작 흐름은 다음과 같습니다.

```text
POST /articles/{id}/delete
→ ArticleController.deleteArticle()
→ 작성자 본인인지 검사
→ ArticleRepository.delete()
→ 해당 게시판 목록으로 이동
```

삭제 버튼에는 `confirm()`을 사용해서 사용자가 실수로 삭제하지 않도록 한 번 더 확인합니다.

## 7. 보안 구조

보안 설정은 `SecurityConfig.java`에서 담당합니다.

### 접근 권한

```text
로그인 필요:
- /write
- /articles/create
- /articles/*/edit
- /articles/*/update
- /articles/*/delete

누구나 접근 가능:
- /
- /login
- /register
- /board/**
- GET /articles/*
```

즉, 글 목록과 상세보기는 누구나 가능하지만 글 작성, 수정, 삭제는 로그인한 사용자만 가능합니다.

### 작성자 권한 검사

수정과 삭제는 로그인만 되어 있다고 되는 것이 아니라, 작성자 본인인지 추가로 확인합니다.

검사 방식:

```text
현재 로그인한 username
→ MemberRepository.findByUsername()
→ 회원 nickname 조회
→ Article.author와 비교
→ 같으면 본인 글로 판단
```

본인이 아닌 경우 `403 Forbidden` 응답을 발생시킵니다.

### CSRF 보호

회원가입, 로그인, 로그아웃, 글쓰기, 수정, 삭제처럼 데이터를 변경하는 POST 요청에는 CSRF 토큰을 포함합니다.

```html
<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">
```

이 토큰은 Spring Security가 정상적인 화면에서 보낸 요청인지 확인하는 데 사용합니다.

## 8. 화면별 역할

| 화면 파일 | 역할 |
|---|---|
| `home.html` | 메인 화면, 로그인 상태 표시, 게시판 카테고리 이동 |
| `login.html` | 로그인 화면 |
| `register.html` | 회원가입 화면 |
| `board.html` | 카테고리별 게시글 목록 화면 |
| `write.html` | 새 글 작성 화면 |
| `article.html` | 게시글 상세보기 화면 |
| `edit.html` | 게시글 수정 화면 |

## 9. 주요 URL 정리

| URL | Method | 기능 |
|---|---|---|
| `/` | GET | 홈 화면 |
| `/login` | GET | 로그인 화면 |
| `/login` | POST | 로그인 처리 |
| `/logout` | POST | 로그아웃 처리 |
| `/register` | GET | 회원가입 화면 |
| `/register` | POST | 회원가입 처리 |
| `/board/{category}` | GET | 카테고리별 게시글 목록 |
| `/write` | GET | 글쓰기 화면 |
| `/articles/create` | POST | 게시글 등록 |
| `/articles/{id}` | GET | 게시글 상세보기 |
| `/articles/{id}/edit` | GET | 게시글 수정 화면 |
| `/articles/{id}/update` | POST | 게시글 수정 처리 |
| `/articles/{id}/delete` | POST | 게시글 삭제 처리 |

## 10. 데이터 흐름 예시

### 글 작성 흐름

```text
1. 사용자가 자유게시판에서 글쓰기 버튼 클릭
2. /write?category=자유게시판 으로 이동
3. write.html에서 자유게시판이 기본 선택됨
4. 제목과 내용을 입력하고 등록
5. ArticleForm이 category, title, content를 받음
6. ArticleController가 로그인 사용자의 닉네임을 조회
7. Article 엔티티를 만들어 DB에 저장
8. /board/자유게시판 으로 리다이렉트
```

### 글 수정 흐름

```text
1. 사용자가 본인 글 상세 페이지 접속
2. 컨트롤러가 현재 사용자와 작성자를 비교
3. 본인 글이면 수정하기 버튼 표시
4. 수정 화면에서 제목/내용/카테고리 변경
5. 수정 요청 시 서버에서 작성자 여부를 다시 검사
6. Article.update()로 기존 게시글 정보 변경
7. 저장 후 상세 페이지로 이동
```

### 글 삭제 흐름

```text
1. 사용자가 본인 글 상세 페이지 접속
2. 삭제하기 버튼 클릭
3. 브라우저 confirm 창에서 삭제 여부 확인
4. 서버에서 작성자 본인인지 검사
5. DB에서 게시글 삭제
6. 삭제된 글이 있던 게시판 목록으로 이동
```

## 11. 발표할 때 강조하면 좋은 포인트

### MVC 패턴을 사용했다

이 프로젝트는 Spring MVC 구조를 따릅니다.

```text
View(HTML)
→ Controller
→ Service
→ Repository
→ Database
```

이 구조 덕분에 화면, 요청 처리, 비즈니스 로직, 데이터 접근 역할이 분리되어 있습니다.

### Spring Security를 사용했다

로그인/로그아웃을 직접 모두 구현하지 않고 Spring Security를 활용했습니다. 특히 비밀번호 암호화, 로그인 인증, 접근 권한 제한, CSRF 보호를 적용했습니다.

### JPA를 사용해 DB 코드를 줄였다

SQL을 직접 많이 작성하지 않고 `JpaRepository`를 상속해서 기본 CRUD 기능을 사용했습니다. `findByCategory()`처럼 메서드 이름만으로 카테고리별 조회도 구현했습니다.

### 본인 글만 수정/삭제할 수 있게 했다

화면에서 버튼을 숨기는 것뿐만 아니라 컨트롤러에서 작성자를 다시 검사합니다. 이 점이 실제 보안에서 중요합니다.

### Thymeleaf로 서버 데이터를 화면에 출력했다

`th:text`, `th:href`, `th:if`, `th:each` 등을 사용해서 서버에서 넘긴 게시글 목록, 로그인 상태, 작성자 여부를 화면에 동적으로 보여줍니다.

## 12. 발표용 짧은 설명문

이 프로젝트는 Spring Boot 기반의 건양대학교 게시판 웹 애플리케이션입니다. 사용자는 회원가입과 로그인을 할 수 있고, 취업, 자유게시판, 건의사항 게시판으로 나누어 글을 작성하고 조회할 수 있습니다.

백엔드는 Spring MVC 구조로 만들었고, Controller가 요청을 받고 Repository가 JPA를 통해 데이터베이스와 통신합니다. 회원 정보와 게시글 정보는 각각 `Member`, `Article` 엔티티로 관리합니다.

로그인 기능은 Spring Security를 사용했습니다. 비밀번호는 BCrypt로 암호화해서 저장하고, 글 작성, 수정, 삭제는 로그인한 사용자만 가능하도록 설정했습니다.

게시글 수정과 삭제 기능에서는 현재 로그인한 사용자의 닉네임과 게시글 작성자 닉네임을 비교합니다. 그래서 본인이 작성한 글일 때만 수정과 삭제가 가능하고, 다른 사용자의 글은 수정하거나 삭제할 수 없습니다.

화면은 Thymeleaf 템플릿으로 구성했습니다. 게시판 목록에서는 카테고리별 글을 보여주고, 상세 화면에서는 작성자 본인에게만 수정/삭제 버튼을 보여줍니다.

## 13. 개선하면 좋은 점

- 게시글 작성자 비교를 닉네임 대신 회원 id로 연결하면 더 안정적입니다.
- 게시글 검색 기능을 추가할 수 있습니다.
- 댓글 기능을 추가할 수 있습니다.
- 게시글 목록에 페이징 기능을 추가하면 글이 많아져도 보기 편합니다.
- 관리자 권한을 추가해서 부적절한 글을 관리할 수 있습니다.
- CSS를 공통 파일로 분리하면 화면 스타일 관리가 쉬워집니다.

