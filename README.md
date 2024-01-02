<div align="center">
	<img src="/images/riotLogo.png" alt="Logo" width="80" height="80">

  <h3>LOL 조합 추천 프로젝트</h3>
  <br />
  <p>
    <a href="http://13.124.127.226:3000/">배포 링크</a>
  </p>
</div>
<br />

### 목차
1. [프로젝트 소개](#프로젝트-소개)
    - [주요 라이브러리](#주요-라이브러리)
    - [배포 아키텍쳐](#배포-아키텍쳐)
2. [사용법 및 기능소개](#사용법-및-기능소개)
3. [개발 로드맵](#개발-로드맵)
4. [세부 내용](#세부-내용)
    - [ERD](#ERD)
    - [Sequence Diagram](#sequence-diagram)
    - [Swagger UI](#Swagger-UI)
    - [figma 설계 초안](#figma-설계-초안)
    - [selenium 적용 파트](#selenium-적용-파트)
<br />

## 프로젝트 소개
<br />

![메인화면](/images/mainPage.png)

5 대 5로 전투하는 AOS 게임인 리그오브레전드에 관련하여 각각의 챔피언 승률을 보여주는 서비스는 있으나 챔피언 5명의 조합 별 승률을 보여주는 서비스가 없어서 이 프로젝트를 시작하게 되었고, 통계 데이터를 기반으로 승률을 끌어올리자는 취지로 이 서비스를 만들게 되었습니다.
<br />
<br />
사용자가 원하는 조합을 설정한 뒤 실력에 따라 나누어둔 등급(모든 티어, 챌린저, 그랜드마스터, 마스터, 다이아몬드) 별로 설정해서 볼 수 있고,
<br />
조합 승률 순, 조합 선택 횟수 순으로 정렬되어 있는 정보를 선택해서 얻을 수 있습니다.

<br />

얻을 수 있는 정보
- 승률이 높은 조합
- 픽 횟수가 많은 조합
- 조합 운영 팁
- 팀 조합에 어울리는 챔피언

<br />
<br />

### 주요 라이브러리

- ![spring](https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
- ![springSecurity](https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
- ![jwt](https://img.shields.io/badge/jwt-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
- ![redis](https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
- ![selenium](https://img.shields.io/badge/selenium-43B02A?style=for-the-badge&logo=selenium&logoColor=white)
- ![node.js](https://img.shields.io/badge/node.js-339933?style=for-the-badge&logo=node.js&logoColor=white)

<br />

### 배포 아키텍쳐

![배포아키텍쳐](/images/architecture.png)

EC2에 프론트엔드 서버와 백엔드 서버를 분리해서 REST API를 적용하였습니다.
<br />
LocalServer에 riot사이트 API를 이용하여 스프링 스케줄러를 적용해서 지속적으로 EC2 DB 데이터를 최신으로 업데이트합니다.

<br />

## 사용법 및 기능소개

![메인화면](/images/mainPage.gif)
<br />
원하는 챔피언을 검색해서 찾거나 기존의 초상화 테이블에서 선택합니다.
<br />
<br />
챔피언 초상화를 클릭 또는 드래그해서 조합 테이블로 이동시키면 그 조합의 통계를 출력합니다.
<br />
<br />
통계 정보를 티어 순, 픽 횟수 순, 승률 순으로 정렬해서 원하는 데이터를 얻을 수 있습니다.
<br />
<br />
<br />
세부 기능 소개는 링크의 문서를 참조해 주세요. <a href="https://zircon-moat-99e.notion.site/59948773811147a9b44c639823b64394?pvs=4">Documentation</a>

<br />

## 개발 로드맵
- [X] 게시판 CRUD 기능 구현
- [X] springSecurity, JWT 구현
- [X] 조합 관련 기능 구현
- [X] 미생성 게시물 url로 접근 불가 로직 추가
- [ ] 관리자 권한으로 챔피언 추가 기능 구현
- [ ] 게시물 페이지 일정 이상 넘어가면 화살표로 구현

<br />

## 세부 내용

### Sequence Diagram
<a href="https://zircon-moat-99e.notion.site/726533b2de3a43c283b5da3c62b81c3c?pvs=4">Documentation</a>
<br />

### Swagger UI
<a href="http://13.124.127.226:8081/swagger-ui/">Documentation</a>
<br />
