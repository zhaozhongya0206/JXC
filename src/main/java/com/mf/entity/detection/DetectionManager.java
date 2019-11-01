package com.mf.entity.detection;/**
 * @Author zzy
 * @Description
 * @Date $ $
 * @Param $
 * @return $
 */

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Arrays;

@Entity
@Table(name = "detection_manager", schema = "db_jxc2")
public class DetectionManager {
	
	@Id
	@GeneratedValue
    private Integer id;
    private String detectionCode;//检测区域编号
    private String detectionName;//检测区域名称
    private String detectionType;//检测区域类型(1-运动 2-数字识别 3-颜色)
    private String digitSectionSmall;//2-数字识别 最小区间值)
    private String digitSectionBig;//2-数字识别 最大区间值)
    private String photoCode;//相机编号
    private String photoName;//相机名称
    private Timestamp createTime;//创建时间
    private String detectionLeftx;//X坐标
    private String detectionLefty;//Y坐标
    private String detectionWidth;//宽度
    private String detectionHeight;//高度
    private String type;//画图形状标识
    private Integer projectId;
    private String canvasWidth;
    private String canvasHeight;
    private String projectName;
    
    //用于展示使用
    @Transient
    private String createTimeStart;//设置开始时间
    @Transient
    private String createTimeEnd;//设置结束时间
    
    @Transient
    private String detectionTypeName;//检测区域类型名称
    @Transient
    private String exceptionVido;//异常视频文件
    @Transient
    private String exceptionImg;//异常图片文件
    
    @Transient
    private String[] listX;//X 坐标
    @Transient
    private String[] listY;//Y 坐标
    @Transient
    private String[] listWidth;//宽度
    @Transient
    private String[] listHeight;//高度
    @Transient
    private String[] listType;//画图类型
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "detection_code")
    public String getDetectionCode() {
        return detectionCode;
    }

    public void setDetectionCode(String detectionCode) {
        this.detectionCode = detectionCode;
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
    @Column(name = "detection_type")
    public String getDetectionType() {
        return detectionType;
    }

    public void setDetectionType(String detectionType) {
        this.detectionType = detectionType;
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
	@Column(name = "detection_left_x")
	public String getDetectionLeftx() {
		return detectionLeftx;
	}

	public void setDetectionLeftx(String detectionLeftx) {
		this.detectionLeftx = detectionLeftx;
	}

	@Basic
	@Column(name = "detection_left_y")
	public String getDetectionLefty() {
		return detectionLefty;
	}

	public void setDetectionLefty(String detectionLefty) {
		this.detectionLefty = detectionLefty;
	}

	@Basic
	@Column(name = "detection_width")
	public String getDetectionWidth() {
		return detectionWidth;
	}

	public void setDetectionWidth(String detectionWidth) {
		this.detectionWidth = detectionWidth;
	}

	@Basic
	@Column(name = "detection_height")
	public String getDetectionHeight() {
		return detectionHeight;
	}

	public void setDetectionHeight(String detectionHeight) {
		this.detectionHeight = detectionHeight;
	}

	@Basic
	@Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Basic
	@Column(name = "project_id")
	public Integer getProjectId() {
		return projectId;
	}

	@Basic
	@Column(name = "canvas_width")
	public String getCanvasWidth() {
		return canvasWidth;
	}

	public void setCanvasWidth(String canvasWidth) {
		this.canvasWidth = canvasWidth;
	}

	@Basic
	@Column(name = "canvas_height")
	public String getCanvasHeight() {
		return canvasHeight;
	}

	public void setCanvasHeight(String canvasHeight) {
		this.canvasHeight = canvasHeight;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@Basic
	@Column(name = "project_name")
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDetectionTypeName() {
		return detectionTypeName;
	}

	public void setDetectionTypeName(String detectionTypeName) {
		this.detectionTypeName = detectionTypeName;
	}

	public String getExceptionVido() {
		return exceptionVido;
	}

	public void setExceptionVido(String exceptionVido) {
		this.exceptionVido = exceptionVido;
	}

	public String getExceptionImg() {
		return exceptionImg;
	}

	public void setExceptionImg(String exceptionImg) {
		this.exceptionImg = exceptionImg;
	}

	public String[] getListX() {
		return listX;
	}

	public void setListX(String[] listX) {
		this.listX = listX;
	}

	public String[] getListY() {
		return listY;
	}

	public void setListY(String[] listY) {
		this.listY = listY;
	}

	public String[] getListWidth() {
		return listWidth;
	}

	public void setListWidth(String[] listWidth) {
		this.listWidth = listWidth;
	}

	public String[] getListHeight() {
		return listHeight;
	}

	public void setListHeight(String[] listHeight) {
		this.listHeight = listHeight;
	}

	public String[] getListType() {
		return listType;
	}

	public void setListType(String[] listType) {
		this.listType = listType;
	}

	@Basic
    @Column(name = "digit_section_small")
	public String getDigitSectionSmall() {
		return digitSectionSmall;
	}

	public void setDigitSectionSmall(String digitSectionSmall) {
		this.digitSectionSmall = digitSectionSmall;
	}

	@Basic
    @Column(name = "digit_section_big")
	public String getDigitSectionBig() {
		return digitSectionBig;
	}

	public void setDigitSectionBig(String digitSectionBig) {
		this.digitSectionBig = digitSectionBig;
	}

	@Override
	public String toString() {
		return "DetectionManager [id=" + id + ", detectionCode=" + detectionCode + ", detectionName=" + detectionName
				+ ", detectionType=" + detectionType + ", digitSectionSmall=" + digitSectionSmall + ", digitSectionBig="
				+ digitSectionBig + ", photoCode=" + photoCode + ", photoName=" + photoName + ", createTime="
				+ createTime + ", detectionLeftx=" + detectionLeftx + ", detectionLefty=" + detectionLefty
				+ ", detectionWidth=" + detectionWidth + ", detectionHeight=" + detectionHeight + ", type=" + type
				+ ", projectId=" + projectId + ", canvasWidth=" + canvasWidth + ", canvasHeight=" + canvasHeight
				+ ", projectName=" + projectName + ", createTimeStart=" + createTimeStart + ", createTimeEnd="
				+ createTimeEnd + ", detectionTypeName=" + detectionTypeName + ", exceptionVido=" + exceptionVido
				+ ", exceptionImg=" + exceptionImg + ", listX=" + Arrays.toString(listX) + ", listY="
				+ Arrays.toString(listY) + ", listWidth=" + Arrays.toString(listWidth) + ", listHeight="
				+ Arrays.toString(listHeight) + ", listType=" + Arrays.toString(listType) + "]";
	}

}
