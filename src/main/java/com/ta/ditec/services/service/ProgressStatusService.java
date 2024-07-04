package com.ta.ditec.services.service;

import java.util.List;

import com.ta.ditec.services.model.ProgressStatus;
import com.ta.ditec.services.request.ProgressStatusRequest;

public interface ProgressStatusService {

	public List<ProgressStatus> getProgressStatus(ProgressStatusRequest req);
}
