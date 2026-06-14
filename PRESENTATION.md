# 건양대학교 게시판 프로젝트 발표자료

## 1. 프로젝트 소개

이 프로젝트는 Spring Boot 기반의 건양대학교 게시판 웹 애플리케이션입니다.

사용자는 회원가입과 로그인을 할 수 있고, 게시판 카테고리별로 글을 조회하거나 작성할 수 있습니다. 또한 자신이 작성한 글에 대해서만 수정과 삭제가 가능하도록 구현했습니다.

발표 핵심 문장:

> 이 프로젝트는 Spring Boot MVC 구조를 바탕으로 회원 인증, 게시글 CRUD, 카테고리별 게시판 조회 기능을 구현한 웹 게시판입니다.

## 2. 주요 기능

### 2.1 회원 기능

- 회원가입
- 로그인
- 로그아웃
- 비밀번호 암호화 저장
- 로그인한 사용자의 닉네임 표시

관련 파일:

- `MemberController.java`
- `MemberService.java`
- `Member.java`
- `MemberRepository.java`
- `login.html`
- `register.html`
- `SecurityConfig.java`

설명 포인트:

> 회원가입 시 아이디, 비밀번호, 비밀번호 확인, 닉네임을 입력합니다. 비밀번호와 비밀번호 확인 값이 다르면 다시 회원가입 화면으로 이동하고, 이미 사용 중인 아이디도 가입할 수 없도록 검사합니다. 비밀번호는 그대로 저장하지 않고 `BCryptPasswordEncoder`로 암호화해서 저장합니다.

### 2.2 게시판 기능

- 카테고리별 게시글 목록 조회
- 게시글 상세 조회
- 게시글 작성
- 게시글 수정
- 게시글 삭제
- 작성자 본인만 수정/삭제 가능

관련 파일:

- `ArticleController.java`
- `Article.java`
- `ArticleForm.java`
- `ArticleRepository.java`
- `board.html`
- `article.html`
- `write.html`
- `edit.html`

설명 포인트:

> 게시글은 카테고리, 제목, 내용, 작성자, 작성일 정보를 가집니다. 게시판 목록에서는 특정 카테고리에 해당하는 글만 조회하고, 상세 화면에서는 글의 제목, 작성자, 작성일, 내용을 확인할 수 있습니다. 수정과 삭제 버튼은 로그인한 사용자가 글 작성자인 경우에만 표시됩니다.

## 3. 사용 기술 및 라이브러리

### 3.1 Backend

- Java 21
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Spring Security

### 3.2 Frontend

- Thymeleaf
- HTML
- CSS
- Thymeleaf Spring Security Extras

### 3.3 Database

- MySQL
- Hibernate JPA

### 3.4 Build Tool

- Maven

### 3.5 개발 편의 라이브러리

- Lombok

설명 포인트:

> Spring Web MVC는 사용자의 요청을 컨트롤러로 연결하는 역할을 합니다. Spring Data JPA는 데이터베이스 접근 코드를 줄여주고, Spring Security는 로그인과 접근 권한 관리를 담당합니다. Thymeleaf는 서버에서 전달한 데이터를 HTML 화면에 출력하는 템플릿 엔진입니다.

## 4. 전체 프로젝트 구조

```text
konyang1
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── kr/pe/jm/konyang1
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
│   │           ├── article.html
│   │           ├── write.html
│   │           └── edit.html
│   └── test
```

설명 포인트:

> 이 프로젝트는 일반적인 Spring Boot MVC 구조를 따릅니다. `controller`는 요청을 받고, `service`는 회원가입 같은 비즈니스 로직을 처리하며, `repository`는 데이터베이스와 연결됩니다. `entity`는 DB 테이블과 매핑되는 객체이고, `templates`는 사용자가 보는 HTML 화면입니다.

## 5. MVC 구조 설명

```text
사용자 브라우저
    ↓ 요청
Controller
    ↓ 데이터 조회/저장 요청
Repository
    ↓
Database
    ↓ 조회 결과
Controller
    ↓ Model에 데이터 담기
Thymeleaf Template
    ↓
HTML 응답
```

발표 대본:

