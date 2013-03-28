slf4j-android-logger  [![Build Status](https://travis-ci.org/PSDev/slf4j-android-logger.png?branch=master)](https://travis-ci.org/PSDev/slf4j-android-logger)
==============

This library is based on the official slf4j-android implementation but does different logging.
It does not use the classname as the log tag for but instead appends it after the message. It also logs the line number and current thread.
Configuration of the log tag is done through a properties file (`logger.properties`).

Configuration
-------------

Create a `logger.properties` file in the classpath and configure it.

### Available properties:

**Name:** `de.psdev.slf4j.android.logger.logTag`  
**Description:** The log tag to use when logging to Androids logger. Maximum 23 chars!

**Name:** `de.psdev.slf4j.android.logger.defaultLogLevel`  
**Values:** `TRACE`|`DEBUG`|`INFO`|`WARN`|`ERROR`

**Example**:
```properties
de.psdev.slf4j.android.logger.logTag=MyAwesomeApp
de.psdev.slf4j.android.logger.defaultLogLevel=DEBUG
```

Download (not yet available in central)
--------

Download [the latest JAR][1] or grab via Maven:

```xml
<dependency>
  <groupId>de.psdev.slf4j-android-logger</groupId>
  <artifactId>slf4j-android-logger</artifactId>
  <version>1.0.0</version>
</dependency>
```


Credits
-------

This library is based upon the official [slf4j-android][2] implementation.


License
-------

    Copyright 2013 Philip Schiffer

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: http://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=de.psdev&a=slf4j-android-logger&v=LATEST
[2]: https://github.com/qos-ch/slf4j/tree/master/slf4j-android
