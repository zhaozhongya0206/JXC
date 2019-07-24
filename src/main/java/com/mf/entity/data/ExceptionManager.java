package com.mf.entity.data;

import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "exceptions", schema = "db_jxc2")
public class ExceptionManager {
	
	@Id
	@GeneratedValue
	private Integer id;
    private String projectId;//委托单编号
    private String projectCode;//委托单code
    private String projectName;//流程主题
    private String photoId;//相机编码
    private String photoCode;//相机code
    private String photoName;//相机名称
    private String flag;//是否已读标识（0未读，1已读）
    private String sampleId;//项目实例号
    private String sampleCode;//项目实例code
    private String sampleName;//项目实例名称
    private String detectionId;//检测区域号
    private String detectionCode;//检测区域code
    private String detectionName;//检测区域号名称
    private String folderVideoPath;//视频存放路径（根目录）
    private String folderPicturePath;//图片存放路径（根目录）
    private String videoPath;//异常视频文件路径（MP4）多个以,分割
    private String picturePath;//异常图像文件路径（png）多个以,分割
    private Timestamp createTime;//创建时间
    
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
    @Column(name = "project_id")
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	@Basic
    @Column(name = "sample_id")
	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	@Basic
    @Column(name = "detection_id")
	public String getDetectionId() {
		return detectionId;
	}

	public void setDetectionId(String detectionId) {
		this.detectionId = detectionId;
	}
	
	@Basic
    @Column(name = "picture_path")
	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	@Basic
    @Column(name = "video_path")
	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	@Basic
    @Column(name = "folder_video_path")
	public String getFolderVideoPath() {
		return folderVideoPath;
	}

	public void setFolderVideoPath(String folderVideoPath) {
		this.folderVideoPath = folderVideoPath;
	}

	@Basic
    @Column(name = "folder_picture_path")
	public String getFolderPicturePath() {
		return folderPicturePath;
	}

	public void setFolderPicturePath(String folderPicturePath) {
		this.folderPicturePath = folderPicturePath;
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
    @Column(name = "project_name")
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
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
    @Column(name = "detection_name")
	public String getDetectionName() {
		return detectionName;
	}

	public void setDetectionName(String detectionName) {
		this.detectionName = detectionName;
	}

	@Basic
    @Column(name = "photo_id")
	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
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
    @Column(name = "flag")
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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
    @Column(name = "project_code")
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
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
    @Column(name = "sample_code")
	public String getSampleCode() {
		return sampleCode;
	}

	public void setSampleCode(String sampleCode) {
		this.sampleCode = sampleCode;
	}

	@Basic
    @Column(name = "detection_code")
	public String getDetectionCode() {
		return detectionCode;
	}

	public void setDetectionCode(String detectionCode) {
		this.detectionCode = detectionCode;
	}

	@Override
	public String toString() {
		return "ExceptionManager [id=" + id + ", projectId=" + projectId
				+ ", projectCode=" + projectCode + ", projectName="
				+ projectName + ", photoId=" + photoId + ", photoCode="
				+ photoCode + ", photoName=" + photoName + ", flag=" + flag
				+ ", sampleId=" + sampleId + ", sampleCode=" + sampleCode
				+ ", sampleName=" + sampleName + ", detectionId=" + detectionId
				+ ", detectionCode=" + detectionCode + ", detectionName="
				+ detectionName + ", folderVideoPath=" + folderVideoPath
				+ ", folderPicturePath=" + folderPicturePath + ", videoPath="
				+ videoPath + ", picturePath=" + picturePath + ", createTime="
				+ createTime + ", createTimeStart=" + createTimeStart
				+ ", createTimeEnd=" + createTimeEnd + "]";
	}

}