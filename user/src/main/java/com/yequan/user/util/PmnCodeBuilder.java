package com.yequan.user.util;

import com.yequan.user.dao.SysPermissionDOMapper;
import com.yequan.user.pojo.dbo.SysPermissionDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/7/30 16:06
 * @Description: pmnCode生成器
 */
@Service
public class PmnCodeBuilder {

    @Autowired
    private SysPermissionDOMapper sysPermissionDOMapper;

    /**
     * 生成逻辑:
     * 1.父节点parentCode为0,表示该节点为根节点
     * 2.根据parentCode获取当前一级的所有权限pmnCode,找出最大pmnCode,然后在其后进行加1生成新的pmnCode
     *
     * @param parentCode
     * @return
     */
    public Integer builderPmnCode(Integer parentCode) {
        Integer pmnCode = null;
        int level = 0;
        if (null == parentCode) {
            return pmnCode;
        }
        //1.确认新建pmnCode所在等级
        if (0 == parentCode) {
            level = 1;
        } else {
            confirmPmnCodeLevel(parentCode);
        }
        List<SysPermissionDO> sysPermissionDOList =
                sysPermissionDOMapper.selectChildrenParallelPermission(parentCode);
        //sysPermissionDOList为空表示parentCode下无权限,表示新建一个
        if (CollectionUtils.isEmpty(sysPermissionDOList)) {

        } else {

        }
        return pmnCode;
    }

    private void confirmPmnCodeLevel(Integer parentCode) {
        int i = parentCode.toString().indexOf("00");
    }

}
