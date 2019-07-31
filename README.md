## Movies Decades , SWVL Challange

The past decade held a lot of movies, some left a mark and some were just a set of 24-60 pictures per second.This app is a ​Master - Detail Application​ to showcase those movies and the signature they left behind.

## Challenge description
- Load movies list from a JSON file.
- Ability to search movies by name .
- Categorize the movies search results by year and each year category has the top 5 rated movies within.
- Show movies details with the following details :-
    ● Movie Title.
    ● Movie Year.
    ● Movie Genres (if any).
    ● Movie Cast (if any).
    ● A two column list of pictures fetched from flickr that matches the movie title as the search
      query.
- Using Flicker api to retrieve photos that matches the movie title in a 2 column grid 

## Screenshot
![png](https://github.com/Ahmed3Elshaer/MoviesDecade/master/art/1.png)
![png](https://github.com/Ahmed3Elshaer/MoviesDecade/master/art/2.png)
![png](https://github.com/Ahmed3Elshaer/MoviesDecade/master/art/3.png)


## Specifications
- Caching for movies from JSON file to optmize loading time.
- Realtime search and sorting speed.
- Clean design
- descriptive git history.
- Unit test.
- Partly include comments.
- Reactive and Functional code
## Languages, libraries and tools used

 * [Kotlin](https://kotlinlang.org/)
 * [androidX libraries](https://developer.android.com/jetpack/androidx)
 * [Android LifeCycle](https://developer.android.com/topic/libraries/architecture)
 * [Glide]
 * [Room]
 * [Android Pagination Library]
 * [Retrofit2]
 * [Android Test Support Library]
 * [Lottie]
 * [Dagger2](https://github.com/google/dagger)
 * [RxJava](https://github.com/ReactiveX/RxJava)
 * [RxKotlin](https://github.com/ReactiveX/RxKotlin)
 * [RxAndroid](https://github.com/ReactiveX/RxAndroid)
 * [Mockito Kotlin](https://github.com/nhaarman/mockito-kotlin/)
 
 
## Requirements
- min SDK 21

## Installation

-Just clone the app and import to Android Studio.
``https://github.com/Ahmed3Elshaer/MoviesDecade.git``

- Make sure that your are in the ``Prod`` product flavour.
- For running the unit tests switch to ``Mock`` product flavour.

## Usage

- For testing the app there is an APK build [HERE!](https://github.com/Ahmed3Elshaer/MoviesDecade/raw/master/app.apk) or in  the repo main page that you can directly download and install.

## Implementation

* In this project I'm using [MVI Kotlin](https://github.com/oldergod/android-architecture)
as an application architecture adopted from the architecture blueprints sample with these design patterns in mind:-
- Repository Pattern
- Facade Pattern
- Singleton
- Observables
- Adapters

* Using Dagger for dependency injection that will make testing easier and our make code 
cleaner and more readable.
* Using Retrofit library to handle the APIs stuff.
* Using Room for caching movies
* Using Pagination Library to smooth the load of data in the inital and search state
* Using Lottie for great animation in loading Images and empty state design.
* Using product flavour ``mock`` for testing and ``prod`` for production

 * Creating a Uni-direction flow adopting the MVI concepts with Intents, Actions, Processors, ViewStates with the beauty of ViewModels to handle lifeCycle and the state of the app.
 
 The MVI architecture embraces reactive and functional programming. The two main components of this architecture, the View and the ViewModel can be seen as functions, taking an input and emiting outputs to each other. The View takes input from the ViewModel and emit back intents. The ViewModel takes input from the View and emit back view states. This means the View has only one entry point to forward data to the ViewModel and vice-versa, the ViewModel only has one way to pass information to the View.
This is reflected in their API. For instance in the main screen, The View has only two exposed methods:
```
sealed class MoviesIntents : MviIntent {
    object InitIntent : MoviesIntents()
    data class SearchIntent(val query: String) : MoviesIntents()
}
```
A View will a) emit its intents to a ViewModel, and b) subscribes to this ViewModel in order to receive states needed to render its own UI.

```
data class MoviesViewStates(
    val isLoading: Boolean,
    val isSearch: Boolean,
    val movies: PagedList<Any>?,
    val error: Throwable?
) : MviViewState {
    companion object {
        fun idle(): MoviesViewStates {
            return MoviesViewStates(
                isLoading = false,
                error = null,
                movies = null,
                isSearch = false
            )
        }
    }

}
```
A ViewModel will a) process the intents of the View, and b) emit a view state back so the View can reflect the change, if any.

## User First 

The MVI architecture sees the user as part of the data flow, a functional component taking input from the previous one and emitting event to the next. The user receives an input―the screen from the application―and outputs back events (touch, click, scroll...). On Android, the input/output of the UI is at the same place; either physically as everything goes through the screen or in the program: I/O inside the activity or the fragment. Including the User to seperate the input of the view from its output helps keeping the code healty.


## Immutability
Data immutability is embraced to help keeping the logic simple. Immutability means that we do not need to manage data being mutated in other methods, in other threads, etc; because we are sure the data cannot change. Data immutability is implemented with Kotlin's data class.

## ViewModel LifeCycle
The ViewModel should outlive the View on configuration changes. For instance, on rotation, the Activity gets destroyed and recreated but your ViewModel should not be affected by this. If the ViewModel was to be recreated as well, all the ongoing tasks and cached latest ViewState would be lost.
We use the Architecture Components library to instantiate our ViewModel in order to easily have its lifecycle correctly managed.


## License
MIT License
```
Copyright (c) [2019] [Ahmed Elshaer]
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

