package ltd.xiamenwelivetechnologyco.navlayout;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
* author:wedfrend
* email:wedfrend@yeah.net
* create:2017/8/29 11:11
* desc: 在手机端加载过程中，会出现很多的状况
 *
 *      1：加载中
 *      2：数据出错
 *      3：网络异常
 *      4：重新加载
 *      为了方便的统一管理，所以自定义View来进行相应的加载状态方法
 *
*/

public class NavFrameLayout extends FrameLayout {

    private FrameLayout wholeFrameLayout;
    //布局中的子页面
    private View mContentView;
    private LayoutInflater layoutInflater;
    //不同的状态
    private View statusView;

    private EnumContent.statusPage statusPage = EnumContent.statusPage.LOADING;

    private final int loadingLayoutId = R.layout.z_loading_layout;
    private final int dateErrorLayoutId = R.layout.z_daterror_layout;
    private final int netErrorLayoutId = R.layout.z_neterror_layout;

    public NavFrameLayout(Context context) {
        super(context,null);
    }

    public NavFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public NavFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitDefault();
    }

    //初始化
    public void InitDefault(){
        wholeFrameLayout = new FrameLayout(getContext());
        layoutInflater = LayoutInflater.from(getContext());
        wholeFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        addView(wholeFrameLayout);
        setViewLoading(loadingLayoutId);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount() != 2){
            //抛出运行时异常
            throw new RuntimeException(NavFrameLayout.class.getSimpleName() + "必须有且只有一个子控件");
        }
        mContentView = getChildAt(1);
        mContentView.setVisibility(View.GONE);
    }

    /**
     * 正在加载
     * @param layoutResID
     */
    protected SwipeRefreshLayout swipeRefreshLayout;
    public void setViewLoading(@LayoutRes int layoutResID){
        statusPage = EnumContent.statusPage.LOADING;
        if (statusView != null && statusView.getParent() != null) {
            ViewGroup parent = (ViewGroup) statusView.getParent();
            parent.removeView(statusView);
        }
        statusView = layoutInflater.inflate(layoutResID,null);
        if (statusView != null) {
            statusView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            wholeFrameLayout.addView(statusView);
        }
        statusView.setVisibility(View.VISIBLE);
        swipeRefreshLayout = ((SwipeRefreshLayout) statusView.findViewById(R.id.refresh_loading));
        swipeRefreshLayout.setProgressViewOffset(true, 50, 100);
        //设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        //下拉刷新调用的一个接口
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent,R.color.colorPrimaryDark);
        swipeRefreshLayout.post(() ->
            swipeRefreshLayout.setRefreshing(true)
        );
    }

    /**
     * 设置界面正常加载的情况下
     */
    public void setNormal(){
        if(statusView.getVisibility() == View.VISIBLE){
            //异常界面隐藏
            statusView.setVisibility(View.GONE);
            //正常界面显示
        }
        mContentView.setVisibility(View.VISIBLE);
        //处理不同的情况
        if(statusPage.equals(EnumContent.statusPage.NONE)){
        }
        //正在加载中
        if(statusPage.equals(EnumContent.statusPage.LOADING)){
            //加载界面显示
            swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
            return;
        }
        //数据出现异常
        if(statusPage.equals(EnumContent.statusPage.DATEERROR)){

            return;
        }
        //网络出现异常
        if(statusPage.equals(EnumContent.statusPage.NETERROR)){
            return;
        }
    }

    /**
     * 界面出现异常
     */
    public void setException(EnumContent.statusPage status){
        statusPage = status;
        if(statusView.getVisibility() == View.GONE){
            //正常界面隐藏
            mContentView.setVisibility(View.GONE);
            //异常界面显示
            statusView.setVisibility(View.VISIBLE);
        }
        //处理不同的情况
        if(status.equals(EnumContent.statusPage.NONE)){

        }
        //正在加载中
        if(status.equals(EnumContent.statusPage.LOADING)){
            //加载界面显示
            setViewLoading(loadingLayoutId);
            return;
        }
        //数据出现异常
        if(status.equals(EnumContent.statusPage.DATEERROR)){
            setViewDateError(dateErrorLayoutId);
            return;
        }
        //网络出现异常
        if(status.equals(EnumContent.statusPage.NETERROR)){
            setViewNetError(netErrorLayoutId);
            return;
        }
    }


    /**
     * 数据异常
     * @param layoutResID
     */
    public void setViewDateError(@LayoutRes int layoutResID){

        if (statusView != null && statusView.getParent() != null) {
            ViewGroup parent = (ViewGroup) statusView.getParent();
            parent.removeView(statusView);
        }
        statusView = layoutInflater.inflate(layoutResID,null);
        if (statusView != null) {
            statusView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            wholeFrameLayout.addView(statusView);
        }
        //下面要做一些数据异常显示的事

    }

    /**
     * 网络 异常
     * @param layoutResID
     */
    public void setViewNetError(@LayoutRes int layoutResID){

        if (statusView != null && statusView.getParent() != null) {
            ViewGroup parent = (ViewGroup) statusView.getParent();
            parent.removeView(statusView);
        }
        statusView = layoutInflater.inflate(layoutResID,null);
        if (statusView != null) {
            statusView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            wholeFrameLayout.addView(statusView);
        }
        statusView.findViewById(R.id.tv_reloading).setOnClickListener(v ->{
            setException(EnumContent.statusPage.LOADING);
            loadingListener.onLoading();
        });
    }

    //这里还需要一个接口的触发时间，来实现网络异常如何点击
    public interface LoadingListener{
        void onLoading();
    }
    private LoadingListener loadingListener;
    public LoadingListener getLoadingListener() {
        return loadingListener;
    }
    public void setLoadingListener(LoadingListener loadingListener) {
        this.loadingListener = loadingListener;
    }
}
