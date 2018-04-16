package com.yhy.widget.core.step.entity;

import android.graphics.PointF;

import java.io.Serializable;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2018-04-14 14:20
 * version: 1.0.0
 * desc   : 步骤节点
 */
public class StepNode implements Serializable {
    private static final long serialVersionUID = 8961443525468360517L;
    
    // 节点圆心
    public PointF center;
    // 节点半径
    public float radius;
    // 节点状态
    public StepAble.Status status;
    // 节点类型【开头，中间，末尾】
    public NodeType nodeType;

    /**
     * 节点类型枚举
     */
    public enum NodeType {
        // 开头，中间，末尾
        START, MIDDLE, END
    }
}
