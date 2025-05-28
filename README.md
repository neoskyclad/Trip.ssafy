# 🗺️ Trip.ssafy - 함께 계획하는 스마트한 여행

## **📖 프로젝트 소개**

<aside>

📌 "당신의 똑똑한 여행 플래너, 지금부터 함께 시작해요”

</aside>

- **여행 준비부터 마무리까지**, 유저와 친구가 함께 일정을 계획하고 정산할 수 있는 협업형 여행 플랫폼입니다.
- **AI 추천 시스템**과 **공동 정산 기능**, **일정 관리**, **공지/메모**, **여행 후 리뷰 작성** 등 여행에 필요한 다양한 기능을 제공합니다.
- **Vue 3**, **Spring Boot**, **MySQL**을 기반으로 개발되었습니다.

## **👥 팀원**

| 이름   | 역할 | 기여 내용                         |
| ------ | ---- | --------------------------------- |
| 권대현 | 팀장 | 프로젝트 기획, 프론트 디자인 담당 |
| 조예진 | 팀원 | 백엔드 담당, 비즈니스 로직 설계   |

## **📅 개발 기간**

**개발 기간** : 2025년 3월 13일 ~ 2025년 5월 28일

## **🧰 Tech Stack**

<table>
  <tr>
    <td align="center"><img src="https://img.shields.io/badge/Vue.js-35495E?style=for-the-badge&logo=vue.js&logoColor=4FC08D" /></td>
    <td align="center"><img src="https://img.shields.io/badge/Vite-646CFF?style=for-the-badge&logo=vite&logoColor=white" /></td>
    <td align="center"><img src="https://img.shields.io/badge/TailwindCSS-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white" /></td>
    <td align="center"><img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" /></td>
    <td align="center"><img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" /></td>
  </tr>
</table>

## 🧩 주요 기능

### 👤 일반 사용자

#### 🔑 로그인 & 회원가입
- 카카오/네이버 소셜 로그인
- 일반 회원가입

#### ✈️ 여행 준비
- 여행 방 생성 / 삭제
- 친구 초대 및 관리
- 여행 일정 계획 및 수정

#### 🧠 AI 추천
- 지역 기반 추천 (공공 API 활용)
- 성향, 스타일 기반 추천
- 여행 동행자 기반 스타일 추천

#### 📅 일정 관리
- 드래그 앤 드롭으로 일정 구성
- 메모/공지사항 작성 및 공유
- 여행 일정 전체 보기

#### 💰 정산 기능
- 사용자별 지출 등록
- 인원별 금액 정산
- 룰렛 게임으로 더치페이 재미요소 제공

#### 📌 게시판 & 리뷰
- 여행 후기 작성
- 자유 게시판 (질문/홍보/정보 공유)

---

### 🏢 여행사 유저
- 게시판에 홍보글 작성 가능

---

### 🛠️ 관리자
- 공지사항 CRUD
- 사용자 정보 관리

## **🖼️ 페이지 미리 보기**

<details>
<summary>📍 여행 메인 페이지</summary>

- 메인 페이지

  ![image.png](./readme/img/image.png)

- 게시판 페이지

  ![image.png](./readme/img/image%201.png)

  - 게시글 페이지

    ![image.png](./readme/img/image%202.png)

- 광고 페이지

  ![image.png](./readme/img/image%203.png)

- AI 추천 페이지

  ![image.png](./readme/img/image%204.png)

- 마이 페이지

  ![image.png](./readme/img/image%205.png)

- 로그인 / 회원가입 모달

  ![image.png](./readme/img/image%206.png)

  ![image.png](./readme/img/image%207.png)

</details>

<details>
<summary>🛫 여행 방 모달</summary>

- 메인 페이지

  ![image.png](./readme/img/image%208.png)

- 여행 방 생성 모달

  ![image.png](./readme/img/image%209.png)

- 여행 방 페이지

  ![image.png](./readme/img/image%2010.png)

</details>

<details>
<summary>🗺️ 일정 관리</summary>

- 여행 장소 추가 모달

  ![image.png](./readme/img/image%2012.png)

- 여행 장소 메모 모달

  ![image.png](./readme/img/image%2013.png)

</details>

<details>
<summary>🤖 AI 여행지 추천</summary>

