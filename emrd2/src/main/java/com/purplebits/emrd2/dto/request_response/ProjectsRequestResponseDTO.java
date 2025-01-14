package com.purplebits.emrd2.dto.request_response;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.purplebits.emrd2.dto.EntitiesDTO;
import com.purplebits.emrd2.dto.ProjectDTO;
import com.purplebits.emrd2.types.EntityType;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

public class ProjectsRequestResponseDTO {

	private Integer projectId;
	private Integer entityId;
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

	public ProjectsRequestResponseDTO() {
		super();
	}

	public ProjectsRequestResponseDTO(ProjectDTO projectDTO) {
		super();
		this.projectId = projectDTO.getProjectId();
		this.entityId = projectDTO.getEntity().getEntityId();
		this.projectName = projectDTO.getProjectName();
		this.projectDisplayName = projectDTO.getProjectDisplayName();
		this.projectPassword = projectDTO.getProjectPassword();
		this.projectUILayout = projectDTO.getProjectUILayout();
		this.prRequired = projectDTO.isPrRequired();
		this.physicalAge = projectDTO.getPhysicalAge();
		this.offlineAccessRequired = projectDTO.isOfflineAccessRequired();
		this.onlineAge = projectDTO.getOnlineAge();
		this.scanTimePerRecord = projectDTO.getScanTimePerRecord();
		this.projectDetails = projectDTO.getProjectDetails();
		this.dynamicForm = projectDTO.getDynamicForm();
		this.bookmark = projectDTO.isBookmark();
		this.qcRatio = projectDTO.isQcRatio();
		this.shareProject = projectDTO.isShareProject();
	}

	@JsonIgnore
	public ProjectDTO getProjectDTO() {
		ProjectDTO projectDTO = new ProjectDTO();
		projectDTO.setProjectId(projectId);
		projectDTO.setProjectName(projectName);
		projectDTO.setProjectDisplayName(projectDisplayName);
		projectDTO.setProjectPassword(projectPassword);
		projectDTO.setProjectUILayout(projectUILayout);
		projectDTO.setPrRequired(prRequired);
		projectDTO.setPhysicalAge(physicalAge);
		projectDTO.setOfflineAccessRequired(offlineAccessRequired);
		projectDTO.setOnlineAge(onlineAge);
		projectDTO.setScanTimePerRecord(scanTimePerRecord);
		projectDTO.setProjectDetails(projectDetails);
		projectDTO.setDynamicForm(dynamicForm);
		projectDTO.setBookmark(bookmark);
		projectDTO.setQcRatio(qcRatio);
		projectDTO.setShareProject(shareProject);

		EntitiesDTO entitiesDTO = new EntitiesDTO();
		entitiesDTO.setEntityId(entityId);
		projectDTO.setEntity(entitiesDTO);
		return projectDTO;
	}

	public static List<ProjectsRequestResponseDTO> getProjectRequestResponseDTO(List<ProjectDTO> projectDTOS) {
		List<ProjectsRequestResponseDTO> res = new ArrayList<>();
		for (ProjectDTO project : projectDTOS) {
			res.add(new ProjectsRequestResponseDTO(project));
		}
		return res;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
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
