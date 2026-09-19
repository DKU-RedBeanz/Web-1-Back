# 레드빈즈 웹개발 프로젝트 1팀

레드빈즈 웹개발 프로젝트 1팀의 백엔드 저장소입니다. 서비스 주제와 요구사항은 팀 논의 후 작성합니다.

## 프로젝트 정보

| 항목 | 내용 |
|---|---|
| 프로젝트명 | 레드빈즈 웹개발 프로젝트 1팀 |
| 핵심 영역 | 팀 논의 후 확정 |
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

현재 [Sprint 1](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Sprint-1)은 공통 프로젝트·개인 로컬 MySQL 연결·Security 초기 설정·사용자 흐름 정리를 다룹니다. 담당자와 기한은 미정입니다.

공통 설정은 `.env.example`을 개인 `.env`로 복사해 사용하고 Spring Boot가 직접 읽도록 구현할 예정입니다. 개인 `.env`는 Git에서 제외합니다. 자세한 방법은 [로컬 환경 안내](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Local-Development)를 참고하세요.

## 실행 방법

Spring 기본 프로젝트 업로드 후 추가 예정입니다. 학생 A의 초기 코드 PR에 MySQL 준비, 빈 DB 생성, 환경변수 등록, 서버 실행, 연결 확인 방법을 함께 작성합니다.

## 협업 방법

이슈 → 담당자 지정 → 브랜치 → 작업·PR → 리뷰 → 병합 순서로 진행합니다.

자세한 규칙은 [Git 컨벤션](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Git-Convention)과 [작업 및 코드 리뷰 절차](https://github.com/DKU-RedBeanz/Web-1-Back/wiki/Work-and-Review)를 참고하세요.

[Issue 템플릿](.github/ISSUE_TEMPLATE/task.md) · [PR 템플릿](.github/pull_request_template.md)
