# MyTasks
<img src="/ss/icon.png" align="left"
width="160" hspace="10" vspace="10">

A Minimal Tasks planner app. Promise it will be useful.<br> 
Helps you to create tasks and reminds you <br>
about them through notifications.

<p align="left">
<a href="https://play.google.com/store/apps/details?id=com.thakurnitin2684.mytasks">
    <img alt="Get it on Google Play"
        height="80"
        src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" />
</a>  
</p>

## UI

<img src="/ss/gif1.gif" 
width="200" hspace="10" vspace="10">

<img src="/ss/gif2.gif"  
width="500" hspace="10" vspace="10">

Has a responsive UI for Landscape orientation and For Tablet screens (achieved via Fragments )

## Download

[Download](https://play.google.com/store/apps/details?id=com.thakurnitin2684.mytasks) My Tasks from playstore.

## Structure
 - <strong> MVVM with ROOM DB </strong>
<img src="/ss/structure_mt.PNG" >

## Overview
  Used in app:
  - MVVM : This project follows android recommmended MVVM architecture, having seperate layers for data, Viewmodels and UI
  - Room Db
  - Hilt: uses hilt for dependency injection
  - Kotlin
  - Fragments 
  - BroadCast (for notifications)
  - Coroutines for accessing data from Database behind the ui
  - Recycler view
  - Dialogs

## License

(see [LICENSE](LICENSE)).

MIT License

Copyright (c) 2020 Nitin Thakur

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

