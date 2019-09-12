package com.daiyanping.springcloud.common.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

@Data
public class SuperEntity extends Model {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
