## Android Humhub

# Set you app for your hum hub application 

Name of application is define in string.xml | res/values/string
Change you primary url in: res/values/string (EN - AR - DE - ES - FR) or add new string for you language
Chane name of link is label of item 

# Push notification
Change GMC json file with your configuration in Google Firebase project

# Color 
Color of application is located in res/values/colors.xml

# Set splash screen
Create new graphic with pshotoshp, modify res/mipmap/your_logo.png

# Create new intem on left menù
- Add new string on res/values/string
- Add item on menu/activity_main_drawer.xml and call the string for label, type and url
- Create new function on MainActivity (java/com.becode.humhub/MainActivity.java). Row line 397 to 444
call new string for set type and url 
Remember set correctly item_position for view correctly selected item on app

# Bottom bar
This function is similar to left bar
to change item change string and xml/bottombar_tabs.xml
if you add new item add function on MainActivity row 150 to 229 you see two type of function you set reselect function.

# ADS Google
Uncommet row 263 to 298 in MainActivity and uncomment in res/layout/content_main.xml 
Set you Google ADS code to connect your Google ADwords in AndroidMAnifest set Api Key





