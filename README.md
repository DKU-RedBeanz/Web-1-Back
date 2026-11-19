# 레드빈즈 웹개발 프로젝트 1팀

외부 AI API를 활용한 영단어·회화 학습과, 조건에 맞는 스터디 추천·연결을 제공하는 서비스의 백엔드 저장소입니다. 로그인·회원 정보와 스터디 내부 소통을 기획하며, 댓글 또는 채팅 중 선택은 미정입니다.

## 프로젝트 정보

| 항목 | 내용 |
|---|---|
| 프로젝트명 | 레드빈즈 웹개발 프로젝트 1팀 |
| 핵심 영역 | 회원, AI 학습, 스터디 탐색·추천·참여, 내부 소통 |
| 개발 상태 | 초기 구성 중 |
| 서비스 버전 | 미릴리스 |
| 저장소 | [DKU-RedBeanz/Web-1-Back](https://github.com/DKU-RedBeanz/Web-1-Back) |
| 라이선스 | [MIT](LICENSE) |

Spring Boot와 MySQL로 회원가입, 아이디 중복 검사, 내 정보 관리 및 관리자용 회원 목록 API를 제공합니다.

## 기술 및 프로젝트 설정

| 항목 | 설정 |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.1 |
| 빌드 | Gradle · Groovy DSL |
| Group | `com.redbeanz` |
| Artifact | `redbeanz-backend` |
| 기본 패키지 | `com.redbeanz.backend` |
| 패키징 | Jar |
| 설정 형식 | YAML |
| 로컬·테스트용 DB | **각자의 개인 로컬 MySQL** |
| 팀 공유 DB | Aiven for MySQL |

Gradle·MySQL의 세부 버전은 초기 프로젝트 구성 시 확정합니다.

### 주요 의존성

- Spring Web
- Lombok
- Validation
- Spring Data JPA
- MySQL Driver
- Spring Security

의존성 추가와 기능 구현은 구분합니다. 인증·인가 초기 설정의 작업 범위는 스프린트 이슈에서 관리합니다.

## 협업 위키

- [협업 가이드](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Work-and-Review): Git 컨벤션과 작업·리뷰 절차
- [Process](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Assignments): 스프린트별 작업과 진행 기준
- [기타](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Home#기타): 요구사항·로컬 환경·아키텍처·ERD
- [회의](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Meetings): 회의 기록

현재 [Sprint 1](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Sprint-1)은 공통 프로젝트·개인 로컬 MySQL 연결·Security 초기 설정·사용자 흐름 정리를 다룹니다. 마감은 **2026-09-24(목)**입니다.

Spring Boot는 개인 `.env`를 읽습니다. `.env.example`을 복사해 사용하세요. 개인 `.env`는 Git에서 제외합니다.

## 역할

| 담당 | 작업 |
|---|---|
| 유다현 | Spring 공통 프로젝트·MySQL·.env 설정, 백엔드 연결 지원 |
| 유관우 | Spring Security 초기 설정, 개인 MySQL 연결 확인 |
| 박인찬 | [프론트 저장소](https://github.com/DKU-RedBeanz/Web-1-Front)의 기본 프로젝트·스터디 샘플 화면 |
| 멘토 | 범위 조율, 코드 리뷰·병합 |

DB 연결 완료 기준은 백엔드 두 사람의 `SELECT 1` 성공입니다. AI 제공사·추천 방식·댓글/채팅·인증 방식은 추후 확정합니다. 실제 AI 연동과 CRUD는 Sprint 1 필수 구현 범위가 아닙니다.

## 실행 방법

1. 로컬 MySQL에서 데이터베이스를 생성합니다.

```sql
CREATE DATABASE IF NOT EXISTS redbeanz CHARACTER SET utf8mb4;
```

2. `mysql -u 본인_계정 -p redbeanz -e "source db/01_create_users.sql"`로 회원 테이블을 생성합니다.
3. `.env.example`을 `.env`로 복사하고 MySQL 접속 정보를 입력합니다.
4. JDK 21에서 `.\gradlew.bat bootRun`으로 서버를 실행합니다. 기본 포트는 8080입니다.

로컬 테스트 DB를 사용해 `.\gradlew.bat test`를 실행할 수 있습니다.

Aiven에 테이블이 이미 있다면 `.env`의 `DB_URL`에 `jdbc:mysql://<호스트>:<포트>/<DB이름>?sslMode=REQUIRED`를 넣고 `DB_USERNAME`과 `DB_PASSWORD`를 설정합니다. 연결만 확인하려면 `.\gradlew.bat test --tests "*RedbeanzBackendApplicationTests" --rerun`을 실행하세요.

### 회원 API

| 요청 | 권한 | 본문 및 응답 |
|---|---|---|
| `POST /api/users` | 공개 | `loginId`, `email`, `password`, `nickname` → 201 회원 정보, 400 입력 오류, 409 아이디·이메일 중복 |
| `GET /api/users/login-id-availability?loginId=redbeanz` | 공개 | 200 `{ "available": true }` 또는 `{ "available": false }`, 400 잘못된 아이디 |
| `GET /api/users/me` | 본인 | 200 내 정보와 `X-CSRF-TOKEN` 응답 헤더 |
| `GET /api/users` | 관리자 | 200 회원 정보 배열, 403 권한 부족 |
| `PATCH /api/users/me` | 본인 | `email`, `nickname`, `password` 중 변경할 필드 → 200 변경된 회원 정보, 400 입력 오류, 409 이메일 중복 |
| `DELETE /api/users/me` | 본인 | 회원 정보의 숫자 `id`, 현재 `password` → 204 탈퇴 완료, 403 본인 확인 실패 |

보호된 API는 가입할 때 사용한 `loginId`와 `password`로 HTTP Basic 인증을 합니다. 운영 환경에서는 HTTPS를 사용합니다. `PATCH`와 `DELETE` 요청에는 먼저 `GET /api/users/me`에서 받은 `JSESSIONID` 쿠키와 `X-CSRF-TOKEN` 헤더를 함께 보냅니다.

`PATCH`로 비밀번호를 바꿀 때는 현재 비밀번호로 인증하고 JSON 본문의 `password`에 새 비밀번호를 보냅니다. 회원 목록과 회원 정보 응답에는 비밀번호와 해시가 포함되지 않습니다.

## 협업 방법

이슈 → 담당자 지정 → 브랜치 → 작업·PR → 리뷰 → 병합 순서로 진행합니다.

자세한 규칙은 [Git 컨벤션](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Git-Convention)과 [작업 및 코드 리뷰 절차](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Work-and-Review)를 참고하세요.

[Issue 템플릿](.github/ISSUE_TEMPLATE/task.md) · [PR 템플릿](.github/pull_request_template.md)
