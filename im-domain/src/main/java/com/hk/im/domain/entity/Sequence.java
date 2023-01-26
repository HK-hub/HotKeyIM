package com.hk.im.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName : Sequence
 * @author : HK意境
 * @date : 2023/1/26 18:46
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@TableName(value ="tb_sequence")
@Data
@Accessors(chain = true)
public class Sequence implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标识业务类型
     */
    @TableField(value = "name")
    private String name;

    /**
     * 会话参与者
     */
    @TableField(value = "participant_id")
    private Long participantId;

    /**
     * 会话发起者
     */
    @TableField(value = "communication_id")
    private Long communicationId;

    /**
     * 下一次将要申请的号段起始位置
     */
    @TableField(value = "max")
    private Long max;

    /**
     * 递增步长
     */
    @TableField(value = "step")
    private Integer step;

    /**
     * 号段大小
     */
    @TableField(value = "segment")
    private Integer segment;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    /**
     * 当前ID
     */
    @TableField(exist = false)
    private Long current = 1L;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Sequence other = (Sequence) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getParticipantId() == null ? other.getParticipantId() == null : this.getParticipantId().equals(other.getParticipantId()))
            && (this.getCommunicationId() == null ? other.getCommunicationId() == null : this.getCommunicationId().equals(other.getCommunicationId()))
            && (this.getMax() == null ? other.getMax() == null : this.getMax().equals(other.getMax()))
            && (this.getStep() == null ? other.getStep() == null : this.getStep().equals(other.getStep()))
            && (this.getSegment() == null ? other.getSegment() == null : this.getSegment().equals(other.getSegment()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getParticipantId() == null) ? 0 : getParticipantId().hashCode());
        result = prime * result + ((getCommunicationId() == null) ? 0 : getCommunicationId().hashCode());
        result = prime * result + ((getMax() == null) ? 0 : getMax().hashCode());
        result = prime * result + ((getStep() == null) ? 0 : getStep().hashCode());
        result = prime * result + ((getSegment() == null) ? 0 : getSegment().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", participantId=").append(participantId);
        sb.append(", communicationId=").append(communicationId);
        sb.append(", max=").append(max);
        sb.append(", step=").append(step);
        sb.append(", segment=").append(segment);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}