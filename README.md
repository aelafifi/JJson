# JJson
JSON library for Java (android) - built over org.json.JSONObject/JSONArray

# Usage

### Example 1
```java
JObject user = new JObject();

// let's set some properties
user.set("username", "ahmedsalah94");
user.set("profile.firstname", "Ahmed");
user.set("profile.lastname", "El-Afifi");
user.set("profile.emails.0", "ahmed.s.elafifi@gmail.com");
// user = {
//     "username": "ahmedsalah94",
//     "profile": {
//         "firstname": "Ahmed",
//         "lastname": "El-Afifi",
//         "emails": ["ahmed.s.elafifi@gmail.com"]
//     }
// }

// let's get some data
if (user.get("username").equals("ahmedsalah94") && user.get("password").equals("123456")) {
    System.out.println("Hello!");
}
// user.get("password") is null, and null doesn't has equals method!!!!!
// let's correct it

if (user.get("username", "").equals("ahmedsalah94") && user.get("password", "").equals("123456")) {
    System.out.println("Hello!");
}
// now it works

if (user.getNN("username").equals("ahmedsalah94") && user.getNN("password").equals("123456")) {
    System.out.println("Hello!");
}
// this also works
// getNN (getNonNull) returns NULL "Object" which has equals method :)
```

### Example 2
```java
JObject user = new JObject();

String username = user.get("username", "default").toString();
// user = {}

String username = user.get("username", "default", true).toString();
// NOW: user = {"username": "default"}
// third-parameter will create it if it's not exists

JObject formData = new JObject();
formData.set("username", "ahmedsalah94");
formData.set("password", "123456");
String url = "http://.../login?" + formData.getQueryString();
// url = "http://.../login?username=ahmedsalah94&password=123456"
```
