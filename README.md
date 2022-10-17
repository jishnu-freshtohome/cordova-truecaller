---
title: Truecaller Authentication
description: Authenticate users with Truecaller. 
---

# cordova-plugin-truecaller

__Supported Platforms__

- Android

__Generate Fingerprint/SHA1__
On Linux

    keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

On Windows

    keytool -list -v -keystore "c:\users\your_user_name\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android 

You can get the fingerprint/SHA1 from the command output

## Generate partnerkey

* Visit <https://developer.truecaller.com/>, create an account and login.
- Navigate to your dashboard and add application.
- Add your app name, package name and your fingerprint to add the application.
- Copy the generated App Key, which is the partner key.

## Installation

- For installation use the original repo or keep a local copy of this repo and install

    cordova plugin add <path>/cordova-plugin-truecaller --variable PARTNER_KEY='<Your truecaller partnerkey>'

---

## truecaller

### truecaller.check(successCallback, errorCallback)

Can be used to find if the user has truecaller application installed on their device.

### truecaller.verify(options, successCallback, errorCallback)

Displays the truecaller dialog to authenticate the user.

__Example__  

```js
var options = {
  consentMode: 'Bottomsheet',
  loginTextPrefix: 'To continue',
  loginTextSuffix: 'Please verify mobile no',
  ctaTextPrefix: 'Use',
  buttonShape: 'Rounded',
  footerType: 'Later',
  consentTitle: 'Verify',
  sdkOptions: 'With otp',
};
cordova.plugins.truecaller.check(
    function (success) {
      cordova.plugins.truecaller.verify(
        options,
        function (profile) {
          console.log('Got profile', profile);
        },
        function (error) {
          console.error(error);
        }
      );
    },
    function (error) {
      if (error === 'Not installed')
        console.error('Truecaller is not installed on user device.');
      else console.error(error);
    }
  );
```

<a name="module_camera.cleanup"></a>

### options.consentMode : <code>string</code>
__Kind__: consentMode property of <code>[Options](#)</code>
__Default__: Bottomsheet
__Alowed Values__

| Value | Description |
| --- | --- |
| Bottomsheet | Displays a bottomsheet |
| Fullscreen | Displays a fullscreen modal |
| Popup | Displays a popup |

### options.buttonColor : <code>hex color code</code>
__Kind__: buttonColor property of <code>[Options](#)</code>
__Default__: #2979FF

### options.buttonTextColor : <code>hex color code</code>
__Kind__: buttonTextColor property of <code>[Options](#)</code>
__Default__: #FFFFFF

### options.loginTextPrefix : <code>string</code>
__Kind__: loginTextPrefix property of <code>[Options](#)</code>
__Default__: To continue
__Description__: Will display the same value.
__Alowed Values__

| Value |
| --- |
| To get started |
| To continue |
| To complete your order |
| To checkout |
| To proceed with your booking |
| To continue with your booking |
| To get details |
| To view more |
| To continue reading |
| For new updates |
| To get updates |
| To subscribe |
| To subscribe and get updates |

### options.loginTextSuffix : <code>string</code>
__Kind__: loginTextSuffix property of <code>[Options](#)</code>
__Default__: To continue
__Description__: Will display the same value.
__Alowed Values__

| Value |
| --- |
| Please verify mobile no |
| Please login |
| Please signup |
| Please login signup |
| Please register |
| Please sign in |

### options.ctaTextPrefix : <code>string</code>
__Kind__: ctaTextPrefix property of <code>[Options](#)</code>
__Default__: Proceed with
__Description__: Will display the same value.
__Alowed Values__

| Value |
| --- |
| Use |
| Continue with |
| Proceed with |

### options.buttonShape : <code>string</code>
__Kind__: buttonShape property of <code>[Options](#)</code>
__Default__: Rounded
__Description__: The shape of button.
__Alowed Values__

| Value |
| --- |
| Rectangle |
| Rounded |

### options.footerType : <code>string</code>
__Kind__: footerType property of <code>[Options](#)</code>
__Default__: Later
__Description__: Will display the same value in footer.
__Alowed Values__

| Value |
| --- |
| Later |
| Manually |
| Another method |

### options.consentTitle : <code>string</code>
__Kind__: consentTitle property of <code>[Options](#)</code>
__Default__: Verify
__Description__: Will display the same value.
__Alowed Values__

| Value |
| --- |
| Login |
| Sign up |
| Sign in |
| Verify |
| Register |
| Get started |

### options.sdkOptions : <code>string</code>
__Kind__: sdkOptions property of <code>[Options](#)</code>
__Default__: With otp
__Alowed Values__

| Value |
| --- |
| With otp |
| Without otp |

### truecaller.clearSdk()

Clears the trueScope.

__Example__  

```js
cordova.plugins.truecaller.clearSdk()
```
