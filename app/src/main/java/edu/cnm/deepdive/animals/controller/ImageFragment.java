package edu.cnm.deepdive.animals.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.animals.R;
import edu.cnm.deepdive.animals.model.Animal;
import edu.cnm.deepdive.animals.model.service.AnimalService;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageFragment extends Fragment {

  private WebView contentView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_image, container, false);
    setupWebView(root);
    return root;
  }

  private void setupWebView(View root) {
    contentView = root.findViewById(R.id.content_view);
    contentView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        // TODO Update view to indicate that load is complete.
      }
    });
    WebSettings settings = contentView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setSupportZoom(true);
    settings.setBuiltInZoomControls(true);
    settings.setDisplayZoomControls(false);
    settings.setUseWideViewPort(true);
    settings.setLoadWithOverviewMode(true);
    new RetrieveImageTask().execute();
  }

  /**
   * AsyncTask enables proper and easy * use of the UI thread. This class allows performing *
   * background operations and publishing results * on the UI thread without having to manipulate *
   * threads and/or handlers. An asynchronous task is * defined by a computation that runs on a *
   * background thread and whose result is published on the * UI thread.
   */
  private class RetrieveImageTask extends AsyncTask<List<Animal>, Void, List<Animal>> {

    AnimalService animalService;

    protected void onPreExecute() {
      super.onPreExecute();
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .create();
      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl("https://us-central1-apis-4674e.cloudfunctions.net")
          .addConverterFactory(GsonConverterFactory.create(gson))
          .build();
      animalService = retrofit.create(AnimalService.class);
    }

    @Override
    protected List<Animal> doInBackground(List<Animal>... lists) {
      List<Animal> animals = null;
      try {
        Response<List<Animal>> response = animalService
            .getAnimals("0c42474fa81fa47077bdee783fe9ce75f2536cd9").execute();
        if (response.isSuccessful()) {
          animals = response.body();
        }
      } catch (
          IOException e) {
        Log.e("AnimalService", e.getMessage(), e);
      }
      return animals;
    }

    @Override
    protected void onPostExecute(List<Animal> animals) {
      super.onPostExecute(animals);
      String url = animals.get(17).getUrl();
      contentView.loadUrl(url);
    }
  }
}
