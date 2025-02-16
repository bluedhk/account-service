# Account Service

## 📌 프로젝트 개요
**Account Service**는 Spring Boot 기반의 RESTful API로, 사용자의 계좌 정보를 관리하고 입출금 기능을 제공하는 서비스입니다.

## 🚀 기술 스택
- **언어**: Java 17+
- **프레임워크**: Spring Boot 3.x
- **빌드 도구**: Gradle
- **데이터베이스**: H2 (테스트용) / PostgreSQL 또는 MySQL
- **ORM**: Spring Data JPA (Hibernate)
- **동시성 처리**: Database Locking
- **API 문서화**: Swagger (Springdoc OpenAPI)
- **테스트 프레임워크**: JUnit 5, AssertJ, Mockito
- **뷰 템플릿**: Thymeleaf

## 📌 기능 개요
### 1️⃣ 계좌 관리
- 계좌 생성 (`POST /api/account/create`)
- 특정 계좌 조회 (`GET /api/account/{id}/balance`)

### 2️⃣ 입출금 기능
- 입금 (`POST /api/account/{id}/deposit?amount=value`)
- 출금 (`POST /api/account/{id}/withdraw?amount=value`)

### 3️⃣ 동시성 제어
- 입금 및 출금 요청 동시 발생 시 정확한 잔액 유지
- 데이터 정합성을 위한 트랜잭션 관리

## 📌 API 문서 (Swagger UI)
Swagger UI를 통해 API를 테스트할 수 있습니다.
- **접속 경로**: [Swagger UI](http://localhost:8080/swagger-ui.html)

## 📌 프로젝트 실행 방법
### 1️⃣ 소스 코드 클론
```sh
git clone https://github.com/your-repo/account-service.git
cd account-service
```

### 2️⃣ Gradle 빌드 및 실행
```sh
./gradlew clean build
./gradlew bootRun
```

### 3️⃣ 테스트 실행
```sh
./gradlew test
```

## 📌 데이터베이스 설정
기본적으로 **H2 (인메모리 DB)**를 사용하며, `application.properties`에서 설정 변경 가능합니다.
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

## 📌 UI 화면 구성 (Thymeleaf)
1. **홈 화면 (`/`)**: 계좌 생성 및 관리
2. **계좌 상세 (`/account/{id}`)**: 잔액 조회, 입금, 출금

## 📌 기여 방법
1. 저장소 Fork
2. 새로운 기능 브랜치 생성 (`feature/your-feature`)
3. 변경 사항 커밋 및 PR 요청

## 📌 라이선스
이 프로젝트는 **MIT 라이선스**를 따릅니다.
