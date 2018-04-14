package com.yhy.widget.core.step.entity;

import android.graphics.PointF;

import java.io.Serializable;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-04-14 14:20
 * version: 1.0.0
 * desc   :
 */
public class StepNode implements Serializable {
    public PointF center;
    public float radius;
    public StepAble.Status status;
    public NodeType nodeType;

    public enum NodeType {
        START, MIDDLE, END
    }
}
