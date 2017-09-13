package com.example.lacie.bakernotes;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ListNetActivity extends Activity {
    RequestTask requestTask;
    ListView netList;
    ArrayAdapter<String> adapter;
    String[] itemLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_net);
        netList = (ListView) findViewById(R.id.netList);
        itemLinks = new String[]{};
        requestTask = new RequestTask();
        requestTask.execute();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        netList.setAdapter(adapter);


    }

    public class RequestTask extends AsyncTask<Void, String, String> {
        public String nameItem;

        @Override
        protected String doInBackground(Void... params) {

            try {
                //to future: there is 16 rec on the page and 14 pages
                DefaultHandler handler = new DefaultHandler() {
                    public boolean name = false, flag = false;

                    public void startElement(String uri, String localName,
                                             String qName, Attributes attributes) {

                        if (attributes.getValue("class") != null) {
                            String attributeValue = attributes.getValue("class");
                            if (attributeValue.equalsIgnoreCase("card__title title")) {
                                name = true;
                            }
                        }
                        //get image and name from attributes >_<
                    }//end of startElement

                    public void characters(char[] ch, int start, int length) {
                        //get content of tags
                        if (name) {
                            // String nameItem, publishProgress(nameItem)
                            nameItem = new String(ch, start, length);
                            Log.i("tag", nameItem);
                            publishProgress(nameItem);
                            name = false;
                        }//end of characters
                    }

                };//end of Handler
                //create SAXParser with tagsoup (lib to parse html correctly)
                SAXParserImpl impl = SAXParserImpl.newInstance(null);
                //SAXParserImpl.newInstance(null).parse(
                //  new URL("https://www.edimdoma.ru/retsepty/tags/%D1%80%D0%B5%D1%86%D0%B5%D0%BF%D1%82%D1%8B%20%D0%B4%D0%BB%D1%8F%20%D1%85%D0%BB%D0%B5%D0%B1%D0%BE%D0%BF%D0%B5%D1%87%D0%BA%D0%B8")
                //          .openConnection().getInputStream(), handler
                // );

                //create url, connection
                HttpURLConnection connection = (HttpURLConnection) new URL("https://www.edimdoma.ru/retsepty/tags/%D1%80%D0%B5%D1%86%D0%B5%D0%BF%D1%82%D1%8B%20%D0%B4%D0%BB%D1%8F%20%D1%85%D0%BB%D0%B5%D0%B1%D0%BE%D0%BF%D0%B5%D1%87%D0%BA%D0%B8")
                        .openConnection();
                //get inputStream and launch parser
                try {
                    InputStream stream = connection.getInputStream();
                    impl.parse(stream, handler);

                } finally {
                    connection.disconnect();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return null;
        }


        public void onProgressUpdate(String... params) {
            super.onProgressUpdate(params);
            adapter.add(params[0]);
        }


    }
}
