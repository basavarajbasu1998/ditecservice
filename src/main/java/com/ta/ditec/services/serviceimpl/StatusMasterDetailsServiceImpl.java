package com.ta.ditec.services.serviceimpl;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.model.StatusMasterDetails;
import com.ta.ditec.services.repo.StatusMasterDetailsRepository;
import com.ta.ditec.services.request.PaginationRequest;
import com.ta.ditec.services.request.StatusMasterDetailsUpdateRequest;
import com.ta.ditec.services.service.StatusMasterDetailsService;
import com.ta.ditec.services.utils.TAUtils;

@Service
public class StatusMasterDetailsServiceImpl implements StatusMasterDetailsService {
	
	 private static final Logger logger = LoggerFactory.getLogger(StatusMasterDetailsServiceImpl.class);

	@Autowired
	private StatusMasterDetailsRepository statusMasterDetailsRepository;

	@Override
	public StatusMasterDetails saveDetails(StatusMasterDetails request) {
		try {
			StatusMasterDetails smdetl = statusMasterDetailsRepository.save(request);
			smdetl.setStatusId("Sta" + TAUtils.getId(smdetl.getId()));
			return statusMasterDetailsRepository.save(smdetl);
		} catch (Exception e) {
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public List<StatusMasterDetails> fetchStatusMasterDetails(PaginationRequest request) {
		
		Sort sort = request.getOrder().equalsIgnoreCase("ascending") ? Sort.by(request.getOrderBy()).ascending()
				: Sort.by(request.getOrderBy()).descending();

		Pageable paging = PageRequest.of(request.getStart(), request.getEnd(), sort);

		if (StringUtils.isEmpty(request.getViewType())) {
			if (request.getSearchBy() != null) {
				Page<StatusMasterDetails> pagedResult = statusMasterDetailsRepository
						.findAllByStatusNameContaining(request.getSearch(),
								 paging);
				logger.info("Data fetched successfully");

				try {
					if (pagedResult != null) {
						return pagedResult.getContent();
					} else {
						logger.error("Data not found in the database");
						throw new TaException(ErrorCode.NOT_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
					}
				} catch (Exception e) {
					e.printStackTrace();
					
					throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			} else {
				Page<StatusMasterDetails> pageresult = statusMasterDetailsRepository.findAll(paging);
				logger.info("Data fetched successfully");
				try {
					if (pageresult != null) {
						return pageresult.getContent();
					} else {
						throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			}
		} else {
			try {
				List<StatusMasterDetails> details = statusMasterDetailsRepository.findAll();
				logger.info("Data fetched successfully");
				return details;
			} catch (Exception e) {
				logger.error("Data not found in the database");
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}

		}

	}

	@Override
	public StatusMasterDetails UpdateStatusMasterDetails(StatusMasterDetailsUpdateRequest smdetil) {
		try {
			if (smdetil != null && smdetil.getStatusId() != null) {
				List<StatusMasterDetails> depDBDetails = statusMasterDetailsRepository
						.findByStatusId(smdetil.getStatusId());
				logger.info("Data fetched successfully");
				StatusMasterDetails depDB = null;
				if (Objects.nonNull(depDBDetails) && CollectionUtils.isNotEmpty(depDBDetails)) {
					depDB = depDBDetails.get(0);
				}
				if (Objects.nonNull(smdetil.getStatusName()) && StringUtils.isNotEmpty(smdetil.getStatusName())) {
					depDB.setStatusName(smdetil.getStatusName());
				}

				return statusMasterDetailsRepository.save(depDB);
			} else {
				logger.error("Data not found in the database");
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}

		} catch (Exception e) {
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public void deleteUser(String id) {
		try {
			if (id != null) {
				List<StatusMasterDetails> d = statusMasterDetailsRepository.findByStatusId(id);
				logger.info("Data fetched successfully");
				if(!CollectionUtils.isEmpty(d)) {
					statusMasterDetailsRepository.delete(d.get(0));
				}
				else {
					logger.error("Data not found in the database");
					throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			} 
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

}
