# misssion_SpringPremier

> Themyleaf, jwt로 구현한 기본 사이트

</br>

## 1. 프로젝트 개요

- **로그인**: jwt 로그인
- **회원가입**: 일반회원가입
- **회원관리**: 회원 탈퇴 기능
- **게시판관리**: 게시판 생성, 삭제기능

</br>

## 2. 기술 스택

### 핵심
- **Java 21** 
- **Spring Boot 3.4.5** - 메인 프레임워크
- **thymeleaft 3.1.3** - 프론트엔드

### 데이터베이스
- **H2 Database** - 메인 데이터베이스


</br>

## 3. 프로젝트 구조

```
groom-mission-SpringPremier/
├── .github/
│   └── ISSUE_TEMPLATE/  # GitHub 이슈 템플릿
│   └── workflows/  # GitHub Actions 워크플로우 파일이 위치하는 디렉토리
├── gradle/
│   └── wrapper/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── groom/
│   │   │       ├── domain/
│   │   │       │   ├── board/      # 게시판 관련 디렉토리
│   │   │       │   ├── comment/    # 코멘트 관련 디렉토리
│   │   │       │   ├── member/     # 회원관련 디렉토리
│   │   │       │   └── post/       # 우편번호 관련 디렉토리
│   │   │       └── global/         # 애플리케이션의 전역 설정 및 공통 기능이 위치하는 디렉토리
│   │   │           ├── config/     
│   │   │           ├── controller/ 
│   │   │           ├── dto/
│   │   │           ├── exception/
│   │   │           ├── filter/
│   │   │           ├── interceptor/
│   │   │           ├── repository/
│   │   │           ├── security/
│   │   │           └── service/
│   │   └── resources/
│   │       ├── static/
│   │       └── templates/ : Gradle 설정 파일로, 멀티 프로젝트 설정 등이 포함
│   └── test/
├── .gitattributes # Git 속성 파일로, 파일 형식 및 병합 전략 등을 정의
├── .gitignore # Git에서 무시할 파일 및 디렉토리를 지정하는 파일
├── Procfile # 애플리케이션 실행 방식을 정의하는 파일
├── build.gradle # Gradle 빌드 스크립트 파일로, 의존성 및 빌드 설정이 포함
├── deps.txt # 프로젝트의 의존성 목록이 포함된 텍스트 파일
├── gradlew # Gradle 래퍼 스크립트로, Gradle을 설치하지 않고도 프로젝트를 빌드
├── gradlew.bat # Gradle 래퍼 스크립트로, Gradle을 설치하지 않고도 프로젝트를 빌드
└── settings.gradle # Gradle 설정 파일로, 멀티 프로젝트 설정 등이 포함
```

</br>


## 4. 시작하기

### 요구사항
- Java 21
- Git
- IntelliJ IDEA 권장
- **Lombok** 플러그인 설치
- **Checkstyle** 플러그인 설치 (Naver 스타일 가이드)

### 1) 저장소 클론
```bash
git clone https://github.com/jwp9622/groom-mission-SpringPremier.git
cd groom-mission-SpringPremier
```

### 2) 환경 변수 설정
`.env.example` 파일을 복사해 `.env` 파일을 생성하고 필요한 환경 변수를 설정합니다.

## 5. 배포
- AWS IM github-action2
- Elastic Beanstalk 애플리케이션명 mission_SpringPremier_env
- Elastic Beanstalk 환경 MissionSpringPremierenv-env
- AWS S3 groom-mission-springpremier
 