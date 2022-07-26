# Naver Open API 를 이용한 영화 찾기 앱

### 해당 프로젝트의 설명에 관하여 블로그에 상세히 정리해 두었습니다.
- [프로젝트 설명 블로그](https://narvis2.github.io/posts/Android-Clean-Architecture/)
- [Wiki Document](https://github.com/narvis2/MovieSearchApp/wiki/%EC%95%B1-%EC%84%B8%EB%B6%80-%EC%A0%95%EB%B3%B4)

## 앱 사진
|메인 화면 / 검색 결과 없음|키보드 포커스|검색 결과|빈값 검색|
|---|---|---|---|
|<img src="https://user-images.githubusercontent.com/74344026/180112247-bf22bbf6-7806-424a-9da2-54a2200e9f7d.jpg" />|<img src="https://user-images.githubusercontent.com/74344026/180112692-a1ce70c4-5401-43f0-b5fe-0cba3700b640.jpg" />|<img src="https://user-images.githubusercontent.com/74344026/180112806-daeb0680-25b6-4ed8-bde9-51ebb760bc8a.jpg" />|<img src="https://user-images.githubusercontent.com/74344026/180337230-fda0ba08-8e82-4807-9108-b938685b10a9.jpg" />

|네트워크 끊김|스크롤|영화 상세|뒤로 가기|
|---|---|---|---|
|<img src="https://user-images.githubusercontent.com/74344026/180112877-99546a16-9308-4c0b-a1ea-18e3ed44ba49.jpg" />|<img src="https://user-images.githubusercontent.com/74344026/180112931-d777fdbb-427f-4018-a056-eaa63d001f7b.jpg" />|<img src="https://user-images.githubusercontent.com/74344026/180112981-b64f9063-e06a-4133-b4ac-ff410450280c.jpg" />|<img src="https://user-images.githubusercontent.com/74344026/180337338-40490d4e-1575-4fe9-b5cd-c9dce6f26092.jpg" />

## 기존 폴더 구조
```sh
app
├─ ..
├─ src
│ ├─ main  
│ │ ├─ java  
│ │ │ ├─ com.example.moviesearchapp  
│ │ │ │ ├─ base
│ │ │ │ ├─ di  
│ │ │ │ ├─ utils  
│ │ │ │ ├─ view  
│ │ │ │ ├─ MovieApplication.kt
│ │ │ │ res  
data
├─ ..
├─ src
│ ├─ main
│ │ ├─ java  
│ │ │ ├─ com.example.data
│ │ │ │ ├─ api
│ │ │ │ ├─ mapper  
│ │ │ │ ├─ model  
│ │ │ │ ├─ repository
domain
├─ ..
├─ src
│ ├─ main
│ │ ├─ java  
│ │ │ ├─ com.example.domain
│ │ │ │ ├─ model
│ │ │ │ ├─ repository
│ │ │ │ ├─ usecase      
```

## 주요 사용 기술
* Kotlin
* MVC
* MVVM
* Clean Archtecture
* Multi-Module Project
* DataBinding
* kotlin Coroutine
* Jetpack Navigation
* AAC ViewModel
* LiveData
* Glide
* Dagger-Hilt
* Retrofit2
* Okhttp3
* Gson
* SwiperefreshLayout
* Timber
* Paging3
* Material Design
* FragmentX
