package com.mf.entity.camera;

import javax.persistence.*;

@Entity
@Table(name = "t_test", schema = "db_jxc2")
public class Test {
	
	@Id
	@GeneratedValue
	private Integer id;
	
    private String name;
    
    private Integer age;
    
    private String address;
    
    private String remark;

  
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "age")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
		return "Test [id=" + id + ", name=" + name + ", age=" + age
				+ ", address=" + address + ", remark=" + remark + "]";
	}
    
}
