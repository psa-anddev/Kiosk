# Kiosk

## Introduction
Kiosk is a project whose aim is to provide an example of the use of the new Android architecture components in conjunction with Kotlin, Reactive Extensions (Rx) and the principles of Uncle Bob's clean architecture. 

The application is a simple RSS reader which allows loading an RSS feed providing a URL and display the information about the feed as well as the items that are available in the feed. 

## Architecture
The project is done following the principles of clean architecture as proposed by Uncle Bob. However, due to the nature of the Android architecture components, the project is following a loose MVVM approach rather than an MVP one. 

### Core module
The core module contains the classes that are necessary for the whole application. Entities, exceptions and the gateway interfaces are in this module. This module also contains an object called KioskContext which is used as source for the gateways. By creating that class, the project doesn't need any dependency injection library.

### Feeds module
The feeds module contains all the use case related to loading feeds. At the moment of writing the document, the only use case available is the loading of the feed data. 

### Delivery module
The delivery module (called delivery-main) is responsible for handling the UI. The project is using the version 3.0 of the Android Gradle plugin and is prepared to target O, however, the source in master is targetting API 25 so that it can run all the way to API 15 (when targetting O, only O devices can run the application). 

The delivery is architected in an MVVMesque fashion. The classes the View uses to print the data are suffixed as data in order to avoid confussion with the view model which is using the view model library that Google announced in IO 2017. The view model uses Rx only to make sure that the UI operations are running on the main thread. The view receives the data through the view model which has a live data Kotlin property to provide the view with what is to be printed. 

## Tools used in the project
- Android Studio 3.0
- Android Gradle plugin 3.0
- Android build tools 26.0.0 (Release Candidate 2)
- Constraint Layout (1.1 beta)
- View model library
- Lifecycle library
- Live Data library
- RxKotlin
- RxAndroid
- Picasso
- Retrofit
- SimpleXML
- KotlinTest
- Mockito (Kotlin fork)
- MockWebServer
