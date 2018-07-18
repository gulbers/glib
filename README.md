# glib
Custom view and utilities for Android apps


Glib provide custom view for TextView, Button, EditText, Expand GridView, Expand ListView, ScrollListView, Touch ImageView, and Loading Dialog

Utilities on this lib is:
- HttpClient post with json, form, or multipart body.
- Location to get current position and also translated to city, province, etc
- Telephony info to get IMEI or sim card operator name
- Other utils

### Installation
Add this depencencies on your app build.gradle
```
dependencies {
  compile 'net.gulbers:glib:1.0'
}
```

### How to use
Check [WIKI](https://github.com/gulbers/glib/wiki) to see the full description

### Features
#### Custom View
For XML view usage:
- net.gulbers.glib.view.GTextView 
- net.gulbers.glib.view.GEditText
- net.gulbers.glib.view.GExpandGridView
- net.gulbers.glib.view.GExpandListView
- net.gulbers.glib.view.GScrollListView
- net.gulbers.glib.view.GButton
- net.gulbers.glib.view.GTouchImageView
For loading dialog:
- net.gulbers.glib.view.GLoadingDialog

#### Utilities
- net.gulbers.glib.utils.Debug
- net.gulbers.glib.utils.HttpHelper
- net.gulbers.glib.utils.LocationTracker
- net.gulbers.glib.utils.TelephonyInfo
- net.gulbers.glib.utils.MUtils

### Developed By
- Sony Gultom [sony.gultom@gmail.com](mailto:sony.gultom@gmail.com)

### Licence
```
Copyright 2018 Sony Gultom

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
