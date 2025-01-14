package com.purplebits.emrd2.util;

public class ResponseMessages {

	public static final String CREATE_ROLES_OK = "role.created.ok";
	public static final String FETCH_ROLES_OK = "role.fetch.ok";
	public static final String FETCH_ROLES_ERROR = "role.fetch.error";
	public static final String DELETE_ROLES_OK = "role.delete.ok";
	public static final String DELETE_ROLES_ERROR = "role.delete.error";
	public static final String ROLES_NOT_FOUND_ERROR = "roles.not.found.error";
	public static final String ROLE_ALREADY_EXISTS = "role.already.exists";
	public static final String ROLEID_ALREADY_FOUND_ERROR = "role.id.already.found.error";
	public static final String ROLE_DELETION_ERROR = "role.deletion.error";
	public static final String ADMIN_ROLE_DELETION_ERROR = "admin.role.deletion.error";
	public static final String CREATE_ROLES_PERMISSIONS_OK = "roles.create.or.update.success.message";
	public static final String CREATE_ROLES_ERROR = "roles.create.error.message";
	public static final String UPDATE_ROLES_OK = "roles.update.success.message";
	public static final String UPDATE_ROLES_ERROR = "roles.update.error.message";

	public static final String BAD_CREDENTIALS_EXCEPTION_LOG_MESSAGE = "authentication.bad.credentials.log.message";
	public static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE = "validation.method.argument.not.valid.message";
	public static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION_LOG_MESSAGE = "validation.method.argument.not.valid.log.message";
	public static final String RUNTIME_EXCEPTION_MESSAGE = "exception.runtime.unexpected.message";
	public static final String RUNTIME_EXCEPTION_LOG_MESSAGE = "exception.runtime.unexpected.log.message";
	public static final String NULL_POINTER_EXCEPTION_MESSAGE = "exception.null.pointer.message";
	public static final String NULL_POINTER_EXCEPTION_LOG_MESSAGE = "exception.null.pointer.log.message";
	public static final String NUMBER_FORMAT_EXCEPTION_MESSAGE = "exception.number.format.message";
	public static final String NUMBER_FORMAT_EXCEPTION_LOG_MESSAGE = "exception.number.format.log.message";
	public static final String SQL_EXCEPTION_MESSAGE = "exception.sql.message";
	public static final String SQL_EXCEPTION_LOG_MESSAGE = "exception.sql.log.message";
	public static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE = "exception.illegal.argument.message";
	public static final String ILLEGAL_ARGUMENT_EXCEPTION_LOG_MESSAGE = "exception.illegal.argument.log.message";
	public static final String INPUT_MISMATCH_EXCEPTION_MESSAGE = "exception.input.mismatch.message";
	public static final String INPUT_MISMATCH_EXCEPTION_LOG_MESSAGE = "exception.input.mismatch.log.message";
	public static final String NO_SUCH_ELEMENT_EXCEPTION_MESSAGE = "exception.no.such.element.message";
	public static final String NO_SUCH_ELEMENT_EXCEPTION_LOG_MESSAGE = "exception.no.such.element.log.message";
	public static final String JSON_PARSING_EXCEPTION = "json.parsing.exception";
	public static final String UNAUTHORIZED = "unauthorized";

	public static final String FILE_NOT_FOUND_EXCEPTION_MESSAGE = "exception.file.not.found.message";
	public static final String FILE_NOT_FOUND_EXCEPTION_LOG_MESSAGE = "exception.file.not.found.log.message";
	public static final String IO_EXCEPTION_MESSAGE = "exception.io.error.message";
	public static final String IO_EXCEPTION_LOG_MESSAGE = "exception.io.error.log.message";
	public static final String FILE_ALREADY_EXISTS_EXCEPTION_MESSAGE = "exception.file.already.exists.message";
	public static final String FILE_ALREADY_EXISTS_EXCEPTION_LOG_MESSAGE = "exception.file.already.exists.log.message";
	public static final String ACCESS_DENIED_EXCEPTION_MESSAGE = "exception.access.denied.message";
	public static final String ACCESS_DENIED_EXCEPTION_LOG_MESSAGE = "exception.access.denied.log.message";
	public static final String DIRECTORY_NOT_EMPTY_EXCEPTION_MESSAGE = "exception.directory.not.empty.message";
	public static final String DIRECTORY_NOT_EMPTY_EXCEPTION_LOG_MESSAGE = "exception.directory.not.empty.log.message";

