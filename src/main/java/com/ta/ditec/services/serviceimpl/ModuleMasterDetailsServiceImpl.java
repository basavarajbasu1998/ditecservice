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
import com.ta.ditec.services.model.ModuleMasterDetails;
import com.ta.ditec.services.repo.ModuleMasterDetailsRepository;
import com.ta.ditec.services.request.ModuleMasterDetailsUpdateRequest;
import com.ta.ditec.services.request.PaginationRequest;
import com.ta.ditec.services.service.ModuleMasterDetailsService;
import com.ta.ditec.services.utils.TAUtils;

@Service
public class ModuleMasterDetailsServiceImpl implements ModuleMasterDetailsService {
	
	 private static final Logger logger = LoggerFactory.getLogger(ModuleMasterDetailsServiceImpl.class);

	@Autowired
	private ModuleMasterDetailsRepository moduleMasterDetailsRepository;

	@Override
	public ModuleMasterDetails saveDetails(ModuleMasterDetails request) {
		try {
			ModuleMasterDetails mmdetil = moduleMasterDetailsRepository.save(request);
			logger.info("Data saved successfully");
			mmdetil.setModuleId("Mod" + TAUtils.getId(mmdetil.getId()));
			logger.info("Data saved successfully");
			return moduleMasterDetailsRepository.save(mmdetil);
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public List<ModuleMasterDetails> fetchMasterDataTypeDetailList(PaginationRequest request) {
		
		Sort sort = request.getOrder().equalsIgnoreCase("ascending") ? Sort.by(request.getOrderBy()).ascending()
				: Sort.by(request.getOrderBy()).descending();

		Pageable paging = PageRequest.of(request.getStart(), request.getEnd(), sort);

		if (StringUtils.isEmpty(request.getViewType())) {
			if (request.getSearchBy() != null) {
				Page<ModuleMasterDetails> pagedResult = moduleMasterDetailsRepository
						.findAllByModuleNameContaining(request.getSearch(), paging);
				try {
					if (pagedResult != null) {
						return pagedResult.getContent();
					} else {
						throw new TaException(ErrorCode.NOT_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occurred as an internal server error");
					throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			} else {
				Page<ModuleMasterDetails> pageresult = moduleMasterDetailsRepository.findAll(paging);
				try {
					if (pageresult != null) {
						return pageresult.getContent();
					} else {
						throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
					}
				} catch (TaException e) {
					throw e;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occurred as an internal server error");
					throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			}
		} else {
			try {
				List<ModuleMasterDetails> details = moduleMasterDetailsRepository.findAll();
				return details;
			} catch (TaException e) {
				throw e;
			} catch (Exception e) {
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}

		}


	}

	@Override
	public ModuleMasterDetails updateModuleMasterDetails(ModuleMasterDetailsUpdateRequest mmdetl) {

		try {
			if (mmdetl != null && mmdetl.getModuleId() != null) {

				List<ModuleMasterDetails> depDBDetails = moduleMasterDetailsRepository
						.findByModuleId(mmdetl.getModuleId());
				logger.info("Data fetched successfully");

				ModuleMasterDetails depDB = null;

				if (Objects.nonNull(depDBDetails) && CollectionUtils.isNotEmpty(depDBDetails)) {
					depDB = depDBDetails.get(0);
				}

				if (Objects.nonNull(mmdetl.getModuleName()) && StringUtils.isNotEmpty(mmdetl.getModuleName())) {
					depDB.setModuleName(mmdetl.getModuleName());
				}

				if (Objects.nonNull(mmdetl.getModuleIcon()) && StringUtils.isNotEmpty(mmdetl.getModuleIcon())) {
					depDB.setModuleIcon(mmdetl.getModuleIcon());
				}

				if (Objects.nonNull(mmdetl.getModuleUrl()) && StringUtils.isNotEmpty(mmdetl.getModuleUrl())) {
					depDB.setModuleUrl(mmdetl.getModuleUrl());
				}
				return moduleMasterDetailsRepository.save(depDB);
			} else {
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public void deleteUser(String id) {
		try {
			if (id != null) {
				List<ModuleMasterDetails> d = moduleMasterDetailsRepository.findByModuleId(id);
				logger.info("Data fetched successfully");
				if(!CollectionUtils.isEmpty(d)) {
					moduleMasterDetailsRepository.delete(d.get(0));
				}
				else {
					throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			} else {
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

}
