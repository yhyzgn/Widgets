package com.yhy.widget.core.step.entity;

import java.io.Serializable;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-04-14 11:09
 * version: 1.0.0
 * desc   :
 */
public interface StepAble extends Serializable {

    enum Status {
        COMPLETE, DEFAULT, ATTENTION
    }

    Status getStatus();
}
