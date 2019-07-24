package com.mf.entity.photo;

import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PHOTO_MANAGER", schema = "db_jxc2")
public class PhotoManager {
	
	@Id
	@GeneratedValue
	private Integer id;
    private String photoCode;//相机编码
    private String photoName;//相机名称
    private String photoIp;//相机ip
    private Integer photoPort;//相机端口号
    private String photoLogoName;//相机登录用户名
    private String photoLogoPassword;//相机登录密码
    private String codeType;//相机编码方式
    private String frameRate;//帧率
    private String photoResolution;//分辨率
    private String videoFormat;//视频格式
    private Timestamp createTime;//创建时间
    private String photoStatus;//相机状态（0闲置，1使用中）
    private String file;//视频文件名称
    private String filePath;//上传文件的完整路径
    
    @Transient
    private String createTimeStart;//设置开始时间
    @Transient
    private String createTimeEnd;//设置结束时间
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
    @Column(name = "photo_name")
    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    @Basic
    @Column(name = "photo_ip")
    public String getPhotoIp() {
        return photoIp;
    }

    public void setPhotoIp(String photoIp) {
        this.photoIp = photoIp;
    }

    @Basic
    @Column(name = "photo_port")
    public Integer getPhotoPort() {
        return photoPort;
    }

    public void setPhotoPort(Integer photoPort) {
        this.photoPort = photoPort;
    }

    @Basic
    @Column(name = "photo_logo_name")
    public String getPhotoLogoName() {
        return photoLogoName;
    }

    public void setPhotoLogoName(String photoLogoName) {
        this.photoLogoName = photoLogoName;
    }

    @Basic
    @Column(name = "photo_logo_password")
    public String getPhotoLogoPassword() {
        return photoLogoPassword;
    }

    public void setPhotoLogoPassword(String photoLogoPassword) {
        this.photoLogoPassword = photoLogoPassword;
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
    @Column(name = "frame_rate")
    public String getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(String frameRate) {
        this.frameRate = frameRate;
    }

    @Basic
    @Column(name = "photo_resolution")
    public String getPhotoResolution() {
        return photoResolution;
    }

    public void setPhotoResolution(String photoResolution) {
        this.photoResolution = photoResolution;
    }

    @Basic
    @Column(name = "video_format")
    public String getVideoFormat() {
        return videoFormat;
    }

    public void setVideoFormat(String videoFormat) {
        this.videoFormat = videoFormat;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

    @Basic
    @Column(name = "photo_status")
	public String getPhotoStatus() {
		return photoStatus;
	}

	public void setPhotoStatus(String photoStatus) {
		this.photoStatus = photoStatus;
	}

	@Basic
	@Column(name = "file")
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	@Basic
	@Column(name = "file_path")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String toString() {
		return "PhotoManager [id=" + id + ", photoCode=" + photoCode
				+ ", photoName=" + photoName + ", photoIp=" + photoIp
				+ ", photoPort=" + photoPort + ", photoLogoName="
				+ photoLogoName + ", photoLogoPassword=" + photoLogoPassword
				+ ", codeType=" + codeType + ", frameRate=" + frameRate
				+ ", photoResolution=" + photoResolution + ", videoFormat="
				+ videoFormat + ", createTime=" + createTime + ", photoStatus="
				+ photoStatus + ", file=" + file + ", filePath=" + filePath
				+ ", createTimeStart=" + createTimeStart + ", createTimeEnd="
				+ createTimeEnd + "]";
	}
	
}