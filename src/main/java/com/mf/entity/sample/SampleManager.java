package com.mf.entity.sample;

import javax.persistence.*;

@Entity
@Table(name = "sample_manager", schema = "db_jxc2")
public class SampleManager {
	
	@Id
	@GeneratedValue
    private Integer id;//主键id
	private String projectId;//委托单Id
	private String projectCode;//委托单编号
    private String projectName;//流程主题
    private String sampleCode;//样品编号
    private String sampleName;//样品名称
    private String shebeiCode;//设备编号
    private String shebeiName;//设备名称
    private String hjShidu;//环境湿度
    private String huFengxiang;//环境风向
    private String personName;//人员名称
    private String shiyanMethod;//实验方法
    private String carType;//车型
    private String carYear;//年份
    private String displacement;//排量
    private String innerCode;//内部编号
    private String remark;//备注

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
    @Column(name = "project_id")
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
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

    @Basic
    @Column(name = "sample_code")
    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
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
    @Column(name = "shebei_code")
    public String getShebeiCode() {
        return shebeiCode;
    }

    public void setShebeiCode(String shebeiCode) {
        this.shebeiCode = shebeiCode;
    }

    @Basic
    @Column(name = "shebei_name")
    public String getShebeiName() {
        return shebeiName;
    }

    public void setShebeiName(String shebeiName) {
        this.shebeiName = shebeiName;
    }

    @Basic
    @Column(name = "hj_shidu")
    public String getHjShidu() {
        return hjShidu;
    }

    public void setHjShidu(String hjShidu) {
        this.hjShidu = hjShidu;
    }

    @Basic
    @Column(name = "hu_fengxiang")
    public String getHuFengxiang() {
        return huFengxiang;
    }

    public void setHuFengxiang(String huFengxiang) {
        this.huFengxiang = huFengxiang;
    }

    @Basic
    @Column(name = "person_name")
    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    @Basic
    @Column(name = "shiyan_method")
    public String getShiyanMethod() {
        return shiyanMethod;
    }

    public void setShiyanMethod(String shiyanMethod) {
        this.shiyanMethod = shiyanMethod;
    }

    @Basic
    @Column(name = "car_type")
    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    @Basic
    @Column(name = "car_year")
    public String getCarYear() {
        return carYear;
    }

    public void setCarYear(String carYear) {
        this.carYear = carYear;
    }

    @Basic
    @Column(name = "displacement")
    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    @Basic
    @Column(name = "inner_code")
    public String getInnerCode() {
        return innerCode;
    }

    public void setInnerCode(String innerCode) {
        this.innerCode = innerCode;
    }

    @Basic
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	@Override
	public String toString() {
		return "SampleManager [id=" + id + ", projectName=" + projectName
				+ ", sampleCode=" + sampleCode + ", sampleName=" + sampleName
				+ ", shebeiCode=" + shebeiCode + ", shebeiName=" + shebeiName
				+ ", hjShidu=" + hjShidu + ", huFengxiang=" + huFengxiang
				+ ", personName=" + personName + ", shiyanMethod="
				+ shiyanMethod + ", carType=" + carType + ", carYear="
				+ carYear + ", displacement=" + displacement + ", innerCode="
				+ innerCode + ", remark=" + remark + "]";
	}
    
}