> 사용자가 브라우저에서 특정 주소로 요청을 보내면 먼저 Controller가 요청을 받습니다. Controller는 필요한 경우 Repository를 통해 DB에서 데이터를 조회하거나 저장합니다. 이후 조회한 데이터를 Model에 담아서 Thymeleaf 템플릿으로 전달하고, Thymeleaf가 최종 HTML 화면을 만들어 사용자에게 보여줍니다.

## 6. 핵심 클래스 설명

### 6.1 `Konyang1Application.java`

Spring Boot 애플리케이션의 시작점입니다.

```java
SpringApplication.run(Konyang1Application.class, args);
```

설명 포인트:

> 이 메서드가 실행되면서 Spring Boot 서버가 시작되고, 컨트롤러, 서비스, 리포지토리 같은 스프링 객체들이 자동으로 등록됩니다.

### 6.2 `SecurityConfig.java`

로그인, 로그아웃, 접근 권한, 비밀번호 암호화 방식을 설정합니다.

핵심 역할:

- 글 작성, 수정, 삭제는 로그인 사용자만 가능
- 홈, 로그인, 회원가입, 게시판 목록, 게시글 상세는 비로그인 사용자도 접근 가능
- 로그인 성공 후 홈 화면으로 이동
- 로그아웃 성공 후 홈 화면으로 이동
- 비밀번호 암호화에 `BCryptPasswordEncoder` 사용

설명 포인트:

> 보안 설정에서는 어떤 URL을 로그인한 사용자만 접근할 수 있게 할지 정합니다. 게시글 작성, 수정, 삭제는 로그인한 사용자만 가능하게 설정했고, 게시글 상세 조회나 게시판 목록은 누구나 볼 수 있게 했습니다.

### 6.3 `MemberService.java`

회원가입과 로그인 인증에 필요한 사용자 조회를 담당합니다.

핵심 로직:

- `register()`에서 아이디 중복 확인
- 비밀번호 암호화 후 회원 저장
- `loadUserByUsername()`에서 Spring Security 로그인 인증용 사용자 정보 조회

설명 포인트:

> `MemberService`는 `UserDetailsService`를 구현합니다. Spring Security는 로그인할 때 `loadUserByUsername()` 메서드를 호출해서 DB에 저장된 사용자 정보를 찾고, 입력한 비밀번호와 암호화된 비밀번호를 비교합니다.

### 6.4 `ArticleController.java`

게시글 관련 요청을 처리하는 핵심 컨트롤러입니다.

담당 기능:

- 글쓰기 화면 표시
- 게시글 등록
- 카테고리별 목록 조회
- 게시글 상세 조회
- 게시글 수정 화면 표시
- 게시글 수정 처리
- 게시글 삭제 처리
- 작성자 본인 여부 검사

설명 포인트:

> `ArticleController`는 게시글 기능의 중심입니다. 글을 저장할 때는 로그인한 사용자의 닉네임을 작성자로 저장하고, 수정이나 삭제할 때는 현재 로그인한 사용자의 닉네임과 게시글 작성자 닉네임을 비교해서 본인 글인지 확인합니다.

### 6.5 `Article.java`

게시글 정보를 DB 테이블과 연결하는 Entity 클래스입니다.

주요 필드:

- `id`: 게시글 번호
- `category`: 게시판 카테고리
- `title`: 제목
- `content`: 내용
- `author`: 작성자 닉네임
- `createdAt`: 작성일

특징:

- `@Entity`로 DB 테이블과 연결
- `@PrePersist`로 저장 직전에 작성일 자동 입력
- `update()` 메서드로 수정 내용 반영

설명 포인트:

> `Article`은 게시글 테이블과 매핑되는 클래스입니다. 글이 처음 저장될 때 `@PrePersist`가 실행되어 현재 시간이 자동으로 `createdAt`에 저장됩니다.

### 6.6 `ArticleRepository.java`

게시글 DB 접근을 담당합니다.

핵심 메서드:

```java
List<Article> findByCategory(String category);
```

설명 포인트:

> Spring Data JPA는 메서드 이름만 보고 쿼리를 자동으로 만들어줍니다. `findByCategory`는 category 값이 같은 게시글만 조회하는 쿼리로 동작합니다.

## 7. 데이터베이스 구조

### 7.1 회원 테이블 개념

`Member` Entity는 `members` 테이블과 연결됩니다.

| 필드 | 의미 |
| --- | --- |
| `id` | 회원 고유 번호 |
| `username` | 로그인 아이디 |
| `password` | 암호화된 비밀번호 |
| `nickname` | 게시글에 표시되는 닉네임 |