	public static final String CREATE_PERMISSIONS_OK = "permissions.create.ok";
	public static final String CREATE_PERMISSIONS_ERROR = "permissions.create.error";
	public static final String UPDATE_PERMISSIONS_OK = "permissions.update.ok";
	public static final String UPDATE_PERMISSIONS_ERROR = "permissions.update.error";
	public static final String FETCH_PERMISSIONS_OK = "permissions.fetch.ok";
	public static final String FETCH_PERMISSIONS_ERROR = "permissions.fetch.error";
	public static final String DELETE_PERMISSIONS_OK = "permissions.delete.ok";
	public static final String DELETE_PERMISSIONS_ERROR = "permissions.delete.error";
	public static final String PERMISSIONS_NOT_FOUND_ERROR = "permissions.not.found.error";
	public static final String DUPLICATE_PERMISSIONS_ERROR = "permissions.duplicate.name.error";
	public static final String DUPLICATE_DISPLAY_NAME_ERROR = "permissions.duplicate.displayName.error";

	// User Management
	public static final String CREATE_USER_OK = "user.create.ok";
	public static final String USER_NOT_FOUND = "user.not.found";
	public static final String USER_FOUND = "user.found";
	public static final String UPDATE_USER_OK = "user.update.ok";
	public static final String DELETE_USER_OK = "user.delete.ok";
	public static final String ACTIVE_USER_FOUND_OK = "user.active.found.ok";
	public static final String BAD_REQUEST = null;

	public static final String USER_ALREADY_EXISTS_ERROR = "user.already.exists.error";
	public static final String USERNAME_ALREADY_EXISTS_ERROR = "username.already.exists.error";

	// User Groups
	public static final String DUPLICATE_GROUP_ERROR = "user.group.duplicate.error";
	public static final String USER_GROUP_NOT_FOUND_ERROR = "user.group.not.found.error";
	public static final String USER_GROUP_NAME_NOT_FOUND_ERROR = "user.group.name.not.found.error";
	public static final String CREATE_USER_GROUP_OK = "user.group.create.ok";
	public static final String UPDATE_USER_GROUP_OK = "user.group.update.ok";
	public static final String FETCH_USER_GROUP_OK = "user.group.fetch.ok";
	public static final String DELETE_USER_GROUP_OK = "user.group.delete.ok";
	public static final String USER_GROUP_STATUS_TYPE_ERROR = "user.group.status.invalid";
	public static final String ADMIN_ROLE_CHANGE_NOT_ALLOWED_ERROR = "admin.role.change.not.allowed";
	public static final String ADMIN_DISPLAY_NAME_CHANGE_NOT_ALLOWED_ERROR = "admin.display.name.change.not.allowed";
	public static final String ADMIN_ROLE_DELETE_NOT_ALLOWED_ERROR = "admin.role.delete.not.allowed";
	public static final String DUPLICATE_GROUP_ID_ERROR = "group.id.duplicate.error";
	public static final String USER_GROUP_DELETE_ERROR = "user.group.delete.error";
	
	// User Group Membership
		public static final String USER_GROUP_MEMBERSHIP_NOT_FOUND_ERROR = "user.group.membership.not.found";
		public static final String CREATE_USER_GROUP_MEMBERSHIP_OK = "user.group.membership.create.ok";
		public static final String UPDATE_USER_GROUP_MEMBERSHIP_OK = "user.group.membership.update.ok";
		public static final String FETCH_USER_GROUP_MEMBERSHIP_OK = "user.group.membership.fetch.ok";
		public static final String DELETE_USER_GROUP_MEMBER_OK = "user.group.member.delete.ok";
		public static final String USER_GROUPS_NOT_ASSIGNED_ERROR = "user.groups.not.assigned.error";

