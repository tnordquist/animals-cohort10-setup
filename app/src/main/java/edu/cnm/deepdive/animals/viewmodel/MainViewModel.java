package edu.cnm.deepdive.animals.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.animals.BuildConfig;
import edu.cnm.deepdive.animals.model.Animal;
import edu.cnm.deepdive.animals.service.AnimalService;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Architecture Components provides ViewModel helper class for the UI controller that is responsible
 * for preparing data for the UI. ViewModel objects are automatically retained during configuration
 * changes so that data they hold is immediately available to the next activity or fragment
 * instance.
 */
@SuppressWarnings("ALL")
public class MainViewModel extends AndroidViewModel {

  private MutableLiveData<List<Animal>> animals;
  private MutableLiveData<Throwable> throwable;
  AnimalService animalService;

  public MainViewModel(@NonNull Application application) {
    super(application);
    animalService = AnimalService.getInstance();
    animals = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    loadAnimals();
  }

  public LiveData<List<Animal>> getAnimals() {
    return animals;
  }
  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  private void loadAnimals() {
    animalService.getAnimals(BuildConfig.CLIENT_KEY)
        .subscribeOn(Schedulers.io())
        .subscribe((new Consumer<List<Animal>>() {
          @Override
          public void accept(List<Animal> animals1) throws Exception {
            MainViewModel.this.animals.postValue(animals1);
          }
        }), new Consumer<Throwable>() {
          @Override
          public void accept(Throwable throwable1) throws Exception {
            MainViewModel.this.throwable.postValue(throwable1);
          }
        });
  }
}
