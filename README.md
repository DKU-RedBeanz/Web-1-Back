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

현재 저장소는 프로젝트 안내와 협업 템플릿을 준비하는 단계입니다. 아래 기술 설정과 의존성은 초기 Spring 프로젝트에 적용할 예정이며, 아직 구현된 기능을 의미하지 않습니다.

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
| 추후 팀 공유 DB | AWS RDS MySQL |

Gradle·MySQL의 세부 버전은 초기 프로젝트 구성 시 확정합니다.

### 구성 예정 의존성

- Spring Web
- Lombok
- Validation
- Spring Data JPA
- MySQL Driver
- Spring Boot DevTools
- SpringDoc OpenAPI
- Spring Security

의존성 추가와 기능 구현은 구분합니다. 인증·인가 초기 설정의 작업 범위는 스프린트 이슈에서 관리합니다.

## 협업 위키

- [협업 가이드](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Work-and-Review): Git 컨벤션과 작업·리뷰 절차
- [Process](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Assignments): 스프린트별 작업과 진행 기준
- [기타](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Home#기타): 요구사항·로컬 환경·아키텍처·ERD
- [회의](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Meetings): 회의 기록

현재 [Sprint 1](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Sprint-1)은 공통 프로젝트·개인 로컬 MySQL 연결·Security 초기 설정·사용자 흐름 정리를 다룹니다. 마감은 **2026-09-24(목)**입니다.

공통 설정은 `.env.example`을 개인 `.env`로 복사해 사용하고 Spring Boot가 직접 읽도록 구현할 예정입니다. 개인 `.env`는 Git에서 제외합니다. 자세한 방법은 [로컬 환경 안내](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Local-Development)를 참고하세요.

## 역할

| 담당 | 작업 |
|---|---|
| 유다현 | Spring 공통 프로젝트·MySQL·.env 설정, 백엔드 연결 지원 |
| 유관우 | Spring Security 초기 설정, 개인 MySQL 연결 확인 |
| 박인찬 | [프론트 저장소](https://github.com/DKU-RedBeanz/Web-1-Front)의 기본 프로젝트·스터디 샘플 화면 |
| 멘토 | 범위 조율, 코드 리뷰·병합 |

DB 연결 완료 기준은 백엔드 두 사람의 `SELECT 1` 성공입니다. AI 제공사·추천 방식·댓글/채팅·인증 방식은 추후 확정합니다. 실제 AI 연동과 CRUD는 Sprint 1 필수 구현 범위가 아닙니다.

## 실행 방법

1. 이 저장소를 clone합니다.

git clone https://github.com/DKU-RedBeanz/Web-1-Back.git


2. `.env.example`을 복사해 `.env` 파일을 만들고, 본인의 로컬 MySQL 계정 정보로 값을 채웁니다.

DB_URL=jdbc:mysql://localhost:3306/redbeanz
DB_USERNAME=본인_로컬_MySQL_계정
DB_PASSWORD=본인_로컬_MySQL_비밀번호

`.env`는 절대 커밋하지 않습니다 (`.gitignore`에 등록되어 있음).

3. 로컬 MySQL에 빈 데이터베이스를 생성합니다.
```sql
   CREATE DATABASE IF NOT EXISTS redbeanz CHARACTER SET utf8mb4;
```

4. IDE(IntelliJ 등)에서 프로젝트를 열고, Project SDK와 Gradle JVM을 21로 설정합니다.

5. `RedbeanzBackendApplication`의 main 메서드를 실행합니다. 콘솔에 `Started RedbeanzBackendApplication`이 뜨면 정상 실행된 것입니다.

### DB 연결 검증

애플리케이션 DataSource를 통해 `SELECT 1`을 실행하는 테스트가 포함되어 있습니다.

- 위치: `src/test/java/com/redbeanz/backend/RedbeanzBackendApplicationTests.java`
- 테스트명: `selectOneReturnsSuccessfully()`
- 실행: IDE에서 해당 테스트를 실행하거나, 터미널에서 아래 명령 실행

.\gradlew test --tests "com.redbeanz.backend.RedbeanzBackendApplicationTests.selectOneReturnsSuccessfully"


### 빌드 확인 명령 (사용한 버전: Gradle 9.7.1, JDK 21)

.\gradlew --version
.\gradlew classes

## 협업 방법

이슈 → 담당자 지정 → 브랜치 → 작업·PR → 리뷰 → 병합 순서로 진행합니다.

자세한 규칙은 [Git 컨벤션](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Git-Convention)과 [작업 및 코드 리뷰 절차](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Work-and-Review)를 참고하세요.

[Issue 템플릿](.github/ISSUE_TEMPLATE/task.md) · [PR 템플릿](.github/pull_request_template.md)
