package com.mf.entity.project;

import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "project_manager", schema = "db_jxc2")
public class ProjectManager {
	
	@Id
	@GeneratedValue
	private Integer id;
    private String projectCode;//委托单编号
    private String projectName;//流程主题
    private String photoCode;//对应相机编码
    private String photoName;//视频源相机名称
    private String codeType;//相机类型
    private String videoSource;//视频源
    private String videoSet;//录像设置(1不录像 2 全天录像)
    private String videoTime;//录像间隔
    private String exceptionEventTime;//录像时长（秒）
    private String projectUrl;//项目地址
    private String videoFileUrl;//视频文件路径
    private Integer projectFlag=0;//项目标记（0 未启用（默认）  1 开始（默认） 2 暂停 3 结束）
    private Timestamp createTime;//创建时间
    private Timestamp updateTime;//修改时间
    private String sampleCode;//样品编号
    private String sampleName;//样品名称
    private String sampleId;//对应样品表信息id
    
    @Transient
    private String projectFlagName;//项目标记名称
    @Transient
    private String videoSetName;//是否录像名称
    @Transient
    private String detectionCode;//检测code
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Basic
    @Column(name = "project_code")
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Basic
    @Column(name = "project_name")
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Basic
    @Column(name = "photo_code")
    public String getPhotoCode() {
        return photoCode;
    }

    public void setPhotoCode(String photoCode) {
        this.photoCode = photoCode;
    }
    
    @Basic
    @Column(name = "code_type")
    public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	@Basic
    @Column(name = "video_source")
    public String getVideoSource() {
        return videoSource;
    }

    public void setVideoSource(String videoSource) {
        this.videoSource = videoSource;
    }

    @Basic
    @Column(name = "video_set")
    public String getVideoSet() {
        return videoSet;
    }

    public void setVideoSet(String videoSet) {
        this.videoSet = videoSet;
    }

    @Basic
    @Column(name = "video_time")
    public String getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(String videoTime) {
        this.videoTime = videoTime;
    }

    @Basic
    @Column(name = "exception_event_time")
    public String getExceptionEventTime() {
        return exceptionEventTime;
    }

    public void setExceptionEventTime(String exceptionEventTime) {
        this.exceptionEventTime = exceptionEventTime;
    }

    @Basic
    @Column(name = "project_url")
    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    @Basic
    @Column(name = "video_file_url")
    public String getVideoFileUrl() {
        return videoFileUrl;
    }

	public void setVideoFileUrl(String videoFileUrl) {
        this.videoFileUrl = videoFileUrl;
    }
	
    @Basic
    @Column(name = "project_flag")
    public Integer getProjectFlag() {
		return projectFlag;
	}

	public void setProjectFlag(Integer projectFlag) {
		this.projectFlag = projectFlag;
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
	@Column(name = "update_time")
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	@Basic
    @Column(name = "sample_name")
	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	
	@Basic
    @Column(name = "photo_name")
	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	@Basic
    @Column(name = "sample_code")
	public String getSampleCode() {
		return sampleCode;
	}

	public void setSampleCode(String sampleCode) {
		this.sampleCode = sampleCode;
	}

	@Basic
    @Column(name = "sample_id")
	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public String getProjectFlagName() {
		return projectFlagName;
	}

	public void setProjectFlagName(String projectFlagName) {
		this.projectFlagName = projectFlagName;
	}
	
	public String getVideoSetName() {
		return videoSetName;
	}
	
	public void setVideoSetName(String videoSetName) {
		this.videoSetName = videoSetName;
	}

	public String getDetectionCode() {
		return detectionCode;
	}

	public void setDetectionCode(String detectionCode) {
		this.detectionCode = detectionCode;
	}

	@Override
	public String toString() {
		return "ProjectManager [id=" + id + ", projectCode=" + projectCode
				+ ", projectName=" + projectName + ", photoCode=" + photoCode
				+ ", photoName=" + photoName + ", codeType=" + codeType
				+ ", videoSource=" + videoSource + ", videoSet=" + videoSet
				+ ", videoTime=" + videoTime + ", exceptionEventTime="
				+ exceptionEventTime + ", projectUrl=" + projectUrl
				+ ", videoFileUrl=" + videoFileUrl + ", projectFlag="
				+ projectFlag + ", createTime=" + createTime + ", sampleCode="
				+ sampleCode + ", sampleName=" + sampleName + ", sampleId="
				+ sampleId + ", projectFlagName=" + projectFlagName
				+ ", videoSetName=" + videoSetName + ", detectionCode="
				+ detectionCode + "]";
	}

}
