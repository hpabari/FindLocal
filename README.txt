
**INSTALLATION**
There are 2 ways to install/run the app.

(1)
Software/device needed to install and run:
Android Phone

Instructions:
1. Find the app's APK file (app-release.apk) in \FindLocal\app\release\
2. Copy this file into your device by connected the phone to the PC
3. Click on the app file in the copied location on the phone
4. There will be a warning against installing third party apps
5. Click Install - Another warning may appear saying the app is from an unknown developer and may be unsafe
6. Click Install Anyway
7. The app will be installed and can be opened


(2)
Software/device needed to install and run:
Android Studio
Android Phone (if running on real device)

Instructions for running on a real device:
1. Open Android Studio and import project
2. Enable USB debugging on phone:
	- Open setting app on phone
	- Select "System"
	- Select "About Phone" at the bottom
	- Tap "Build number" 7 times
	- Return to previous screen and scroll to the and tap "Developer options"
	- Scroll to find "USB debugging" and enable
3. Connect phone to machine with USB cable
4. In Android studio select the app from the run/debug config dropdown menu
5. Select your device from the target device dropdown menu
6. Click Run - App will build and run on the device

Instructions for running on an Emulator (stimulated Android device on PC):
1. In Android Studio, create an Android Virtual Device(AVD)
	Images and instructions for creating an AVD - https://developer.android.com/studio/run/managing-avds#createavd
2. Select the app from the run/debug config dropdown menu
3. Select the AVD from the target device dropdown menu
4. Click Run - App will build and run on the AVD and emulator started

These instructions can be found with more details and images on: https://developer.android.com/training/basics/firstapp/running-app




**FILES SUBMITTED**
- There are libraries, external libraries and generated files in the project.
- Files that I have written/edited/added are:

\FindLocal\app\src\androidTest\java\com\t\findlocal\activities\DetailsActivityTest.java
\FindLocal\app\src\androidTest\java\com\t\findlocal\activities\SearchActivityTest.java
\FindLocal\app\src\main\AndroidManifest.xml
\FindLocal\app\src\main\java\com\t\findlocal\activities\AdvertiseFormActivity.java
\FindLocal\app\src\main\java\com\t\findlocal\activities\CategoriesFragment.java
\FindLocal\app\src\main\java\com\t\findlocal\activities\CategoriesResults.java
\FindLocal\app\src\main\java\com\t\findlocal\activities\DetailsActivity.java
\FindLocal\app\src\main\java\com\t\findlocal\activities\MainActivity.java
\FindLocal\app\src\main\java\com\t\findlocal\activities\MapsActivity.java
\FindLocal\app\src\main\java\com\t\findlocal\activities\SearchActivity.java
\FindLocal\app\src\main\java\com\t\findlocal\activities\TextSearchFragment.java
\FindLocal\app\src\main\java\com\t\findlocal\adapters\CategoryAdapter.java
\FindLocal\app\src\main\java\com\t\findlocal\adapters\FragmentPagerAdapter.java
\FindLocal\app\src\main\java\com\t\findlocal\adapters\ResultsAdapter.java
\FindLocal\app\src\main\java\com\t\findlocal\models\Business.java
\FindLocal\app\src\main\java\com\t\findlocal\models\Category.java
\FindLocal\app\src\main\res\drawable\address_icon.png
\FindLocal\app\src\main\res\drawable\email_icon.png
\FindLocal\app\src\main\res\drawable\find_local_image.png
\FindLocal\app\src\main\res\drawable\listview_shape.xml
\FindLocal\app\src\main\res\drawable\list_item_image.xml
\FindLocal\app\src\main\res\drawable\phone_icon.png
\FindLocal\app\src\main\res\drawable\results_linearview_border.xml
\FindLocal\app\src\main\res\drawable\user_button.xml
\FindLocal\app\src\main\res\drawable\website_icon.png
\FindLocal\app\src\main\res\drawable-v24\ic_launcher_foreground.xml
\FindLocal\app\src\main\res\layout\activity_advertise_form.xml
\FindLocal\app\src\main\res\layout\activity_category_result.xml
\FindLocal\app\src\main\res\layout\activity_details.xml
\FindLocal\app\src\main\res\layout\activity_main.xml
\FindLocal\app\src\main\res\layout\activity_maps.xml
\FindLocal\app\src\main\res\layout\activity_search.xml
\FindLocal\app\src\main\res\layout\fragment_categories.xml
\FindLocal\app\src\main\res\layout\fragment_text_search.xml
\FindLocal\app\src\main\res\layout\list_item_categories.xml
\FindLocal\app\src\main\res\layout\list_item_search_result.xml
\FindLocal\app\src\main\res\mipmap-hdpi\ic_launcher.png
\FindLocal\app\src\main\res\mipmap-hdpi\ic_launcher_round.png
\FindLocal\app\src\main\res\mipmap-mdpi\ic_launcher.png
\FindLocal\app\src\main\res\mipmap-mdpi\ic_launcher_round.png
\FindLocal\app\src\main\res\mipmap-xhdpi\calculator.png
\FindLocal\app\src\main\res\mipmap-xhdpi\camera.png
\FindLocal\app\src\main\res\mipmap-xhdpi\dryer.png
\FindLocal\app\src\main\res\mipmap-xhdpi\find_local.png
\FindLocal\app\src\main\res\mipmap-xhdpi\ic_launcher.png
\FindLocal\app\src\main\res\mipmap-xhdpi\ic_launcher_round.png
\FindLocal\app\src\main\res\mipmap-xhdpi\makeupartist.png
\FindLocal\app\src\main\res\mipmap-xhdpi\painter.png
\FindLocal\app\src\main\res\mipmap-xhdpi\search.png
\FindLocal\app\src\main\res\mipmap-xhdpi\sew.png
\FindLocal\app\src\main\res\mipmap-xhdpi\tutor.png
\FindLocal\app\src\main\res\mipmap-xhdpi\window.png
\FindLocal\app\src\main\res\mipmap-xxhdpi\ic_launcher.png
\FindLocal\app\src\main\res\mipmap-xxhdpi\ic_launcher_round.png
\FindLocal\app\src\main\res\mipmap-xxhdpi\search.png
\FindLocal\app\src\main\res\values\colors.xml
\FindLocal\app\src\main\res\values\dimens.xml
\FindLocal\app\src\main\res\values\strings.xml
\FindLocal\app\src\main\res\values\styles.xml
\FindLocal\app\src\test\java\com\t\findlocal\SearchActivityUnitTests.java
\FindLocal\build.gradle
\FindLocal\app\build.gradle
\FindLocal\app\google-services.json