### 7.2 게시글 테이블 개념

`Article` Entity는 `article` 테이블과 연결됩니다.

| 필드 | 의미 |
| --- | --- |
| `id` | 게시글 번호 |
| `category` | 게시판 카테고리 |
| `title` | 게시글 제목 |
| `content` | 게시글 내용 |
| `author` | 작성자 닉네임 |
| `createdAt` | 작성일 |

설명 포인트:

> 회원과 게시글은 각각 Entity 클래스로 관리됩니다. JPA가 이 Entity를 바탕으로 DB 테이블과 객체를 연결해주기 때문에 SQL을 직접 많이 작성하지 않아도 데이터를 저장하고 조회할 수 있습니다.

## 8. 주요 기능 흐름

### 8.1 회원가입 흐름

```text
register.html
    ↓ POST /register
MemberController
    ↓
MemberService.register()
    ↓
MemberRepository
    ↓
members 테이블 저장
    ↓
로그인 페이지로 이동
```

발표 대본:

> 회원가입 화면에서 정보를 입력하면 `MemberController`가 요청을 받습니다. 먼저 비밀번호와 비밀번호 확인 값이 같은지 검사하고, `MemberService`에서 아이디 중복 여부를 확인합니다. 문제가 없으면 비밀번호를 암호화해서 DB에 저장하고 로그인 페이지로 이동합니다.

### 8.2 로그인 흐름

```text
login.html
    ↓ POST /login
Spring Security
    ↓
MemberService.loadUserByUsername()
    ↓
비밀번호 검증
    ↓
로그인 성공 시 홈 화면 이동
```

발표 대본:

> 로그인은 직접 컨트롤러에서 처리하지 않고 Spring Security가 처리합니다. 사용자가 아이디와 비밀번호를 입력하면 Spring Security가 `MemberService`에서 사용자 정보를 가져오고, 암호화된 비밀번호와 비교해서 로그인 성공 여부를 판단합니다.

### 8.3 게시글 작성 흐름

```text
board.html의 글쓰기 버튼
    ↓ GET /write?category=카테고리
write.html
    ↓ POST /articles/create
ArticleController.createArticle()
    ↓
ArticleRepository.save()
    ↓
해당 카테고리 게시판으로 이동
```

발표 대본:

> 게시판 목록에서 글쓰기 버튼을 누르면 현재 게시판 카테고리가 글쓰기 화면으로 전달됩니다. 글을 작성하고 등록하면 `ArticleForm`으로 제목, 내용, 카테고리가 넘어오고, 로그인 사용자의 닉네임을 작성자로 넣어 게시글을 저장합니다. 저장 후에는 해당 카테고리 게시판 목록으로 돌아갑니다.

### 8.4 카테고리별 게시판 조회 흐름

```text
GET /board/{category}
    ↓
ArticleController.boardList()
    ↓
ArticleRepository.findByCategory(category)
    ↓
board.html에 articles 전달
```

발표 대본:

> `/board/취업`, `/board/자유게시판` 같은 주소로 들어오면 주소의 카테고리 값을 가져와서 해당 카테고리 글만 조회합니다. 조회 결과는 `articles`라는 이름으로 화면에 전달되고, `board.html`에서 반복문으로 목록을 출력합니다.

### 8.5 게시글 수정 흐름

```text
article.html
    ↓ 작성자 본인인 경우 수정 버튼 표시
GET /articles/{id}/edit
    ↓
작성자 검사
    ↓
edit.html
    ↓ POST /articles/{id}/update
    ↓
다시 작성자 검사
    ↓
Article.update()
    ↓
게시글 상세 페이지로 이동
```

발표 대본:

> 게시글 상세 화면에서는 로그인한 사용자가 작성자인 경우에만 수정 버튼이 보입니다. 하지만 화면에서 버튼을 숨기는 것만으로는 보안이 부족하기 때문에, 수정 화면에 접근할 때와 실제 수정 내용을 저장할 때 모두 서버에서 작성자인지 다시 검사합니다.

### 8.6 게시글 삭제 흐름

```text
article.html
    ↓ 작성자 본인인 경우 삭제 버튼 표시
POST /articles/{id}/delete
    ↓
작성자 검사
    ↓
ArticleRepository.delete()
    ↓
해당 카테고리 게시판으로 이동
```

