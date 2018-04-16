package com.yhy.widget.core.step.entity;

import java.io.Serializable;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-04-14 11:09
 * version: 1.0.0
 * desc   : 可步骤化实体需要实现该接口，用于识别每个步骤的状态
 */
public interface StepAble extends Serializable {

    // 状态枚举
    enum Status {
        // 完成，当前，默认
        COMPLETE, CURRENT, DEFAULT
    }

    /**
     * 获取当前对象的状态
     *
     * @return 当前对象状态
     */
    Status getStatus();
}
