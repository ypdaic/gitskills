package cn.enjoyedu.refle;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：夏雨荷家里提供的夏雨荷的信息
 */
public class Servant {

    public boolean service(String message) {
        System.out.println(message+" 我的手法很好，力度适中，让人倍感舒适。");
        return true;
    }

    @Override
    public String toString() {
        return "我是14号";
    }
}