발표 대본:

> 삭제 기능도 수정과 마찬가지로 작성자 본인만 가능합니다. 삭제 버튼은 작성자에게만 보이고, 실제 삭제 요청이 들어왔을 때도 컨트롤러에서 다시 작성자 검사를 합니다. 삭제가 끝나면 원래 글이 있던 카테고리 게시판으로 이동합니다.

## 9. 보안 처리

### 9.1 로그인 필요 URL

`SecurityConfig.java`에서 다음 기능은 로그인 사용자만 접근하도록 설정했습니다.

- 글쓰기 화면
- 글 등록
- 글 수정 화면
- 글 수정 처리
- 글 삭제 처리

### 9.2 작성자 권한 검사

`ArticleController`의 `isAuthor()`와 `validateAuthor()`가 작성자 여부를 검사합니다.

검사 방식:

```text
현재 로그인 사용자 username
    ↓
MemberRepository.findByUsername()
    ↓
사용자의 nickname 조회
    ↓
Article.author와 비교
```

설명 포인트:

> 이 프로젝트에서는 게시글 작성자를 닉네임으로 저장하고 있습니다. 그래서 현재 로그인한 사용자의 닉네임과 게시글의 작성자 닉네임이 같으면 본인 글로 판단합니다.

### 9.3 CSRF 토큰

POST 요청을 보내는 폼에는 CSRF 토큰을 포함했습니다.

적용 화면:

- `login.html`
- `register.html`
- `write.html`
- `edit.html`
- `article.html`의 삭제 폼

설명 포인트:

> Spring Security는 기본적으로 CSRF 공격을 막기 위해 POST 요청에 CSRF 토큰을 요구합니다. 그래서 로그인, 회원가입, 글쓰기, 수정, 삭제처럼 데이터를 변경하는 요청에는 hidden input으로 CSRF 토큰을 함께 보냅니다.

## 10. Thymeleaf 사용 부분

### 10.1 데이터 출력

```html
<span th:text="${categoryName}"></span>
```

서버에서 전달한 `categoryName` 값을 HTML에 출력합니다.

### 10.2 반복 출력

```html
<tr th:each="article : ${articles}">
```

게시글 목록을 반복해서 출력합니다.

### 10.3 조건부 출력

```html
<a th:if="${isAuthor}">수정하기</a>
```

작성자인 경우에만 수정 버튼을 보여줍니다.

### 10.4 동적 URL 생성

```html
<a th:href="@{/articles/{id}(id=${article.id})}">
```

게시글 id를 URL에 넣어 상세 페이지 링크를 만듭니다.

설명 포인트:

> Thymeleaf는 서버에서 전달한 데이터를 HTML 안에서 쉽게 사용할 수 있게 해줍니다. 이 프로젝트에서는 게시글 목록 반복, 작성자 조건에 따른 버튼 표시, 게시글 상세 링크 생성에 사용했습니다.

## 11. 발표 순서 추천

1. 프로젝트 한 줄 소개
2. 사용 기술 소개
3. 전체 폴더 구조 설명
4. MVC 구조 설명
5. 회원가입/로그인 흐름 설명
6. 게시글 작성/조회 흐름 설명
7. 수정/삭제 권한 검사 설명
8. Thymeleaf 화면 처리 설명
9. 마무리 및 개선점

## 12. 3분 발표 대본 예시

안녕하세요. 제가 만든 프로젝트는 Spring Boot 기반의 건양대학교 게시판 웹 애플리케이션입니다.

이 프로젝트의 주요 기능은 회원가입, 로그인, 로그아웃, 카테고리별 게시글 조회, 게시글 작성, 수정, 삭제입니다. 특히 수정과 삭제는 본인이 작성한 글에 대해서만 가능하도록 구현했습니다.

기술은 Java 21과 Spring Boot를 사용했고, 웹 요청 처리는 Spring Web MVC, 데이터베이스 연동은 Spring Data JPA, 로그인과 권한 관리는 Spring Security를 사용했습니다. 화면은 Thymeleaf 템플릿으로 구성했고, 데이터베이스는 MySQL을 사용했습니다.

