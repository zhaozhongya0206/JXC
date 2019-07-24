package com.mf.entity.camera;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @ClassName:PlayVideo  
 * @Description:播放视频实体类
 * @author zzy
 * @date 2018年12月10日
 * @version V1.0
 */
@Entity
@Table(name = "t_play_video", schema = "db_jxc2")
public class PlayVideo {
	
    @Id
    @GeneratedValue
    private Integer id;//视频id
    
    private String title;//视频标题
    private String src;//上传后的视频地址
    private String suffix;//上传的视频名称
    private String newSuffix;//上传文件新的文件名称为当前时间
    private String descript;//视频描述
    private Timestamp createTime;//创建时间
    private String userName;//用户名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "src")
    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Basic
    @Column(name = "suffix")
    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    
    @Basic
    @Column(name = "new_suffix")
    public String getNewSuffix() {
        return newSuffix;
    }

    public void setNewSuffix(String newSuffix) {
        this.newSuffix = newSuffix;
    }

    @Basic
    @Column(name = "descript")
    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

	@Override
	public String toString() {
		return "PlayVideo [id=" + id + ", title=" + title + ", src=" + src + 
				", suffix=" + suffix + ", descript=" + descript + ", createTime="
				+ createTime + ", userName="
				+ userName + "]";
	}

}