	// Role Permissions
	public static final String DUPLICATE_PERMISSION_ERROR = "role.permission.duplicate.error";
	public static final String PERMISSION_NOT_FOUND_ERROR = "role.permission.not.found.error";
	public static final String CREATE_ROLE_PERMISSION_OK = "role.permission.create.ok";
	public static final String UPDATE_ROLE_PERMISSION_OK = "role.permission.update.ok";
	public static final String ROLE_PERMISSION_NOT_FOUND_ERROR = "role.permission.not.found";
	public static final String FETCH_ROLE_PERMISSION_OK = "role.permission.fetch.ok";
	public static final String DELETE_ROLE_PERMISSION_OK = "role.permission.delete.ok";
	public static final String ROLE_PERMISSIONS_NOT_FOUND_ERROR = "role.permissions.not.found.error";
	public static final String ADMIN_GROUP_DELETION_ERROR = "admin.group.delete.not.allowed";

	// OTP and Authentication
	public static final String OTP_SENT_SUCCESS = "otp.sent.success";
	public static final String OTP_VERIFIED_SUCCESS = "otp.verified.success";
	public static final String OTP_RESEND_COOLDOWN_ERROR = "otp.resend.cooldown.error";
	public static final String OTP_NOT_FOUND_OR_EXPIRED = "otp.not.found.or.expired";
	public static final String INVALID_EMAIL_OR_OTP_EXPIRED = "otp.invalid.email.or.expired";
	public static final String OTP_EXPIRED = "otp.expired";
	public static final String MAX_OTP_ATTEMPTS_EXCEEDED = "otp.max.attempts.exceeded";
	public static final String INVALID_OTP = "otp.invalid";
	public static final String PASSWORD_RESET_SUCCESS = "password.reset.success";
	public static final String ERROR_EMAIL_SENDING = "email.sending.error";

	public static final String CREATE_AUDITLOGS_OK = "auditlog.create.ok";
	public static final String CREATE_AUDITLOGS_ERROR = "auditlog.create.error";
	public static final String FETCH_AUDITLOGS_OK = "auditlog.fetch.ok";
	public static final String FETCH_AUDITLOGS_ERROR = "auditlog.fetch.error";
	public static final String AUDITLOGS_NOT_FOUND_ERROR = "auditlog.not.found.error";
	public static final String ADD_NEW_AUDITLOG_INVOKED_LOG = "auditlog.add.new.invoked.log.message";

	public static final String DELETE_USER_GROUP_MEMBERSHIP_OK = "usergroup.membership.delete.ok";
	public static final String STATUS_TAG_NOT_FOUND_ERROR = "status.tag.not.found.error";
	public static final String USER_MAPPED_TO_GROUP_SUCCESS = "usergroup.mapping.success";
	public static final String USER_MAPPED_TO_GROUP_ERROR = "usergroup.mapping.error";
	public static final String USER_ALREADY_EXISTS = "user.already.exists.error";
	public static final String USERGROUP_ALREADY_MAPPED_TO_USER_ERROR = "usergroup.already.mapped.error";
	public static final String ADMINISTRATOR_USER_GROUP_DELETE_ERROR = "usergroup.admin.delete.error";
	public static final String TOKEN_BLACKLISTED_ERROR = "token.blacklisted.error.message";
	public static final String TOKEN_BLACKLISTED_LOG_MESSAGE = "token.blacklisted.log.message";

