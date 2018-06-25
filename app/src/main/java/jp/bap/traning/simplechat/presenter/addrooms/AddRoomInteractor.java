package jp.bap.traning.simplechat.presenter.addrooms;

import android.util.Log;

import java.util.List;

import jp.bap.traning.simplechat.response.AddRoomResponse;
import jp.bap.traning.simplechat.service.ApiClient;
import jp.bap.traning.simplechat.utils.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRoomInteractor {

    public AddRoomInteractor() {
    }

    public void addRoom(List<Integer> ids, int type, AddRoomView callback) {

        Call<AddRoomResponse> mCallUser = ApiClient.getService().createRoom(ids, type);
        mCallUser.enqueue(new Callback<AddRoomResponse>() {
            @Override
            public void onResponse(Call<AddRoomResponse> call, Response<AddRoomResponse> response) {
                if (response.body().getStatus() == Common.STATUS_SUCCESS) {
                    callback.onSuccess(response.body());
                    Log.e("addRoom", "success");
                } else {
                    callback.onError(response.body().getMessage(), response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<AddRoomResponse> call, Throwable t) {
                callback.onFailure();
                Log.e("addRoom", "fail");
            }
        });
    }
}
