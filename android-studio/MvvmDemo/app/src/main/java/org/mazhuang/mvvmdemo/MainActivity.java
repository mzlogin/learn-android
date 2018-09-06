package org.mazhuang.mvvmdemo;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.mazhuang.mvvmdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // 结论：
        // 1. 要实现 model -> UI 的绑定（带变动通知），必须是 Observable，要么 Entity 要么 Field，且触发 notifyChange
        // 2. 要实现 UI -> model，使用 @={model} 即可
        //
        // 如果非要 ViewModel + 双向绑定，那么可以是
        // 1. ViewModel + ObservableFields
        // 2. ViewModel extends BaseObservable + @Bindable fields 手动 notify
        // 
        // 官网给出了正确示范
        // https://developer.android.google.cn/topic/libraries/data-binding/two-way

        // 双向绑定 UI <--> Model
        ObservablePojo user = new ObservablePojo();
        binding.setObservablePojo(user);

        // 单向绑定，UI -> Model 有效，Model -> UI 无效
        Pojo pojo = new Pojo();
        binding.setPojo(pojo);

        // https://developer.android.google.cn/topic/libraries/data-binding/observability
        // Any plain-old object can be used for data binding, but modifying the object doesn't automatically cause the UI to update. Data binding can be used to give your data objects the ability to notify other objects, known as listeners, when its data changes.
        // 无绑定，只有 setPojo2 这一下有效，后面再改变值就无效了，因为 String 的不可变性？
        Pojo pojo2 = new Pojo();
        pojo2.setUsername("pojo2");
        binding.setPojo2(pojo2);

        // 单向绑定，UI -> Model 有效，Model -> UI 无效
        ViewModelPojo viewModelPojo = ViewModelProviders.of(this).get(ViewModelPojo.class);
        binding.setViewModelPojo(viewModelPojo);

        // 单向绑定，UI -> Model 有效，Model -> UI 无效
        LiveDataPojo liveDataPojo = new LiveDataPojo();
        binding.setLiveDataPojo(liveDataPojo);

        // 单向绑定，UI -> Model 有效，Model -> UI 无效
        ViewModelLiveData viewModelLiveData = ViewModelProviders.of(this).get(ViewModelLiveData.class);
        binding.setViewModelLiveData(viewModelLiveData);

        // 双向绑定
        ObservableFieldPojo observableFieldPojo = new ObservableFieldPojo();
        binding.setObservableFieldPojo(observableFieldPojo);

        // 单向绑定，Model -> UI 有效，UI -> Model 无效
        ObservableFieldPojo observableFieldPojo2 = new ObservableFieldPojo();
        binding.setObservableFieldPojo2(observableFieldPojo2);
    }
}
