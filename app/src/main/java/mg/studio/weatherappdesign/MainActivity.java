package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

    //获取时间日期  get the date
    private static String mWay;
    private static String mYear;
    private static String mMonth;
    private static String mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button refresh_but = (Button) findViewById(R.id.update);
        final TextView xingqi = (TextView) findViewById(R.id.xingqi);
        final TextView myDate = (TextView) findViewById(R.id.tv_date);
        final TextView myCity = (TextView) findViewById(R.id.tv_location);
        final TextView wendu = (TextView) findViewById(R.id.temperature_of_the_day);

        refresh_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                mYear = String.valueOf(c.get(Calendar.YEAR));                 // 获取当前年份     year
                mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);           // 获取当前月份     month
                mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));          // 获取当前月份的日期号码      day
                mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
                if ("1".equals(mWay)) {
                    xingqi.setText("SUNDAY");
                } else if ("2".equals(mWay)) {
                    xingqi.setText("MONDAY");
                } else if ("3".equals(mWay)) {
                    xingqi.setText("TUESDAY");
                } else if ("4".equals(mWay)) {
                    xingqi.setText("WENDNESDAY");
                } else if ("5".equals(mWay)) {
                    xingqi.setText("THURSDAY");
                } else if ("6".equals(mWay)) {
                    xingqi.setText("FRIDAY");
                } else if ("7".equals(mWay)) {
                    xingqi.setText("SATURDAY");
                }
                myDate.setText(mMonth + "/" + mDay + "/" + mYear);   //03/03/2018
                myCity.setText("重庆");
                getWeatherDatafromNet("101040100");

                //wendu.setText("19");
                Toast.makeText(MainActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void btnClick(View view) {
        new DownloadUpdate().execute();
    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "http://mpianatra.com/Courses/info.txt";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String temperature) {
            //Update the temperature displayed
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(temperature);
        }
    }


    private void getWeatherDatafromNet(String cityCode)
    {

        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityCode;
        Log.d("Address:",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final char a,b;
                final TextView wendu = (TextView) findViewById(R.id.temperature_of_the_day);
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(address);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    //urlConnection.setConnectTimeout(8000);
                    //urlConnection.setReadTimeout(8000);
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer sb = new StringBuffer();
                    String str;
                    str=reader.readLine();
                    a = str.charAt(96);
                    b = str.charAt(97);
                    Log.d("读取", String.valueOf(a)+String.valueOf(b));

                    wendu.setText(String.valueOf(a)+String.valueOf(b));
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
