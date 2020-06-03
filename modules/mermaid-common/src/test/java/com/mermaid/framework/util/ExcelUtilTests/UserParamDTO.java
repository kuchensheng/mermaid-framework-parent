package com.mermaid.framework.util.ExcelUtilTests;

import com.mermaid.framework.annotation.Cell;
import com.mermaid.framework.annotation.Excel;

import java.util.Date;
import java.util.List;

@Excel(startRow = 2)
public class UserParamDTO {
    private String id;

    @Cell(clumonNum = 1,name = "主键")
    private String seq;

    @Cell(clumonNum = 2,name = "用户名",required = true,max = 20)
    private String username;

    @Cell(clumonNum = 3,name = "性别",required = true)
    private String sex;

    @Cell(clumonNum = 4,name = "电话",required = true,max = 13,errMsg = "电话不能为空")
    private String telePhone;

    @Cell(clumonNum = 5,name = "工号")
    private String uuid;

    private Integer education;

    @Cell(clumonNum = 6,name = "学历")
    private String educationDesc;

    private Date birthDate;

    @Cell(clumonNum = 7,name = "出生年月",required = true)
    private String birth;


    private List<ParamDTO> paramDTOList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getEducation() {
        return education;
    }

    public void setEducation(Integer education) {
        this.education = education;
    }

    public String getEducationDesc() {
        return educationDesc;
    }

    public void setEducationDesc(String educationDesc) {
        this.educationDesc = educationDesc;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }


    public List<ParamDTO> getParamDTOList() {
        return paramDTOList;
    }

    public void setParamDTOList(List<ParamDTO> paramDTOList) {
        this.paramDTOList = paramDTOList;
    }

    @Override
    public String toString() {
        return "UserParamDTO{" +
                "id='" + id + '\'' +
                ", seq='" + seq + '\'' +
                ", username='" + username + '\'' +
                ", sex='" + sex + '\'' +
                ", telePhone='" + telePhone + '\'' +
                ", uuid='" + uuid + '\'' +
                ", education=" + education +
                ", educationDesc='" + educationDesc + '\'' +
                ", birthDate=" + birthDate +
                ", birth='" + birth + '\'' +
                ", paramDTOList=" + paramDTOList +
                '}';
    }
}
