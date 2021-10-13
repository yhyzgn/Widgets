package com.yhy.widget.demo.entity;

import com.yhy.widget.core.step.entity.StepAble;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-04-14 13:24
 * version: 1.0.0
 * desc   :
 */
public class TestStep implements StepAble {

    public String text;
    public Status status;

    public TestStep(String text) {
        this(text, Status.COMPLETE);
    }

    public TestStep(String text, Status status) {
        this.text = text;
        this.status = status;
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