프로젝트 구조는 Spring MVC 방식으로 나누었습니다. `Controller`는 사용자의 요청을 받고, `Service`는 회원가입과 로그인에 필요한 로직을 처리합니다. `Repository`는 DB 접근을 담당하고, `Entity`는 DB 테이블과 매핑됩니다. `templates` 폴더에는 실제 사용자에게 보여지는 HTML 화면들이 있습니다.

회원가입은 `MemberController`와 `MemberService`에서 처리합니다. 아이디 중복을 검사하고, 비밀번호는 `BCryptPasswordEncoder`를 이용해 암호화해서 저장합니다. 로그인은 Spring Security가 담당하며, `MemberService`의 `loadUserByUsername()` 메서드를 통해 사용자 정보를 조회합니다.

게시글 기능은 `ArticleController`가 담당합니다. 사용자가 글을 작성하면 카테고리, 제목, 내용이 `ArticleForm`으로 전달되고, 로그인한 사용자의 닉네임을 작성자로 저장합니다. 게시판 목록은 `findByCategory()`를 사용해 카테고리별로 글을 조회합니다.

수정과 삭제 기능에서는 보안 처리를 중요하게 생각했습니다. 화면에서는 작성자인 경우에만 수정/삭제 버튼을 보여주고, 실제 요청이 들어왔을 때도 서버에서 현재 로그인한 사용자의 닉네임과 게시글 작성자 닉네임을 비교합니다. 그래서 다른 사용자가 주소를 직접 입력해도 다른 사람의 글은 수정하거나 삭제할 수 없습니다.

마지막으로 Thymeleaf를 사용해서 서버에서 전달받은 게시글 목록을 화면에 반복 출력하고, 작성자 여부에 따라 버튼을 조건부로 보여주었습니다. 이 프로젝트를 통해 Spring Boot의 MVC 구조, JPA를 이용한 DB 처리, Spring Security를 이용한 인증과 권한 관리 흐름을 학습할 수 있었습니다.

## 13. 예상 질문과 답변

### Q1. 왜 Spring Security를 사용했나요?

로그인, 로그아웃, 접근 권한 설정, 비밀번호 암호화를 직접 구현하면 보안상 실수하기 쉽습니다. Spring Security를 사용하면 검증된 방식으로 인증과 권한 관리를 처리할 수 있기 때문에 사용했습니다.

### Q2. 비밀번호는 어떻게 저장되나요?

비밀번호는 평문으로 저장하지 않고 `BCryptPasswordEncoder`로 암호화한 뒤 DB에 저장합니다. 그래서 DB 내용을 직접 보더라도 원래 비밀번호를 알 수 없습니다.

### Q3. 작성자 본인만 수정/삭제 가능한 이유는 무엇인가요?

게시글에는 작성자 닉네임이 저장되어 있습니다. 수정이나 삭제 요청이 들어오면 현재 로그인한 사용자의 닉네임을 조회한 뒤 게시글 작성자와 비교합니다. 두 값이 같을 때만 수정과 삭제를 허용합니다.

### Q4. JPA Repository를 사용한 장점은 무엇인가요?

기본적인 저장, 조회, 삭제 기능을 직접 SQL로 작성하지 않아도 사용할 수 있습니다. 또한 `findByCategory`처럼 메서드 이름만으로 조건 조회 기능을 만들 수 있어 코드가 간결해집니다.

### Q5. CSRF 토큰은 왜 필요한가요?

CSRF 토큰은 사용자가 의도하지 않은 POST 요청이 실행되는 것을 막기 위한 보안 장치입니다. 글쓰기, 수정, 삭제처럼 데이터를 변경하는 요청에 토큰을 포함해 안전한 요청인지 확인합니다.

## 14. 개선할 수 있는 점

- 게시글 작성자를 닉네임 문자열 대신 회원 Entity와 관계로 연결하기
- 게시글 목록에 페이징 기능 추가하기
- 댓글 기능 추가하기
- 제목/내용 검색 기능 추가하기
- 에러 페이지를 사용자 친화적으로 만들기
- CSS 파일을 분리해서 화면 스타일 관리하기
- 테스트 코드 추가하기

마무리 문장:

> 이 프로젝트는 단순한 게시판 기능을 넘어서 회원 인증, 권한 검사, JPA 기반 데이터 처리, Thymeleaf 화면 구성을 모두 포함한 Spring Boot 웹 애플리케이션입니다.
