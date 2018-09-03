package com.zenglb.framework.modulea.mvp.handylife;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zenglb.framework.modulea.R;
import com.zenglb.framework.modulea.http.AModuleApiService;
import com.zenglb.framework.modulea.http.result.AnyLifeResult;
import com.zlb.http.result.StaffMsg;
import com.zlb.base.BaseStatusFragment;
import com.zlb.commontips.ErrorCallback;
import com.zlb.dagger.scope.ActivityScope;
import com.zlb.httplib.BaseObserver;
import com.zlb.httplib.rxUtils.SwitchSchedulers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * fragment contain the content lists with 2 different types of items.
 * 代码仅供阅读，不可用于任何形式的发布，传播，拷贝或其他的商业用途
 * Created by zlb on 2018/3/23.
 */
@ActivityScope
public class AnyLifeFragment extends BaseStatusFragment implements AnyLifeContract.HandyLifeView {
    private static final int perPageSize = 20;
    private static final String ARG_DATA_TYPE = "data_type";      //data type {cityguide,shop,eat}

    private int page = 1;   //假设我们的Page 都是从1开始
    private RecyclerView mRecyclerView = null;
    private AnyLifeAdapter handyLifeAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<AnyLifeResult> handyLifeResultBeans = new ArrayList<>();

    @Inject
    AnyLifePresenter mPresenter;  //dagger

    @Inject
    AModuleApiService aModuleApiService;

    /**
     * 显示数据
     *
     * @param tabsResultBeansTemp
     */
    @Override
    public void showHandyLifeData(List<AnyLifeResult> tabsResultBeansTemp) {
        page++; //next page for simple
        mBaseLoadService.showSuccess();          //ui status
        handyLifeResultBeans.addAll(tabsResultBeansTemp);
        handyLifeAdapter.notifyDataSetChanged(); // update ui

        handyLifeAdapter.setEnableLoadMore(true); //can load more
        mSwipeRefreshLayout.setRefreshing(false); //as u see
        if (tabsResultBeansTemp.size() > perPageSize - 1) {
            handyLifeAdapter.loadMoreComplete();
        } else {
            handyLifeAdapter.loadMoreEnd();  //no more data
        }
    }


    /**
     * get Http Data failed.
     *
     * @param code
     * @param message
     */
    @Override
    public void getHandyLifeDataFailed(int code, String message) {
        if (page == 1) {
            handyLifeAdapter.notifyDataSetChanged();  //data clear ,then update ui
        }
        handyLifeAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setRefreshing(false);
        if (handyLifeResultBeans.size() == 0) {
            // should switch(code)
            mBaseLoadService.showCallback(ErrorCallback.class);  //for easy
        } else {
            handyLifeAdapter.loadMoreFail(); //load more failed
        }
    }

    @Inject
    public AnyLifeFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AnyLifeFragment newInstance(String dataType) {
        AnyLifeFragment fragment = new AnyLifeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATA_TYPE, dataType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int onCreateFragmentView() {
        getStaffMsgTest();

        return R.layout.fragment_any_life;
    }







    /**
     * 组件化ApiService test
     *
     */
    private void getStaffMsgTest() {

        aModuleApiService.getStaffMsgInaModule()
                .compose(SwitchSchedulers.applySchedulers())
                .subscribe(new BaseObserver<StaffMsg>(getActivity(),false) {
                    @Override
                    public void onSuccess(StaffMsg staffMsg) {
                        Log.e("StaffMsg",staffMsg.toString());
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                    }
                });

    }





    /**
     * refresh data
     */
    private void refresh() {
        page = 1;
        handyLifeResultBeans.clear();
        mSwipeRefreshLayout.setRefreshing(true);
        handyLifeAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        mBaseLoadService.showSuccess();

        mPresenter.getHandyLifeData(getArguments().getString(ARG_DATA_TYPE), page);
    }


    /**
     * click common ui empty error status ui will invoke onHttpReload
     *
     * @param v
     */
    @Override
    protected void onHttpReload(View v) {
        refresh();
    }

    /**
     * Views init
     *
     * @param rootView
     */
    @Override
    public void initViews(View rootView) {
        handyLifeAdapter = new AnyLifeAdapter(getContext(), handyLifeResultBeans);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setAdapter(handyLifeAdapter);

        handyLifeAdapter.setOnLoadMoreListener(() ->
                        mPresenter.getHandyLifeData(getArguments().getString(ARG_DATA_TYPE), page)
                , mRecyclerView);

        // 当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested方法
        handyLifeAdapter.setPreLoadNumber(3);
        handyLifeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setTag("tag"+getArguments().getString(ARG_DATA_TYPE));
        mSwipeRefreshLayout.setOnRefreshListener(() -> refresh());
        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Bind view to the presenter
        mPresenter.takeView(this);
    }

//    @Override
//    public void onPause() {
//        mPresenter.dropView();
//        super.onPause();
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

}
