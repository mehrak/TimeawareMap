# Time aware in-memory key-value map data store

spring boot in-memory key-value data store REST service able to store data in memory and return their historical values. 

### Build and Installation Guide
Goto the checked-out src code directory and execute following commands from the command line:

**1)** The below command will compile the project and build a fat jar
* ./gradlew build

**2)** The below command will run the data store service, accessible as a REST webservice on localhost:8080
 * java -jar build/libs/TimeawareMap-0.0.1-SNAPSHOT.jar

### Usage Guide

* To store a key-value, select PUT metod and execute below URL from any REST client and provide value in the Body

http://localhost:8080/put/key/{provide-your-key}

e.g. http://localhost:8080/put/key/k1
in the Body section input:- v1

* To fetch the most recent entered value corresponding to the given key, select GET metod and execute below URL from any web browser or REST client

http://localhost:8080/get/key/{provide-your-key}

e.g. http://localhost:8080/get/key/k1

* To fetch the value corresponding to the given key at the given time or the greatest time before the given time, select GET metod and execute below URL from any web browser or REST client

http://localhost:8080/get/key/{provide-your-key}/when/{yyyy-MM-dd'T'HH:mm:ss.SSS}

e.g. http://localhost:8080/get/key/k1/when/2019-03-10T10:10:00.000

* To remove the given key and archive its values along with it temporal info, select DELET metod and execute below URL from a REST client

http://localhost:8080/remove/key/{provide-your-key}

e.g. http://localhost:8080/remove/key/k1