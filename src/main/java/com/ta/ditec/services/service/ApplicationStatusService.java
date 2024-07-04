package com.ta.ditec.services.service;

import com.ta.ditec.services.model.ApplicationStatus;
import com.ta.ditec.services.request.SubAUAApplicationStatusRequest;
import com.ta.ditec.services.response.SubAUAApplicationStatusResponse;

public interface ApplicationStatusService {

	public ApplicationStatus getApplicationStatus(ApplicationStatus req);

	public SubAUAApplicationStatusResponse getApplicationStatustracker(SubAUAApplicationStatusRequest request);
}
