# AirWatch ACC Status Checker

**THIS IS NOT SUPPORTED NOR ENDORSED BY AIRWATCH OR VMWARE SO USE AT YOUR OWN RISK**

Quick and dirty service to check ACC status as well as Directory settings.
The service simply performs a form-based login to the Console like a regular user would and then performs AJAX calls for either ACC or Directory Settings status just like the console client would and returns the JSON response.

There is currently little to no error checking and the properties required are not verified to exist to please use the /status/props endpoints to verify manually once the service is running.

## Setup

**NOTE:** Currently this requires an account with Console Administrator permissions as i have not tested with any other roles.

A couple of properties are required for this to work properly. They are:

```
aw.url.tenant=https://TENANT-URL
aw.login.user=USERNAME  
aw.login.password=PASSWORD  
logging.level.com.example=DEBUG (Optional, but recommended for initial setup)
```

This being a regular Spring Boot application we have variuos different ways we can supply the properties.

### 1. Using application.properties

Create a file called `application.properties` in the same directory as the compiled JAR file and set the properties there.

### 2. Provide the properties as part of running the JAR

When running the Spring Boot app as a standalone JAR you can provide the required properties as command line arguments.  
Example:  

`java -jar  -Daw.url.tenant=https://some.aw-tenant.com -Daw.login.user=my-admin-account -Daw.login.password=secret aw-acc-status-0.0.1-SNAPSHOT.jar`

## Usage

Once started the service runs on port 8080 (Default Spring Boot) and has 3 endpoints:

    http://ip:8080/status/acc
    http://ip:8080/status/ad
    http://ip:8080/status/props

### /status/acc  
Calling this endpoint does the same as clicking the "Test Connection" in `System > Enterprise Integration > Cloud Connector` in the AW console.

Returns a JSON response with a boolean value for the status as well as a message.  

Example response:

```javascript
{
    RedirectUrl: null,
    IsSuccess: true,
    Message: "Cloud Connector is active.",
    CustomMessage: null,
    Errors: { },
    Messages: { },
    HasView: false,
    ViewHtml: null,
    ViewUrl: null,
    IsValidationException: false,
    IsValidationWarning: false,
    ReloadPage: false,
    IsSessionExpired: false,
    Script: null,
    NextWizardUrl: null,
    PreviousWizardUrl: null,
    ShowDialog: false
}
```

It's the raw response from the AW endpoints so a lot of the data does not make sense in this context but the critical parts are `IsSuccess` and `Message` which can be used for monitoring and reporting purposes.

### /status/ad
Calling this endpoint does the same as clicking the "Test Connection" in `System > Enterprise Integration > Directory Services` in the AW console.

Returns a JSON response with a boolean value for the status as well as a message.  

Example response:

```javascript
{
    RedirectUrl: null,
    IsSuccess: true,
    Message: "Connection successful with the given Domain, Bind Username and Password.",
    CustomMessage: null,
    Errors: { },
    Messages: { },
    HasView: false,
    ViewHtml: null,
    ViewUrl: null,
    IsValidationException: false,
    IsValidationWarning: false,
    ReloadPage: false,
    IsSessionExpired: false,
    Script: null,
    NextWizardUrl: null,
    PreviousWizardUrl: null,
    ShowDialog: false
}
```

It's the raw response from the AW endpoints so a lot of the data does not make sense in this context but the critical parts are `IsSuccess` and `Message` which can be used for monitoring and reporting purposes.

### /status/props
This endpoint shows the configured properties which are mapped using `@ConfigurationProperties`.  
Use this to verify that all properties are valid.  
**NOTE:** Password is not shown here so verify that manually.

Returns a JSON response with a boolean value for the status as well as a message.  

Example response:
```javascript
{
    url: {
        tenant: "https://some.aw-tenant.com",
        login: "/AirWatch/Login",
        loginUser: "/AirWatch/login/Login/Login-User",
        cloudConnector: "/AirWatch/Settings/CloudConnector",
        accTest: "/AirWatch/Settings/TestAccConnection",
        directoryService: "/AirWatch/Settings/DirectoryServices",
        directoryTest: "/AirWatch/Settings/TestDirectoryConnection"
    },
        login: {
        user: "my-admin-account"
    }
}
```

Note that the actual endpoints required to perform the various operations are already defined in application.properties contained within the JAR file.  
These properties should not be changed however as noted in the Setup section above tenant, login and user is **required** to be provided.
