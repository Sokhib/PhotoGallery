package com.example.aspirev7.photogallery;

import android.net.Uri;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class FlickrFetchr {
    private static final String TAG = "FlickrFetchr";
    private static final String ENDPOINT = "https://api.flickr.com/services/rest";
    private static final String API_KEY = "59086d08112734f92e47e2a6ff4b9dab";
    private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
    private static final String METHOD_SEARCH= "flickr.photos.search";
    private static final String PARAM_EXTRAS = "extras";
    private static final String PARAM_TEXT = "text";
    private static final String PARAM_MIN_UPLOAD_DATE= "min_upload_date";
    private static final String XML_PHOTO = "photo";
    private static final String EXTRA_MEDIUM_URL = "url_m";
    public static String total="0";

    public static final String PREF_SEARCH_QUERY ="searchQuery" ;
    public static final String PREF_LAST_RESULT_ID = "lastResultId";

    byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {  // reads up to buffer.length() ...
                out.write(buffer, 0, bytesRead); // writes bytesRead length of data to outStream
            }
            out.close();
            return out.toByteArray();

        } finally {
            connection.disconnect();
        }
    }

    public String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public ArrayList<GalleryItem> downloadGalleryItems(String url) {
        ArrayList<GalleryItem> items = new ArrayList<>();
        try {
            String xmlString = getUrl(url);
            Log.i(TAG, "XML: " + xmlString);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));
            parseItems(items, parser);

        } catch (IOException io) {
            Log.e(TAG, "Failed to fetch items", io);
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, "Failed to parse items", xppe);
        }
        return items;

    }
    public ArrayList<GalleryItem> fetchItems(){
        String url = Uri.parse(ENDPOINT).buildUpon()
                .appendQueryParameter("method", METHOD_GET_RECENT)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter(PARAM_EXTRAS, EXTRA_MEDIUM_URL)
                .build().toString();
        return downloadGalleryItems(url);
    }

    public ArrayList<GalleryItem> search(String query) {
        String url = Uri.parse(ENDPOINT).buildUpon()
                .appendQueryParameter("method", METHOD_SEARCH)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter(PARAM_EXTRAS, EXTRA_MEDIUM_URL)
                .appendQueryParameter(PARAM_TEXT, query)
                .appendQueryParameter(PARAM_MIN_UPLOAD_DATE, "2017-10-30")
                .build().toString();

        return downloadGalleryItems(url);
    }


    void parseItems(ArrayList<GalleryItem> items, XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.next();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && XML_PHOTO.equals(parser.getName())) {
                String id = parser.getAttributeValue(null, "id");
                String caption = parser.getAttributeValue(null, "title");
                String smallUrl = parser.getAttributeValue(null, EXTRA_MEDIUM_URL);
                GalleryItem item = new GalleryItem();
                item.setId(id);
                item.setCaption(caption);
                item.setmUrl(smallUrl);
                items.add(item);
            }
            if (eventType == XmlPullParser.START_TAG && parser.getName().equals("photos")) {
                     total = parser.getAttributeValue(null, "total");
                    Log.i(TAG, "Number of items: " + total);
            }
            eventType = parser.next();
        }
    }

}