	public static final String ADD_NEW_ROLE_LOG = "add.new.role.log";
	public static final String GET_ALL_ROLES_LOG = "get.all.roles.log";
	public static final String GET_ROLE_BY_ID_LOG = "get.role.by.id.log";
	public static final String UPDATE_ROLE_LOG = "update.role.log";
	public static final String DELETE_ROLE_LOG = "delete.role.log";
	public static final String ADD_OR_UPDATE_ROLE_LOG = "add.or.update.role.log";
	public static final String GET_ROLE_DETAILS_LOG = "get.role.details.log";
	public static final String GET_PERMISSIONS_DETAILS_LOG = "get.permissions.details.log";
	public static final String GET_SYSTEM_ROLES_LOG = "get.system.roles.log";
	public static final String GET_USERS_ROLE_DETAILS_LOG = "get.users.role.details.log";
	public static final String FIND_ALL_ROLES_BY_ID_STATUS_LOG = "find.all.roles.by.id.status.log";
	public static final String GET_ROLE_BY_ID_AND_STATUS_LOG = "get.role.by.id.and.status.log";
	public static final String GET_ROLE_DETAILS_WITH_PERMISSIONS_LOG = "get.role.details.with.permissions.log";
	public static final String GET_USERS_GROUPS_LOG = "get.users.groups.log";
	public static final String ADD_NEW_ROLE_PERMISSIONS_LOG = "add.new.role.permissions.log";
	public static final String UPDATE_ROLE_PERMISSIONS_LOG = "update.role.permissions.log";
	public static final String GET_ROLE_PERMISSIONS_BY_ID_LOG = "get.role.permissions.by.id.log";
	public static final String GET_ALL_ROLE_PERMISSIONS_LOG = "get.all.role.permissions.log";
	public static final String DELETE_ROLE_PERMISSION_LOG = "delete.role.permission.log";
	public static final String DELETE_ALL_ROLES_LOG = "delete.all.roles.log";
	public static final String ADD_OR_UPDATE_ROLE_WITH_PERMISSIONS_LOG = "add.or.update.role.with.permissions.log";
	public static final String UPDATE_ACL_ENTRIES_LOG = "update.acl.entries.log";
	public static final String UPDATE_ACL_ENTRIES_FOR_LOCATIONS_LOG = "update.acl.entries.locations.log";
	public static final String UPDATE_ACL_ENTRIES_FOR_FOLDERS_LOG = "update.acl.entries.folders.log";
	public static final String UPDATE_ACL_ENTRIES_FOR_FILES_LOG = "update.acl.entries.files.log";

	public static final String ADD_ALL_NEW_ROLE_PERMISSIONS_LOG = "add.all.new.role.permissions.log";
	public static final String GET_ALL_DISTINCT_PERMISSIONS_BY_ROLE_IDS_LOG = "get.all.distinct.permissions.by.role.ids.log";

	public static final String SEND_OTP_LOG = "send.otp.log";
	public static final String VERIFY_OTP_LOG = "verify.otp.log";
	public static final String RESET_PASSWORD_LOG = "reset.password.log";
	public static final String SEND_EMAIL_LOG = "send.email.log";

	public static final String WELCOME_MESSAGE = "email.welcome.message";
	public static final String CREATE_PASSWORD_LINK_MESSAGE = "email.create.password.link.message";
	public static final String LOGIN_LINK_MESSAGE = "email.login.link.message";
	public static final String REGISTERED_EMAIL_MESSAGE = "email.registered.email.message";
	public static final String THANK_YOU_MESSAGE = "email.thank.you.message";
	public static final String OTP_PROCEED_MESSAGE = "email.otp.proceed.message";
	public static final String EMAIL_GREETING_MESSAGE = "email.greeting.message";

	public static final String OTP_MESSAGE = "email.otp.message";
	public static final String OTP_EXPIRY_MESSAGE = "email.otp.expiry.message";
	public static final String OTP_WARNING_MESSAGE = "email.otp.warning";

	public static final String LOGIN_DENIED = "login.denied.exception";
	public static final String LOGIN_PASSWORD_EMPTY = "login.password.empty";
	public static final String ACCOUNT_EXIST_ERROR = "account.exist.error";
	public static final String ACCOUNT_LOGIN_ERROR = "account.login.error";
	public static final String ACCOUNT_LOGIN_OK = "account.login.ok";

	public static final String ACCOUNT_LOGOUT_ERROR = "account.logout.error";
	public static final String ACCOUNT_REFRESH_TOKEN_ERROR = "account.refresh.token.error";
	public static final String DELETE_ROLE_PERMISSION_ERROR = "delete.role.permission.error";
	
