# Naver Open API 를 이용한 영화 찾기 앱

### 해당 프로젝트의 설명에 관하여 블로그에 상세히 정리해 두었습니다.
- [프로젝트 설명 블로그](https://narvis2.github.io/posts/Android-Clean-Architecture/)
- [Wiki Document](https://github.com/narvis2/MovieSearchApp/wiki/%EC%95%B1-%EC%84%B8%EB%B6%80-%EC%A0%95%EB%B3%B4)

## 앱 사진
|매인 화면|키보드 포커스|검색 결과|
|---|---|---|
|<img src="https://user-images.githubusercontent.com/74344026/180112247-bf22bbf6-7806-424a-9da2-54a2200e9f7d.jpg" width="200" height="500" />|<img src="https://user-images.githubusercontent.com/74344026/180112692-a1ce70c4-5401-43f0-b5fe-0cba3700b640.jpg" width="200" height="500" />|<img src="https://user-images.githubusercontent.com/74344026/180112806-daeb0680-25b6-4ed8-bde9-51ebb760bc8a.jpg" width="200" height="500" />|

|네트워크 끊김|스크롤|영화 상세|
|---|---|---|
|<img src="https://user-images.githubusercontent.com/74344026/180112877-99546a16-9308-4c0b-a1ea-18e3ed44ba49.jpg" width="200" height="500" />|<img src="https://user-images.githubusercontent.com/74344026/180112931-d777fdbb-427f-4018-a056-eaa63d001f7b.jpg" width="200" height="500" />|<img src="https://user-images.githubusercontent.com/74344026/180112981-b64f9063-e06a-4133-b4ac-ff410450280c.jpg" width="200" height="500" />|


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

### 주요 사용 기술
* Kotlin
* MVC
* MVVM
* Clean Archtecture
* Multi-Module Project
* DataBinding
* kotlin Coroutne
* Jetpack Navigation
* AAC ViewModel
* LiveData
* Glide
* Dagger-Hilt
* Retrofit2
* Okhttp3
* Gson
* Swiperefreshlayout
* Timber
* Paging3
* Material Design
* FragmentX
