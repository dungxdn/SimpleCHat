package jp.bap.traning.simplechat.presenter.addrooms;

import android.util.Log;
import java.util.List;
import jp.bap.traning.simplechat.response.AddRoomResponse;

public class AddRoomPresenter implements AddRoomView{

    private AddRoomView mAddRoomView;
    private AddRoomInteractor mAddRoomInteractor;
    private int type;

    public AddRoomPresenter(AddRoomView addRoomView) {
        this.mAddRoomInteractor = new AddRoomInteractor();
        mAddRoomView = addRoomView;
    }

    public void addroom(List<Integer> ids, int type){
        this.type = type;
        mAddRoomInteractor.addRoom(ids, type, mAddRoomView);
    }

    @Override
    public void onAddRoomSuccess(AddRoomResponse addRoomResponse) {
        addRoomResponse.getData().setType(type);
        mAddRoomView.onAddRoomSuccess(addRoomResponse);
    }

    @Override
    public void onAddRoomFail() {
        mAddRoomView.onAddRoomFail();
    }

    @Override
    public void onSuccess(AddRoomResponse result) {

    }

    @Override
    public void onError(String message, int code) {

    }
}