	public static final String ENTITY_NAME_EXIST = "entity.name.already.exist";
	public static final String ENTITY_NOT_EXIST = "entity.not.exist";
	public static final String CREATE_ENTITY_OK = "create.entity.ok";
	public static final String UPDATE_ENTITY_OK = "update.entity.ok";
	public static final String FETCH_ENTITY_OK = "fetch.entity.ok";
	public static final String DELETE_ENTITY_OK = "delete.entity.ok";
	
	public static final String PROJECT_NAME_EXIST = "project.name.already.exist";
	public static final String PROJECT_DISPLAY_NAME_EXIST = "project.display.name.already.exist";
	public static final String PROJECT_NOT_EXIST = "project.not.exist";
	public static final String CREATE_PROJECT_OK = "create.project.ok";
	public static final String UPDATE_PROJECT_OK = "update.project.ok";
	public static final String FETCH_PROJECT_OK = "fetch.project.ok";
	public static final String DELETE_PROJECT_OK = "delete.project.ok";
	public static final String FETCH_PROJECT_TAGS_OK = "fetch.project.tags.ok";
	
	public static final String CONTRACT_NOT_EXIST = "contract.not.exist";
	public static final String CREATE_CONTRACT_OK = "create.contract.ok";
	public static final String UPDATE_CONTRACT_OK = "update.contract.ok";
	public static final String FETCH_CONTRACT_OK = "fetch.contract.ok";
	public static final String DELETE_CONTRACT_OK = "delete.contract.ok";
	
	public static final String WORKFLOW_NOT_EXIST = "workflow.not.exist";
	public static final String CREATE_WORKFLOW_OK = "create.workflow.ok";
	public static final String UPDATE_WORKFLOW_OK = "update.workflow.ok";
	public static final String FETCH_WORKFLOW_OK = "fetch.workflow.ok";
	public static final String DELETE_WORKFLOW_OK = "delete.workflow.ok";
	
	public static final String FETCH_TASK_MANAGER_OK = "fetch.taskManager.ok";
	public static final String TASK_MANAGER_NOT_EXIST = "taskManager.not.exist";
	public static final String CREATE_TASK_MANAGER_OK = "create.taskManger.ok";
	public static final String CONTRACT_WORKFLOW_NOT_FOUND = "contract.workflow.not.exist";
	public static final String WORKFLOW_STEP_NOT_FOUND = "workflow.step.not.exist";
	public static final String UPDATE_TASK_MANAGER_OK = "update.taskManager.ok";
	public static final String DELETE_TASK_MANAGER_OK = "delete.taskManager.ok";
	public static final String TASK_STATUS_NOT_VALID = "task.status.not.valid";

	public static final String ENTITY_UPDATE_ERROR = "entity.update.error";
	public static final String PROJECT_UPDATE_ERROR = "project.update.error";
	public static final String CONTRACT_UPDATE_ERROR = "contract.update.error";
	public static final String FILE_NUMBER_UPDATE_ERROR = "file.number.update.error";
	
    public static final String FETCH_TASKS_FOR_USER_OK = "fetch.taskManager.loggedInUser.ok";
    
	public static final String FETCH_STEPS_OK = "fetch.steps.ok";
    public static final String TASK_ID_NOT_FOUND = "task.id.not.exist";
	public static final String RECORD_NOT_EXIST = "record.not.exist";
	public static final String UPDATE_RECORD_OK = "update.record.ok";
	public static final String FETCH_RECORD_OK = "fetch.record.ok";
	public static final String UPLOAD_RECORD_OK = "upload.record.ok";


	public static final String PATTERN_NOT_FOUND = "pattern.not.exist";
	public static final String INVALID_FILE = "invalid.file.type";
	public static final String FILE_NAME_DOES_NOT_MATCH_PATTERN = "file.does.not.match.pattern";
	public static final String UNSUPPORTED_DELIMITER_IN_PATTERN = "unsupported.delimiter.in.pattern";
	public static final String ERROR_SAVING_FILE = "error.saving.file";
	
	public static final String JSON_PROCESSING_EXCEPTION = "json.processing.exception";
	
	
	
}
