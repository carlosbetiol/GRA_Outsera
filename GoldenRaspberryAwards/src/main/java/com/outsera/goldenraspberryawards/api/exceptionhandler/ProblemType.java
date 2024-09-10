package com.outsera.goldenraspberryawards.api.exceptionhandler;

import com.outsera.goldenraspberryawards.core.helper.ResourceUriHelper;
import com.outsera.goldenraspberryawards.core.internationalization.MessageSystem;
import lombok.Getter;

@Getter
public enum ProblemType {

	INVALID_DATA("/invalid-data", MessageSystem.getInstance().getLocalizedMessage("exception.invalidDataTitle")),
	ACCESS_DENIED("/access-denied", MessageSystem.getInstance().getLocalizedMessage("exception.accessDeniedTitle")),
	SYSTEM_ERROR("/system-error", MessageSystem.getInstance().getLocalizedMessage("exception.systemErrorTitle")),
	INVALID_PARAMETER("/invalid-parameter", MessageSystem.getInstance().getLocalizedMessage("exception.invalidParameterTitle")),
	INCOMPREHENSIBLE_MESSAGE("/incomprehensible-message", MessageSystem.getInstance().getLocalizedMessage("exception.incomprehensibleMessageTitle")),
	RESOURCE_NOT_FOUND("/resource-not-found", MessageSystem.getInstance().getLocalizedMessage("exception.resourceNotFoundTitle")),
	ENTITY_IN_USE("/entity-in-use", MessageSystem.getInstance().getLocalizedMessage("exception.entityInUseTitle")),
	BUSINESS_ERROR("/business-error", MessageSystem.getInstance().getLocalizedMessage("exception.businessErrorTitle")),
	REFERENCED_RESOURCE_NOT_FOUND("/referenced-resource-not-found", MessageSystem.getInstance().getLocalizedMessage("exception.referencedResourceNotFoundTitle")),;

	private String title;
	private String uri;

	ProblemType(String path, String title) {
		try {
			this.uri = ResourceUriHelper.getCurrentUrl() + path;
		} catch (Exception e) {
			this.uri = "unknown/" + path;
		}
		this.title = title;
	}
	
}
