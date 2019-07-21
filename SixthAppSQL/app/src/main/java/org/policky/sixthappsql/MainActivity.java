package org.policky.sixthappsql;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ItemAdapter itemAdapter;
    Context thisContext;
    ListView myListView;
    TextView progressTextView;
    Map<String,Double> fruitMap = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        myListView = (ListView) findViewById(R.id.myListView);
        progressTextView = (TextView) findViewById(R.id.progressTextView);
        thisContext = this;

        progressTextView.setText("");

        Button button = (Button) findViewById(R.id.getDataButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetData retrieveData = new GetData();
                retrieveData.execute("");
            }
        });
    }

    private class GetData extends AsyncTask<String,String,String>{

        String message = ""; // displayed in progressTextView
        static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        static final String DB_URL = "jdbc:mysql://" +
                DBStrings.DATABASE_URL + "/" +
                DBStrings.DATABASE_NAME;

        @Override
        protected void onPreExecute() {
            progressTextView.setText("Connecting to database.");
        }

        @Override
        protected String doInBackground(String... strings) {

            Connection connection = null;
            Statement statement = null;



            try{
                Class.forName(JDBC_DRIVER);
                connection = DriverManager.getConnection(DB_URL, DBStrings.USERNAME,DBStrings.PASSWORD);

                statement = connection.createStatement();
                String sql = "SELECT * FROM fruits";
                ResultSet resultSet = statement.executeQuery(sql);

                while(resultSet.next()){
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");

                    fruitMap.put(name,price);
                }

                message = "Process completed.";

                resultSet.close();
                statement.close();
                connection.close();

            }
            catch (SQLException connErr){
                message = connErr.getMessage();
            } catch (ClassNotFoundException e) {
                message = e.getMessage();
                //e.printStackTrace();
            }
            finally {
                try{
                    if(statement != null){
                        statement.close();
                    }
                }
                catch (SQLException se){
                    System.out.println("Process failed.");
                    se.printStackTrace();
                }

                try{
                    if(connection != null){
                        connection.close();
                    }
                }
                catch (SQLException ce){
                    System.out.println("Process failed.");
                    ce.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(String msg) {
            progressTextView.setText(message);

            if(fruitMap.size() > 0){
                itemAdapter = new ItemAdapter(thisContext, fruitMap);
                myListView.setAdapter(itemAdapter);
            }
        }
    }

}
