# Android Humhub
A big thank you to the community humhub

## Set you app for your hum hub application 

Name of application is define in string.xml | res/values/string <br>
Change you primary url in: res/values/string (EN - AR - DE - ES - FR) or add new string for you language <br>
Chane name of link is label of item <br>
Automatically change the title in top main bar if set: setTitle(getString(R.string.name_label)); in your new menu item <br>

## Icon app 
Change png file located in mipmap/ic_laucher

## Push notification Google FCM
Change GMC json file with your configuration in Google Firebase project

## Color 
Color of application is located in res/values/colors.xml

## Set splash screen
Create new graphic with pshotoshp, modify res/mipmap/your_logo.png

## Create new intem on left menù
- Add new string on res/values/string
- Add item on menu/activity_main_drawer.xml and call the string for label, type and url
- Create new function on MainActivity (java/com.becode.humhub/MainActivity.java). Row line 397 to 444
  call new string for set type and url 
- Remember set correctly item_position for view correctly selected item on app

## Bottom bar
This function is similar to left bar <br>
to change item change string and xml/bottombar_tabs.xml <br>
if you add new item add function on MainActivity row 150 to 229 you see two type of function you set reselect function.<br>
For more function on this featured see https://github.com/roughike/BottomBar

## ADS Google
Uncommet row 263 to 298 in MainActivity and uncomment in res/layout/content_main.xml <br>
Set you Google ADS code to connect your Google ADwords in AndroidMAnifest set Api Key <br>

# Additional function 

## menù left right
you set if the burger menu show top left or top right got to: res/values/string.xml and chenge value rtl_version to true
## floating button
Enable floating button uncomment row 136 to 148 in ManActivity and uncomment res/app_bar_main.xml row 47 to 57 to enable view

# TO DO
Badge notification tab get value by webview
Real time notification

