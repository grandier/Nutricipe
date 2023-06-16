# Mobile Development
Product Based Capstone Bangkit 2023

## Screenshots
### User Interface
<img src="https://github.com/grandier/Nutricipe/blob/main/Mobile%20Development/UI1.jpg" width="1000"/>

<img src="https://github.com/grandier/Nutricipe/blob/main/Mobile%20Development/UI2.jpg" width="1000"/>

## Features
### Nutricipe App
- Splash Screen
- Authentication
- Regristration
- Home Screen with List History
- Profile Customization
- Add Photo using CameraX or Gallery
- Recommendation Recipe System
- Recipe Details
- Support Indonesian & English

## Build With
- [Kotlin](https://kotlinlang.org/)
- [Retrofit2](https://github.com/square/retrofit)
- [ViewPager2](https://developer.android.com/jetpack/androidx/releases/viewpager2)
- [Room Paging](https://developer.android.com/topic/libraries/architecture/paging/v3-network-db)
- [Room Database](https://developer.android.com/jetpack/androidx/releases/room)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [GSON](https://github.com/google/gson)
- [GSON Converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson)
- [OkHttp3](https://square.github.io/okhttp/)
- [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started)
- [Glide](https://github.com/bumptech/glide)

## Package Structure

### Nutricipe App
    .com.capstone.nutricipe         # Root Package
    │
    ├── data                        # For data handling
    │   ├── database                # to have local database
    │   ├── di                      # for di  
    |   ├── local                   # Store data entity locally     
    │   ├── paging                  # For paging3 handling
    |   |   ├── adapter             # For adapter of recyclerview
    |   |   └── photo               # for handling photo repository
    │   └── local                   # For paging3 handling
    │       ├── api                 # For api network calls
    |       └── model               # For model from api
    │
    ├── ui                          # Activity/View layer
    │   ├── custom-view             # Text validation handlers
    │   ├── fragment                # View for Onboarding
    │   ├── activity                # View for Splash and Main
    │   └── view-model              # ViewHolder for RecyclerView
    |
    └── utils                       # Utility Classes / Kotlin extensions
    
## Architecture
This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.
