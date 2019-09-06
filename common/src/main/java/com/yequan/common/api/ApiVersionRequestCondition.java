package com.yequan.common.api;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: yq
 * @Date: 2019/9/6 13:39
 * @Description: 自定义请求匹配规则
 */
public class ApiVersionRequestCondition implements RequestCondition<ApiVersionRequestCondition> {

    //匹配request中的版本号 v1 v2 ...
    private static final Pattern VERSION_PATTERN = Pattern.compile("/v(\\d+).*");

    //当前版本号
    private int version;

    //最大版本号
    private static int maxVersion = 1;

    public ApiVersionRequestCondition(int version) {
        this.version = version;
    }

    /**
     * 与另一个condition组合,例如方法和类都配置了@RequestMapping的url组合
     *
     * @param other
     * @return
     */
    @Override
    public ApiVersionRequestCondition combine(ApiVersionRequestCondition other) {
        //版本标签会在类或者方法中同时存在,优先整合方法上的版本信息
        return new ApiVersionRequestCondition(other.version);
    }

    /**
     * 检查request是否匹配，可能会返回新建的对象，例如，如果规则配置了多个模糊规则，可能当前请求
     * 只满足其中几个，那么只会返回这几个条件构建的Condition
     *
     * @param request
     * @return
     */
    @Override
    public ApiVersionRequestCondition getMatchingCondition(HttpServletRequest request) {
        //正则匹配请求uri中是否有版本号
        Matcher matcher = VERSION_PATTERN.matcher(request.getRequestURI());
        if (matcher.find()) {
            String versionNo = matcher.group(1);
            Integer version = Integer.valueOf(versionNo);
            //排除大于最大版本号和小于最低版本号的
            if (version <= maxVersion && version > this.version) {
                return this;
            }
        }
        return null;
    }

    /**
     * 同时满足多个Condition时,区分优先使用哪一个
     *
     * @param other
     * @param request
     * @return
     */
    @Override
    public int compareTo(ApiVersionRequestCondition other, HttpServletRequest request) {
        //以版本号大小判定优先级,越高越优先
        return other.version - this.version;
    }

    public int getVersion() {
        return version;
    }

    public static void setMaxVersion(int maxVersion) {
        ApiVersionRequestCondition.maxVersion = maxVersion;
    }
}
