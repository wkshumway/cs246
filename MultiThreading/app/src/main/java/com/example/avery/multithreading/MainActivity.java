package com.example.avery.multithreading;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.transform.Result;

/**
 * @author Wellesley Shumway
 * @collab Edward Doyle
 */
public class MainActivity extends AppCompatActivity {

    ArrayAdapter<Integer> adapter;

    /**
     * onCreate starts the program
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * create starts a thread which writes a file with ten integers
     * @param view
     */
    public void create(View view){
        new Create().execute(getFilesDir() + "numbers.txt");
    }

    /**
     * load starts a thread which read from a file and displays it in a list view
     * @param view
     */
    public void load(View view) {
        new Load().execute(getFilesDir() + "numbers.txt");
    }

    /**
     * clear clears the list view and the progress bar
     * @param view
     */
    public void clear(View view) {
        adapter.clear();
        ((ProgressBar)findViewById(R.id.progressBar)).setProgress(0);
    }

    public class Create extends AsyncTask<String, Integer, Void> {

        @Override
        public void onPreExecute(){
            ((ProgressBar)findViewById(R.id.progressBar)).setProgress(0);
        }

        @Override
        protected Void doInBackground(String...params){
            try (FileWriter writer = new FileWriter(params[0])){

                for (int i = 1; i < 11; ++i) {
                    writer.write(i + System.getProperty("line.separator"));
                        Thread.sleep(250);

                    publishProgress(i * 10);
                }
            } catch (IOException e) {
                System.out.println(e);
            }
            catch (InterruptedException e) {
                //
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer...values) {
            ((ProgressBar)findViewById(R.id.progressBar)).setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result){

        }
    }


    private class Load extends AsyncTask<String, Integer, Void>{

        List<Integer> numbers = new ArrayList<>();

        @Override
        public void onPreExecute(){
            ((ProgressBar)findViewById(R.id.progressBar)).setProgress(0);
        }

        @Override
        protected Void doInBackground(String... params){
            try (Scanner fin = new Scanner(new File(params[0]))){

                int i = 1;
                while (fin.hasNextInt()) {
                    numbers.add(fin.nextInt());
                    Thread.sleep(250);
                    publishProgress(i++ * 10);
                }
            }
            catch (FileNotFoundException e) {
                System.out.println("File not found!");
            }
            catch (IOException c) {
                c.printStackTrace();
            }
            catch (InterruptedException e) {
                //
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer...values) {
            ((ProgressBar)findViewById(R.id.progressBar)).setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result){

            adapter = new ArrayAdapter<>(
                    MainActivity.this,
                    android.R.layout.simple_list_item_1,
                    numbers
            );

            ((ListView) findViewById(R.id.listView)).setAdapter(adapter);
        }

        private ListView listView;

        public void readFromFile(String filePath) {


        }

    }

}
