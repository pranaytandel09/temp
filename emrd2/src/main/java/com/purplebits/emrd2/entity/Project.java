package com.purplebits.emrd2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.purplebits.emrd2.util.JsonUtils;

@Entity
@Table(name = "table_projects", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"entity_id", "project_name"}),
           @UniqueConstraint(columnNames = {"entity_id", "project_disp_name"})
       })
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Integer projectId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entity_id", nullable = false)
    private Entities entity; 

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "project_disp_name", nullable = false)
    private String projectDisplayName;

    @Column(name = "project_password")
    private String projectPassword;

    @Lob
    @Column(name = "projectuilayout")
    private String projectUILayout;

    @Column(name = "pr_reqd")
    private boolean prRequired;

    @Column(name = "physical_age")
    private Integer physicalAge;

    @Column(name = "offline_access_reqd")
    private boolean offlineAccessRequired;

    @Column(name = "online_age")
    private Integer onlineAge;

    @Column(name = "scan_time_per_rec")
    private Integer scanTimePerRecord;

    @Lob
    @Column(name = "project_details",columnDefinition = "nText")
    private String projectDetails;

    @Lob
    @Column(name = "dynamic_form")
    private String dynamicForm;

    @Column(name = "bookmark", columnDefinition = "bit default 0")
    private boolean bookmark;

    @Column(name = "qc_ratio", columnDefinition = "bit default 0")
    private boolean qcRatio;

    @Column(name = "share_project", columnDefinition = "bit default 0")
    private boolean shareProject;

	public Project(Integer projectId) {
	}

	public Project() {

	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Entities getEntity() {
		return entity;
	}

	public void setEntity(Entities entity) {
		this.entity = entity;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectDisplayName() {
		return projectDisplayName;
	}

	public void setProjectDisplayName(String projectDisplayName) {
		this.projectDisplayName = projectDisplayName;
	}

	public String getProjectPassword() {
		return projectPassword;
	}

	public void setProjectPassword(String projectPassword) {
		this.projectPassword = projectPassword;
	}

	public String getProjectUILayout() {
		return projectUILayout;
	}

	public void setProjectUILayout(String projectUILayout) {
		this.projectUILayout = projectUILayout;
	}

	public boolean isPrRequired() {
		return prRequired;
	}

	public void setPrRequired(boolean prRequired) {
		this.prRequired = prRequired;
	}

	public Integer getPhysicalAge() {
		return physicalAge;
	}

	public void setPhysicalAge(Integer physicalAge) {
		this.physicalAge = physicalAge;
	}

	public boolean isOfflineAccessRequired() {
		return offlineAccessRequired;
	}

	public void setOfflineAccessRequired(boolean offlineAccessRequired) {
		this.offlineAccessRequired = offlineAccessRequired;
	}

	public Integer getOnlineAge() {
		return onlineAge;
	}

	public void setOnlineAge(Integer onlineAge) {
		this.onlineAge = onlineAge;
	}

	public Integer getScanTimePerRecord() {
		return scanTimePerRecord;
	}

	public void setScanTimePerRecord(Integer scanTimePerRecord) {
		this.scanTimePerRecord = scanTimePerRecord;
	}

	public String getProjectDetails() {
		return projectDetails;
	}

	public void setProjectDetails(String projectDetails) {
		this.projectDetails = projectDetails;
	}

	public String getDynamicForm() {
		return dynamicForm;
	}

	public void setDynamicForm(String dynamicForm) {
		this.dynamicForm = dynamicForm;
	}

	public boolean isBookmark() {
		return bookmark;
	}

	public void setBookmark(boolean bookmark) {
		this.bookmark = bookmark;
	}

	public boolean isQcRatio() {
		return qcRatio;
	}

	public void setQcRatio(boolean qcRatio) {
		this.qcRatio = qcRatio;
	}

	public boolean isShareProject() {
		return shareProject;
	}

	public void setShareProject(boolean shareProject) {
		this.shareProject = shareProject;
	}
    
	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
    
}
