package service;

/**
 * 阿里Java编码规范
 * 13.【推荐】接口类中的方法和属性不要加任何修饰符号(public 也不要加)，保持代码的简洁 性，并加上有效的 Javadoc 注释。
 * 尽量不要在接口里定义变量，如果一定要定义变量，肯定是 与接口方法相关，并且是整个应用的基础常量。
 */
public interface ISayService {

    /**
     * 欢迎语
     */
    void sayHello();


}
