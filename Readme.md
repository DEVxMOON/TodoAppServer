# Todo App Server
## 프로젝트 소개

목적 : 기존 프로젝트 개선

Kotlin + Spring Boot

### 변경 및 개선 사항

- 기존에 작성하던 Controller, Service 방식 개선
- CustomException 정의 및 Spring AOP 적용
- QueryDSL을 사용한 트윗 검색 기능 추가
- Pagable을 사용해 페이징 (Cursor, Offset) 구현
- 테스트 코드 작성
    - 서비스 코드 일부 테스트 통과 실패
- AWS 기능 추가
    - S3 이미지 업로드(구현 진행중)
    - EC2 배포

## 도메인 별 기능 설명

### Auth

- 기본 회원가입 및 로그인과 관련된 기능
- 일반 로그인과 소셜 로그인으로 구분됨
    - 소셜 로그인의 경우, 네이버, 구글 사용
        - 이메일을 별도의 인증 없이 받아올 수 있다.

### User

- 사용자 정보와 관련된 기능
    - 전체 사용자를 볼 수 있음
    - 사용자의 상세 정보를 볼 수 있음
    - 사용자 정보를 수정할 수 있음
        - 단, 로그인 된 사용자만이 자신의 정보 수정 가능
        - 그 외, 불가능
    - 사용자 정보 삭제 가능
        - 단, 로그인 된 사용자만이 자신의 정보 수정 가능
        - 그 외, 불가능
- 역할 추가 - 미구현 (ADMIN, USER)
    - IF) ADMIN : 전체 사용자의 정보 수정 및 삭제 가능 - 단, 트윗 및 좋아요 내역은 수정 불가능

### Tweet

- 트윗 작성과 관련된 기능
    - Tweet 추가
    - 답멘션 추가
    - Tweet 삭제
    - 수정 기능은 없다 → tweet sns에는 수정기능이 없다
- 트윗 전체보기 - Home
    - 페이지네이션을 활용
- 트윗 상세보기
- 트윗 검색하기 - queryDSL 사용

### Love

- 좋아요 기능(마음)과 관련된 모든 것
    - 트윗에 좋아요 추가 및 삭제
    - 해당 트윗을 마음에 들어한사용자 리스트 불러오기
    - 사용자가 좋아요한 트윗 리스트 불러오기

## API 설계

### Auth API

| Command | URL | Method | Response |
| --- | --- | --- | --- |
| 회원가입 | /auth/register | POST | 201 |
| 로그인 | /auth/login | POST | 200 |
| 소셜로그인 - 로그인페이지로 | /oauth/{provider} | GET | 200 |
| 소셜로그인 - 원래 페이지로 | /oauth/{provider}/callback | GET | 200 |

### User API

| Command | URL | Method | Response |
| --- | --- | --- | --- |
| 전체 사용자 불러오기 | /user/all | GET | 200 |
| 사용자 상세보기 | /user/{id} | GET | 200 |
| 사용자 정보 수정 | /user | PUT | 200 |
| 비밀번호 변경 | /user/password-change | PATCH | 200 |
| 사용자 삭제(회원 탈퇴) | /user | DELETE | 204 |

### Tweet API

| Command | URL | Method | Response |
| --- | --- | --- | --- |
| 트윗 생성 | /tweets | POST | 201 |
| 트윗 멘션 생성 | /tweets/{id} | POST | 201 |
| 트윗 삭제 | /tweets/{id} | DELETE | 204 |
| 트윗 전체보기 | /tweets/home | GET | 200 |
| 트윗 상세보기 | /tweets/{id} | GET | 200 |
| 트윗 검색하기 | /tweets/search | GET | 200 |

### Love API

| Command | URL | Method | Response |
| --- | --- | --- | --- |
| 마음 추가 | /love | POST | 201 |
| 마음 삭제 | /love | DELETE | 204 |
| 해당 트윗을 마음에 들어한
사용자 리스트 불러오기 | /tweets/{id}/loves | GET | 200 |
| 사용자가 좋아요한
트윗 리스트 불러오기 | /user/loves | GET | 200 |

## ERD

![캡처](https://github.com/DEVxMOON/sns/assets/137713546/37f57afd-f24d-49e4-8524-64c3ade035e0)


## 패키지 구조

```kotlin
src
├─main
│  ├─kotlin
│  │  └─com
│  │      └─hr
│  │          └─sns
│  │              ├─aspect
│  │              ├─domain
│  │              │  ├─auth
│  │              │  │  ├─controller
│  │              │  │  ├─dto
│  │              │  │  ├─oauth
│  │              │  │  │  ├─client
│  │              │  │  │  ├─controller
│  │              │  │  │  ├─dto
│  │              │  │  │  ├─google
│  │              │  │  │  │  └─dto
│  │              │  │  │  ├─naver
│  │              │  │  │  │  └─dto
│  │              │  │  │  ├─service
│  │              │  │  │  └─type
│  │              │  │  └─service
│  │              │  ├─love
│  │              │  │  ├─controller
│  │              │  │  ├─dto
│  │              │  │  ├─entity
│  │              │  │  ├─repository
│  │              │  │  └─service
│  │              │  ├─tweet
│  │              │  │  ├─controller
│  │              │  │  ├─dto
│  │              │  │  ├─entity
│  │              │  │  ├─repository
│  │              │  │  └─service
│  │              │  └─user
│  │              │      ├─controller
│  │              │      ├─dto
│  │              │      ├─entity
│  │              │      ├─repository
│  │              │      └─service
│  │              ├─exception
│  │              └─infra
│  │                  ├─restclient
│  │                  ├─security
│  │                  │  ├─config
│  │                  │  └─jwt
│  │                  └─swagger
│  └─resources
│      ├─static
│      └─templates
└─test
    └─kotlin
        └─com
            └─hr
                └─sns
                    ├─controllertest
                    ├─repositorytest
                    └─servicetest
```
