AeroGear PushEE
===============

AeroGear's Connectivity Server (Java EE poc)

### Some guidance

Starting the JBoss Application Server:

```
./bin/standalone.sh -b 0.0.0.0
```

Deploying the server to JBoss AS using the jboss-as-maven-plugin:

```
mvn package jboss-as:deploy
```

***Note:** When testing functionality with the included webapp, it may be necessary to clear the browser's local storage occasionally to get accurate testing results. This is due to the client library storing channel information for later reuse after losing a connection (via refresh, browser close, internet drop, etc.) The functionality to cleanly handle this issue is in development and will be added soon thus removing the need for manual local storage cleaup. Consult your browser's docs for help with removing items from local storage.

#### Register Push App

Register a ```PushApplication```, like _Mobile HR_:

```
curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"name" : "MyApp", "description" :  "awesome app" }' http://localhost:8080/ag-push/rest/applications
```

_The response returns a **pushApplicationID** for the Push App...._

##### iOS Variant

Add an ```iOS``` variant (e.g. _HR for iOS_):
```
curl -i -H "Accept: application/json" -H "Content-type: multipart/form-data" 

  -F "certificate=@/Users/matzew/Desktop/MyCert.p12"
  -F "passphrase=TopSecret"

  -X POST http://localhost:8080/ag-push/rest/applications/{PUSH_ID}/iOS
```

**NOTE:** The above is a _multipart/form-data_, since it is required to upload the "Apple Push certificate"!

_The response returns a **variantID** for the iOS variant...._

##### Android Variant

Add an ```android``` variant (e.g. _HR for Android_):
```
curl -v -H "Accept: application/json" -H "Content-type: application/json"
  -X POST
  -d '{"googleKey" : "IDDASDASDSA"}'
  
  http://localhost:8080/ag-push/rest/applications/{PUSH_ID}/android 
```

_The response returns a **variantID** for the Android variant...._

##### SimplePush Variant

Add an ```android``` variant (e.g. _HR for Android_):
```
curl -v -H "Accept: application/json" -H "Content-type: application/json"
  -X POST
  -d '{"pushNetworkURL" : "http://localhost:7777/endpoint/"}'

  http://localhost:8080/ag-push/rest/applications/{PUSH_ID}/simplePush 
```

_The response returns a **variantID** for the SimplePush variant...._

#### Registration of an installation, on a device (iOS)

Client-side example for how to register an installation:

```ObjectiveC
- (void)application:(UIApplication*)application
  didRegisterForRemoteNotificationsWithDeviceToken:(NSData*)deviceToken
{
AGDeviceRegistration *registration = 
  [[AGDeviceRegistration alloc] initWithServerURL:[NSURL URLWithString:@"http://server/ag-push/"]];

[registration registerWithClientInfo:^(id<AGClientDeviceInformation> clientInfo) {

  // apply the desired info:
  clientInfo.token = @"2c948a843e6404dd013e79d82e5a0009";
  clientInfo.mobileVariantID = @"2c948a843e6404dd013e79d82e5a0009";
  clientInfo.deviceType = @"iPhone";
  clientInfo.operatingSystem = @"iOS";
  clientInfo.osVersion = @"6.1.3";
  clientInfo.alias = @"mister@xyz.com";

} success:^(id responseObject) {
  NSLog(@"\n%@", responseObject);
} failure:^(NSError *error) {
  NSLog(@"\nERROR");
}];
}
```

For _iOS_ the above sample was based on the **EARLY** version of our [iOS Push SDK](https://github.com/matzew/ag-client-push-sdk)

#### Registration of an installation, for an Android device:

For now, perform HTTP from Android to register the "MobileVariantInstance".
Here is a _CURL_ example for how to perform the:

```
curl -v -H "Accept: application/json" -H "Content-type: application/json" 
   -H "ag-mobile-variant: {id}"
   -X POST
   -d '{
      "deviceToken" : "someTokenString", 
      "deviceType" : "ANDROID", 
      "mobileOperatingSystem" : "android", 
      "osVersion" : "4.0.1"
    }'

http://localhost:8080/ag-push/rest/registry/device 
```

#### Registration of an installation, for a SimplePush client:

CURL example for how to register a connected SimplePush client:


```
curl -v -H "Accept: application/json" -H "Content-type: application/json"
    -H "ag-mobile-variant: {VARIAN_ID}"
    -X POST
    -d '{
       "category" : "broadcast",
       "deviceToken" : "4a81527d-6967-40bb-ac56-755e8cbfb579"
     }'
http://localhost:8080/ag-push/rest/registry/device 
```

The ```category``` matches the (logical) name of the channel; The ```deviceToken``` matches the ```channelID``` from the SimplePushServer.

**NOTE:** For _JavaScript_, an SDK is currently being worked on (see [AG-JS](https://github.com/aerogear/aerogear-js/blob/Notifier-sockjs/src/unified-push/aerogear.unifiedpush.js))

### Sender

#### Broadcast Send

Send broadcast push message to ALL mobile apps of a certain Push APP......:

```
curl -v -H "Accept: application/json" -H "Content-type: application/json" 
   -X POST
   -d '{"key":"value", "alert":"HELLO!", "sound":"default", "badge":7,
       "simple-push":"version=123"}'

http://localhost:8080/ag-push/rest/sender/broadcast/{PushApplicationID}
```

**TODO:** Add link to message format spec (once published)

#### Selected Send

To send a message (version) notification to a selected list of Channels, issue the following command:

```
curl -v -H "Accept: application/json" -H "Content-type: application/json" 
   -X POST

   -d '{
      "alias" : ["user@account.com", "jay@redhat.org", ....],
  
      "deviceType" : ["iPad", "AndroidTablet"],
  
      "message": {"key":"value", "key2":"other value", "alert":"HELLO!",
        "simple-push": { "SomeCategory":"version=123", "anotherCategory":"version=456"}
	  }
   }'

http://localhost:8080/ag-push/rest/sender/selected/{PushApplicationID} 
```

**TODO:** Add link to message format spec (once published)


## More details

Concepts and ideas are also being developed...:

See:
https://gist.github.com/matzew/69d33a18d4fac9fdedd4

REST APIs

* Registry: https://gist.github.com/matzew/2da6fc349a4aaf629bce
* Sender: https://gist.github.com/matzew/b21c1404cc093825f0fb
