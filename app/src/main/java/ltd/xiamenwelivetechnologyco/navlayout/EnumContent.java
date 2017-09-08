package ltd.xiamenwelivetechnologyco.navlayout;

/**
* author:wedfrend
* email:wedfrend@yeah.net
* create:2017/8/28 16:22
* desc:
*/

public class EnumContent {

    /**
     * Desc:在Content中，为了优化程序，减少加载次数
     *
     * 正常情况下没有问题，在一般的用户使用频率角度考虑也不会有问题
     *
     * 当用户在首页依次点击过  消息   服务  邻里  我
     * 此时4个Fragment均被创建，当手机自动息屏或者用户点击Home按钮点击另一程序
     *
     * 当再次打开的时候会 执行 Fragment onstart onResume 等方法，所以加载网络的方法需要作出判断，对于实时变化不太重要的界面，可以不予考虑
     *
    */
    public static enum fragmentContent{
        NONE,MES,SERVICE,NEIGHBOR,ME
    }

    /*
    关于网络的状态
     */
    public static enum NetWork{
        WIFI,MOBILE,NONE
    }

    /**
     * 页面加载状态
     */
    public static enum statusPage{
        NONE,LOADING,DATEERROR,NETERROR
    }
}
