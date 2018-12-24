# 경북대 연합 프로젝트
### 사물인터넷 기반 차량 인포테인먼트 제어 서비스 응용 개발

<br /><br />
## 프로젝트 설계
![Alt text](/Image/통신.png)
### Mobile(Android)
#### : Android Studio(Kotlin), RxJava, Dagger, Apache commons-net(FTP), etc
<br />
### Server
#### : Python 3.7.0, Windows 
<br />

### IVI(Raspberry)
#### : Python 3.5, Raspbian, ffpyplayer, opencv-python, pyftpdlib, XRDP(Debug)

## 기능명세
### 미디어 재생/일시정지/중지 기능
### 사진 보여주기 기능
### 모바일에 있는 미디어 파일을 IVI에 보내기 기능
### 메시지 큐 관리 (다대다 연결 고려)



<br />
## 애로사항
### Mobile(Android)
#### Rxjava, Dagger2 라이브러리 공부와 병행하여 진행
#### 소켓을 이용한 서버와 통신 연결 구조
#### FTP 라이브러리 사용
<br />

### 서버 
#### 다대다 통신 관련 Thread Pool 생성 문제
#### 비정상 연결 종료 시 버그
<br />

### IVI
#### OpenCV, ffpyplayer 라즈베리파이 설정 문제 
#### 모듈화 문제
#### 메모리 할당 문제 (동영상, 사진)

![Alt text](/Image/실행화면.JPG)
