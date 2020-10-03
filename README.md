# Musical Pedometer

## Intro

Musical Pedometer can display the user's exercise track in real-time while the user is running, and record related exercise data in the background. At the same time, this software will perform real-time pattern recognition based on the user's exercise data, and play different music based on the recognition results. After the user finishes the exercise, the software will summarize and analyze the data collected in this period of exercise, and show the user a simple exercise report.

## Project Structure

All source code files are under `com.icycoke.musicalpedometer` package

The collected data file will be saved as `getFilesDir().getAbsolutePath() + "/data.csv"` which is `/data/data/com.icycoke.musicalpedometer/files/data.csv` as default

You can edit `CRITICAL_SPEED` field of `MainActivity` class to set a critical speed which is the standard speed to change the music (for better test the function)

Music files are under`app/src/debug/res/drawable/`

Edit `app/src/debug/res/values/google_maps_api.xml` with your own google maps api key if the default one from me is now invalid