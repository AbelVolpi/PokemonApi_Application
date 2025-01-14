# Pokemon-api-application

This project is a native Android app developed to simulate a Pokedex, consuming
the [PokeApi](https://pokeapi.co/). It's an application developed following the best practices in
Android Development.

## UI & Features

<table border="1" style="border-collapse: collapse">
  <tr>
    <td align="top" valign="center">
      <b>Pagination</b>
    </td>
    <td align="center" valign="center">
      <b>
        Shared Element Transition<br>
        (Views & XML)
      </b>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img align="center" width="140" src="images/pagination.gif" />
    </td>
    <td align="center">
      <img align="center" width="140" src="images/shared_element_animation.gif" />
    </td>
  </tr>
</table>

## 🛠 Architecture

This app was developed using the [MVVM](https://developer.android.com/topic/architecture)
architecture pattern and following
the [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
concepts.

Currently, the structure of the system is as follows:

<img src="images/architecture.png" width="600" style="border-radius: 15px;">

## 📚 Libraries & Tools

- [Fragments](https://developer.android.com/guide/fragments) & [Navigation Component](https://developer.android.com/guide/navigation) - UI and Navigation
- [Retrofit](https://square.github.io/retrofit/), [Okhttp](https://square.github.io/okhttp/) & [Moshi](https://github.com/square/moshi) - Network calls
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Dependency injection
- [Glide](https://github.com/bumptech/glide) - Load images in the app
- [Mockk](https://mockk.io/ANDROID.html) - Unit tests

## 📝 Commits pattern

In this project it's being used the [Karma](https://karma-runner.github.io/6.4/dev/git-commit-msg.html)
commit convention to keep project readability.

## 🔍 Static Code Analysis

It's being used [Ktlint](https://github.com/pinterest/ktlint) to standardize Kotlin code, to check,
run:

```
./gradlew ktlintCheck
```

And to format, run:

```
./gradlew ktlintFormat
```

All rules you can find in `.editorconfig` file.

## 🏗️ How Build/Run the project?

First, build the project with the following command:

```
./gradlew assembleDebug
```

Then, install it on a connected device:

```
./gradlew installDebug
```

## 🚂 CI/CD
🚧 Work in progress

## 🧪 Unit Tests & Code Coverage
🚧 Work in progress
