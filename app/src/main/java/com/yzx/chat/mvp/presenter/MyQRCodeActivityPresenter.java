package com.yzx.chat.mvp.presenter;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.yzx.chat.R;
import com.yzx.chat.base.BaseResponseCallback;
import com.yzx.chat.bean.GroupBean;
import com.yzx.chat.bean.UserBean;
import com.yzx.chat.mvp.contract.MyQRCodeActivityContract;
import com.yzx.chat.network.api.Group.GetTempGroupID;
import com.yzx.chat.network.api.Group.GroupApi;
import com.yzx.chat.network.api.JsonResponse;
import com.yzx.chat.network.api.user.GetTempUserID;
import com.yzx.chat.network.api.user.UserApi;
import com.yzx.chat.network.chat.IMClient;
import com.yzx.chat.network.framework.Call;
import com.yzx.chat.tool.ApiHelper;
import com.yzx.chat.tool.DirectoryManager;
import com.yzx.chat.util.AndroidUtil;
import com.yzx.chat.util.AsyncUtil;
import com.yzx.chat.util.BitmapUtil;
import com.yzx.chat.util.MD5Util;

/**
 * Created by YZX on 2018年02月26日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */


public class MyQRCodeActivityPresenter implements MyQRCodeActivityContract.Presenter {

    private MyQRCodeActivityContract.View mMyQRCodeActivityView;
    private Call<JsonResponse<GetTempUserID>> mGetTempUserIDCall;
    private Call<JsonResponse<GetTempGroupID>> mGetTempGroupIDCall;
    private UserApi mUserApi;
    private GroupApi mGroupApi;

    private boolean isUpdating;

    @Override
    public void attachView(MyQRCodeActivityContract.View view) {
        mMyQRCodeActivityView = view;
        mUserApi = (UserApi) ApiHelper.getProxyInstance(UserApi.class);
        mGroupApi = (GroupApi) ApiHelper.getProxyInstance(GroupApi.class);
    }

    @Override
    public void detachView() {
        AsyncUtil.cancelCall(mGetTempUserIDCall);
        AsyncUtil.cancelCall(mGetTempGroupIDCall);
        mMyQRCodeActivityView = null;
    }

    @Override
    public UserBean getUserInfo() {
        return IMClient.getInstance().userManager().getUser();
    }

    @Override
    public GroupBean getGroupInfo(String groupID) {
        if (TextUtils.isEmpty(groupID)) {
            return null;
        }
        return IMClient.getInstance().groupManager().getGroup(groupID);
    }

    @Override
    public void updateUserQRCode() {
        if (isUpdating) {
            return;
        }
        isUpdating = true;
        AsyncUtil.cancelCall(mGetTempUserIDCall);
        mGetTempUserIDCall = mUserApi.getTempUserID();
        mGetTempUserIDCall.setResponseCallback(new BaseResponseCallback<GetTempUserID>() {
            @Override
            protected void onSuccess(GetTempUserID response) {
                isUpdating = false;
                mMyQRCodeActivityView.showQRCode(response.getTempUserID());
            }

            @Override
            protected void onFailure(String message) {
                isUpdating = false;
                mMyQRCodeActivityView.showHint(message);
            }
        });
        sHttpExecutor.submit(mGetTempUserIDCall);
    }

    @Override
    public void saveQRCodeToLocal(Bitmap bitmap,String id) {
        String savePath = BitmapUtil.saveBitmapToJPEG(bitmap, DirectoryManager.getUserImagePath(), MD5Util.encrypt16(id));
        if (TextUtils.isEmpty(savePath)) {
            mMyQRCodeActivityView.showHint(AndroidUtil.getString(R.string.MyQRCodeActivity_SaveQRCodeFail));
        } else {
            mMyQRCodeActivityView.showHint(AndroidUtil.getString(R.string.MyQRCodeActivity_SaveQRCodeSuccess) + savePath);
        }
    }

    @Override
    public void updateGroupQRCode(String groupID) {
        if (isUpdating || TextUtils.isEmpty(groupID)) {
            return;
        }
        isUpdating = true;
        AsyncUtil.cancelCall(mGetTempGroupIDCall);
        mGetTempGroupIDCall = mGroupApi.getTempGroupID(groupID);
        mGetTempGroupIDCall.setResponseCallback(new BaseResponseCallback<GetTempGroupID>() {
            @Override
            protected void onSuccess(GetTempGroupID response) {
                isUpdating = false;
                mMyQRCodeActivityView.showQRCode(response.getTempGroupID());
            }

            @Override
            protected void onFailure(String message) {
                isUpdating = false;
                mMyQRCodeActivityView.showHint(message);
            }
        });
        sHttpExecutor.submit(mGetTempGroupIDCall);
    }
}
