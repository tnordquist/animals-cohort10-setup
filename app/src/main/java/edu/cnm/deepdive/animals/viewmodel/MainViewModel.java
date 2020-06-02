package edu.cnm.deepdive.animals.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.animals.BuildConfig;
import edu.cnm.deepdive.animals.model.Animal;
import edu.cnm.deepdive.animals.service.AnimalService;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Architecture Components provides ViewModel helper class for the UI controller that is responsible
 * for preparing data for the UI. ViewModel objects are automatically retained during configuration
 * changes so that data they hold is immediately available to the next activity or fragment
 * instance.
 */
@SuppressWarnings("ALL")
public class MainViewModel extends AndroidViewModel {

  private MutableLiveData<List<Animal>> animals;

  public MainViewModel(@NonNull Application application) {
    super(application);
    loadAnimals();
    animals = new MutableLiveData<>();
  }

  public LiveData<List<Animal>> getAnimals() {
    return animals;
  }

  /**
   * AsyncTask enables proper and easy use of the UI thread. This class allows performing background
   * operations and publishing results on the UI thread without having to manipulate threads and/or
   * handlers. An asynchronous task is defined by a computation that runs on a background thread and
   * whose result is published on the UI thread.
   */
  private void loadAnimals() {
    new AsyncTask<List<Animal>, Void, List<Animal>>() {

      AnimalService animalService;
      List<Animal> animals;

      @Override
      protected void onPreExecute() {
        super.onPreExecute();
        Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
        animalService = retrofit.create(AnimalService.class);
      }

      @Override
      protected final List<Animal> doInBackground(List<Animal>... lists) {
        try {
          Response<List<Animal>> response = animalService.getAnimals(BuildConfig.CLIENT_KEY)
              .execute();
          if (response.isSuccessful()) {
            animals = response.body();
            MainViewModel.this.animals.postValue(animals);
          }
        } catch (IOException e) {
          Log.e("AnimalService", e.getMessage(), e);
        }
        return animals;
      }

      @Override
      protected void onPostExecute(List<Animal> animals) {
        super.onPostExecute(animals);
      }
    }.execute();
  }


}