- AI 추천 모달

  ![image.png](./readme/img/image%2015.png)

  - AI 추천 후 일정 추가

    ![image.png](./readme/img/image%2016.png)

</details>

<details>
<summary>💸 정산 기능</summary>

- 여행 장소 정산 모달 (룰렛 게임)

  ![image.png](./readme/img/image%2014.png)

</details>

<details>
<summary>📝 공지/메모/리뷰</summary>

- 공지 사항 모달

  ![image.png](./readme/img/image%2017.png)

- 리뷰 모달

  ![image.png](./readme/img/image%2011.png)

</details>

## 🚀 실행 방법

1. 프로젝트 clone

   ```bash
   git clone https://lab.ssafy.com/ssafy_13th_18class/999_final/ssafy_trip_final_kwondaehyun_choyejin.git
   ```

2. 프론트엔드 실행

   - ssafy_trip_final_kwondaehyun_choyejin/TripSSAFY/frontend 로 이동
   - bash 창 열기
   - 필요한 npm 라이브러리 설치

   ```bash
   npm install
   ```

   - 프론트 엔드 서버 실행

   ```bash
   npm run dev
   ```

   - frontend/ 폴더에 .env 파일 추가
   - 아래의 내용 작성

   ```bash
   VITE_TOUR_API_KEY=1hD0aGdKIUKBE7L59PbF2naCEixas3eMV3Ba9eid3WjUulQ68Bd6E6Ld9hvRA%2Fe2zdjFSrq2Zv3D%2BnHDnnOOCQ%3D%3D
   VITE_KAKAO_JAVASCRIPT_API_KEY=5080b66a272284ba409aa468d26e7a2a
   VITE_KAKAO_REST_API_KEY=2275dd32b67a1fe42ae5af4ed7187a5f
   ```

3. 백엔드 실행

   - ssafy_trip_final_kwondaehyun_choyejin/TripSSAFY 로 이동
   - bash 창 열기
   - 백엔드 서버 실행

   ```bash
   ./gradlew bootRun
   ```

4. DB에 데이터 넣기
   - MySQL에 포트 번호 3306으로 설정
   - User: ssafy / Password: ssafy로 유저 설정
   - ssafy_trip_final_kwondaehyun_choyejin/TripSSAFY/src/main/resources 로 이동
   - default.data.sql을 MySQL에서 실행 (스키마 + 더미 데이터)
   - (선택 사항) dummy_user_10000.sql 실행
5. 브라우저 실행
   - 브라우저에서 [http://localhost:5173/](http://localhost:5173/) 로 이동

## 📂 폴더 구조

> 주요 폴더, 파일만 명시

```
📂 ssafy_trip_final_kwondaehyun_choyejin
├── 📄 README.md                         # 프로젝트 소개 및 실행 방법
└── 📂 TripSSAFY                         # 실제 프로젝트 코드가 위치한 폴더
	├── 📄 build.gradle                  # 백엔드 빌드 설정
	├── 📂 frontend                      # Vue 기반 프론트엔드
	│   ├── 📄 .env                      # 프론트엔드 환경 변수 (API KEY값)
	|   ├── 📄 package.json              # 프론트엔드 의존성 및 스크립트 정의
	│   ├── 📂 public                    # 정적 파일
	│   └── 📂 src                       # 프론트엔드 소스 코드
	└── 📂 src                           # 백엔드 Java 소스
		└── 📂 main
			├── 📂 java
			│   └── 📂 com.ssafy.tripssafy    # 백엔드 비즈니스 로직
			└── 📂 resources
				├── 📂 static                 # 정적 리소스 (이미지, JS 등)
				├── 📄 default_data.sql       # 초기 DB 설정 스크립트
				└── 📄 dummy_user_10000.sql   # 더미 사용자 데이터
```

## 🔗 기타 주소

- [Trip.ssafy 노션](https://www.notion.so/Trip-ssafy-1b5b7fa538de802099eecdaefeed007d?pvs=21)
- [Front 목업](https://www.figma.com/design/egOTTfaOelfjKuopBe1jW6/Trip.ssafy?node-id=0-1&t=upvhZ6EK2SttUtvP-1)
- [ERD](https://www.erdcloud.com/d/8BNciWWAauhfPPwKd)
