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
import com.ta.ditec.services.model.MasterDataDetails;
import com.ta.ditec.services.repo.MasterDataDetailRepo;
import com.ta.ditec.services.request.MasterDataDetailsUpdateRequest;
import com.ta.ditec.services.request.PaginationRequest;
import com.ta.ditec.services.service.MasterDataDetailService;
import com.ta.ditec.services.utils.TAUtils;

@Service
public class MasterDataDetailServiceImpl implements MasterDataDetailService {

	 private static final Logger logger = LoggerFactory.getLogger(MasterDataDetailServiceImpl.class);
	@Autowired
	private MasterDataDetailRepo masterDataDetailRepo;

	@Override
	public MasterDataDetails masterdata(MasterDataDetails masterdata) {

		try {
			MasterDataDetails mastrdata = masterDataDetailRepo.save(masterdata);
			mastrdata.setDataId("Mas" + TAUtils.getId(masterdata.getId()));
			masterDataDetailRepo.save(masterdata);
			logger.info("Data saved successfully");
			return masterDataDetailRepo.save(masterdata);
		} catch (Exception e) {
			logger.error("Error from server as internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public List<MasterDataDetails> fetchMasterDataDetailList(PaginationRequest request) {

		Sort sort = request.getOrder().equalsIgnoreCase("ascending") ? Sort.by(request.getOrderBy()).ascending()
				: Sort.by(request.getOrderBy()).descending();

		Pageable paging = PageRequest.of(request.getStart(), request.getEnd(), sort);

		if (StringUtils.isEmpty(request.getViewType())) {
			if (request.getSearchBy() != null) {
				Page<MasterDataDetails> pagedResult = masterDataDetailRepo
						.findAllByDataTypeContainingOrDataParentContaining(request.getSearch(), request.getSearch(),
								paging);
				try {
					if (pagedResult != null) {
						return pagedResult.getContent();
					} else {
						logger.error("Data not found in database");
						throw new TaException(ErrorCode.NOT_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception occurred from the server side ");
					throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			} else {
				Page<MasterDataDetails> pageresult = masterDataDetailRepo.findAll(paging);
				logger.info("Data fetched successfully");
				try {
					if (pageresult != null) {
						
						return pageresult.getContent();
					} else {
						logger.error("Data not found in database");
						throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Exception as an internal server error occurred");
					throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			}
		} else {
			try {
				List<MasterDataDetails> details = masterDataDetailRepo.findAll();
				logger.info("Data fetched successfully");
				return details;
			} catch (Exception e) {
				logger.error("Data not found in database");
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}

		}
	}

	@Override
	public MasterDataDetails updateMasterDataDetail(MasterDataDetailsUpdateRequest agdetil) {
		try {
			if (agdetil != null && agdetil.getDataId() != null) {

				List<MasterDataDetails> depdetailsDB = masterDataDetailRepo.findByDataId(agdetil.getDataId());
				logger.info("Data fetched successfully");

				MasterDataDetails depDB = null;

				if (Objects.nonNull(depdetailsDB) && CollectionUtils.isNotEmpty(depdetailsDB)) {
					depDB = depdetailsDB.get(0);
				}

				if (Objects.nonNull(agdetil.getDataId()) && StringUtils.isNotEmpty(agdetil.getDataId())) {
					depDB.setDataId(agdetil.getDataId());
				}
				if (Objects.nonNull(agdetil.getDataType()) && StringUtils.isNotEmpty(agdetil.getDataType())) {
					depDB.setDataType(agdetil.getDataType());
				}

				if (Objects.nonNull(agdetil.getDataValue()) && StringUtils.isNotEmpty(agdetil.getDataValue())) {
					depDB.setDataValue(agdetil.getDataValue());
				}
				if (Objects.nonNull(agdetil.getDataParent()) && StringUtils.isNotEmpty(agdetil.getDataParent())) {
					depDB.setDataParent(agdetil.getDataParent());
				}
logger.info("Data saved successfully");
				return masterDataDetailRepo.save(depDB);
			} else {
				logger.error("Data not found in database");
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public void deleteMasterDataDetailById(String id) {
		try {
			if (id != null) {
				List<MasterDataDetails> d = masterDataDetailRepo.findByDataId(id);
				logger.info("Data fetched successfully");
				if (!CollectionUtils.isEmpty(d)) {
					masterDataDetailRepo.delete(d.get(0));
				} else {
					logger.error("Data not found in database");
					throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			} 
		} catch (Exception e) {
			logger.error("Error in server as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

}
