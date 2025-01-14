package com.purplebits.emrd2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.purplebits.emrd2.util.JsonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectDTO {

	private Integer projectId;
    private EntitiesDTO entity;
    private String projectName;
    private String projectDisplayName;
    private String projectPassword;
    private String projectUILayout;
    private boolean prRequired;
    private Integer physicalAge;
    private boolean offlineAccessRequired;
    private Integer onlineAge;
    private Integer scanTimePerRecord;
    private String projectDetails;
    private String dynamicForm;
    private boolean bookmark;
    private boolean qcRatio;
    private boolean shareProject;

	// No-argument constructor
	public ProjectDTO() {}

	// All-arguments constructor
	public ProjectDTO(Integer projectId, String projectName) {
		this.projectId = projectId;
		this.projectName = projectName;
	}
    public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public EntitiesDTO getEntity() {
		return entity;
	}
	public void setEntity(EntitiesDTO entity) {
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
