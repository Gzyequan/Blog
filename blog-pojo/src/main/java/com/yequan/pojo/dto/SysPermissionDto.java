package com.yequan.pojo.dto;

import com.google.common.base.Converter;
import com.yequan.pojo.entity.SysPermissionDO;
import org.springframework.beans.BeanUtils;

/**
 * @Auther: yq
 * @Date: 2019/10/16 14:30
 * @Description:
 */
public class SysPermissionDto {

    private Integer id;

    private Integer parentId;

    private Byte status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public SysPermissionDO convertToSysPermissionDO() {
        SysPermissionDtoConvert sysPermissionDtoConvert = new SysPermissionDtoConvert();
        return sysPermissionDtoConvert.convert(this);
    }

    private static class SysPermissionDtoConvert extends Converter<SysPermissionDto, SysPermissionDO> {

        /**
         * SysPermissionDto转成SysPermissionDO
         *
         * @param sysPermissionDto
         * @return
         */
        @Override
        protected SysPermissionDO doForward(SysPermissionDto sysPermissionDto) {
            SysPermissionDO sysPermissionDO = new SysPermissionDO();
            BeanUtils.copyProperties(sysPermissionDto, sysPermissionDO);
            return sysPermissionDO;
        }

        @Override
        protected SysPermissionDto doBackward(SysPermissionDO sysPermissionDO) {
            throw new AssertionError("不支持逆向转换");
        }
    }

}
