# Palette: 서로의 취향을 담는 AI 데이트 코스 추천 플랫폼

AI 기반으로 연인 맞춤 데이트 코스를 추천하는 웹 플랫폼입니다. 단순 인기순이나 리뷰순이 아닌, 실제 사용자 리뷰에서 **대표 메뉴를 추출하고**, **연인 각각의 취향을 반영한 개인화된 추천**을 제공합니다.

<br>

## 📷 서비스

<img width="1242" alt="image" src="https://github.com/user-attachments/assets/d3e138ac-f17a-4dba-a6a2-94b52ec56208" />

<br>

## 🔗 링크

- **발표 자료**: [발표 자료 PDF](https://file.notion.so/f/f/85b3e012-7142-4881-b986-9fff69aebab2/f9c2a0dc-b811-47b1-8b27-7ec1906eaec2/6%E1%84%8C%E1%85%A9_%E1%84%83%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%8B%E1%85%AF%E1%86%AB_%E1%84%8E%E1%85%AC%E1%84%8C%E1%85%A9%E1%86%BC%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD_compressed.pdf?table=block&id=c60ff570-27b0-4148-ad5d-0298a56aad95&spaceId=85b3e012-7142-4881-b986-9fff69aebab2&expirationTimestamp=1752084000000&signature=rfgtJYvbSmngSKqu4xXD1Jv8mDXLFj7AAjhdDkbryOI&downloadName=6%E1%84%8C%E1%85%A9_%E1%84%83%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%8B%E1%85%AF%E1%86%AB_%E1%84%8E%E1%85%AC%E1%84%8C%E1%85%A9%E1%86%BC%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD_compressed.pdf)
- **시연 영상**: [유튜브 시연 영상](https://youtu.be/gwcOXF_W0Bg?si=wDKJZ1nzdiL0YKOJ)
- **Notion 정리**: [Notion 프로젝트](https://zosungwoo.notion.site/Palette-AI-fc2f32b2901145d0b69b5a2bbbf3cde9)

<br>

## 👥 팀 구성

- PM 1명
- **백엔드 1명 (본인 전담)**
- AI ∙ Data 2명
- 프론트엔드 1명

<br>

## 🛠 사용 기술

<div align="center">
  
  <img src="https://img.shields.io/badge/Java21-000000?style=flat-square&logo=java&color=F40D12">
  <img src="https://img.shields.io/badge/-Gradle-02303A?style=flat-square&logo=gradle&logoColor=white" height="22"/>
  <img src="https://img.shields.io/badge/-Spring%20Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white" height="22"/> 
  <img src="https://img.shields.io/badge/-Spring%20Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white" height="22"/> 
  <img src="https://img.shields.io/badge/-Spring%20Batch-6DB33F?style=flat-square&logo=springbatch&logoColor=white" height="22"/>
  <img src="https://img.shields.io/badge/JWT-black?style=flat-square&logo=JSON%20web%20tokens">
  <br>
  <img src="https://img.shields.io/badge/-AWS-232F3E?style=flat-square&logo=amazonaws&logoColor=white" height="22"/> 
  <img src="https://img.shields.io/badge/-Docker-2496ED?style=flat-square&logo=docker&logoColor=white" height="22"/> 
  <img src="https://img.shields.io/badge/-Nginx-009639?style=flat-square&logo=nginx&logoColor=white" height="22"/> 
  <img src="https://img.shields.io/badge/-MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white" height="22"/>
  <img src="https://img.shields.io/badge/-MongoDB-47A248?style=flat-square&logo=mongodb&logoColor=white" height="22"/>
  <img src="https://img.shields.io/badge/-RabbitMQ-FF6600?style=flat-square&logo=rabbitmq&logoColor=white" height="22"/>
  <img src="https://img.shields.io/badge/-GitHub%20Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white" height="22"/>
  <img src="https://img.shields.io/badge/Swagger-0?style=flat-square&logo=Swagger&logoColor=white&color=%2385EA2D">
</div>

<br>

## 🏢 아키텍쳐

![단락 텍스트 (2)](https://github.com/user-attachments/assets/b57bd60d-5bb3-4fb3-ae47-119f2bd69523)

<br>

## 📐 ERD

![ERD](https://github.com/user-attachments/assets/c1f107f7-3e4b-4120-9249-4fd9b20443e2)

<br>


## ✅ 주요 구현 기능

### 🔧 개발 & 인프라 구축
- 요구사항 분석 → ERD 설계 및 API 명세서 작성
- Spring Boot 기반 REST API 서버 개발 (도메인 패키지 구조)
- GitHub Actions + Docker 기반 CI/CD 파이프라인 구성
- Docker Compose 기반 멀티 모듈 분리 및 NGINX를 활용한 **Blue-Green 무중단 배포** 구현
- API 서버 -  EC2 Auto Scaling Group에 포함되어 인스턴스 자동 확장/축소

### 🔐 인증 및 보안
- Spring Security + JWT 인증/인가 구현
- Access Token은 LocalStorage, Refresh Token은 쿠키에 저장
- **Token Rotation** 적용
- HTTPS 통신을 위한 ELB + ACM 구성

### 🤝 외부 연동 및 통신
- WebClient를 통한 추천 시스템과 API 통신 (레스토랑 추천, 유사 유저 리뷰 등)
- RabbitMQ 기반 비동기 통신 (유저 임베딩 생성 및 업데이트)
- MySQL과 MongoDB, 그리고 Amazon S3(+ CloudFront)와 연동

### 🗂️ 도메인 기능
- 회원가입 / 로그인
- 멤버, 커플 연결, 레스토랑 추천, 리뷰, 메뉴, 카테고리, 데이트코스 API 구현

<br>

## 📈 성능 개선 및 기술 적용 사례

- Lambda@Edge + CloudFront로 이미지 로딩 시간 약 **82% 개선**
- RabbitMQ 기반 유저 임베딩 업데이트 비동기 처리로 API 응답 시간 **89% 개선**
- MongoDB + Spring Batch로 데이터 적재 자동화, 크롤러의 도메인 의존성 해소
- 지역 컬럼 인덱스 적용으로 지역 조건부 쿼리 성능 **86% 향상**

<br>

## 🏆 수상

- **2024 세종대학교 제17회 창의설계경진대회 대상 수상**
