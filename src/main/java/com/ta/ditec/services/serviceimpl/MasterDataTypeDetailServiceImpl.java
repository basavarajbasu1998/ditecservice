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
import com.ta.ditec.services.model.MasterDataTypeDetails;
import com.ta.ditec.services.repo.MasterDataTypeDetailRepo;
import com.ta.ditec.services.request.MasterDataTypeDetailsUpdateRequest;
import com.ta.ditec.services.request.PaginationRequest;
import com.ta.ditec.services.service.MasterDataTypeDetailService;
import com.ta.ditec.services.utils.TAUtils;

@Service
public class MasterDataTypeDetailServiceImpl implements MasterDataTypeDetailService {
	 private static final Logger logger = LoggerFactory.getLogger(MasterDataTypeDetailServiceImpl.class);

	@Autowired
	private MasterDataTypeDetailRepo masterDataTypeDetailRepo;

	@Override
	public MasterDataTypeDetails mdtypedetl(MasterDataTypeDetails mdtypedetl) {
		try {
			MasterDataTypeDetails mastdatatype = masterDataTypeDetailRepo.save(mdtypedetl);
			logger.info("Data saved successfully");
			mastdatatype.setDataTypeId("Mas" + TAUtils.getId(mastdatatype.getId()));
			logger.info("Data saved successfully ");
			return masterDataTypeDetailRepo.save(mastdatatype);
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public List<MasterDataTypeDetails> fetchMasterDataTypeDetailList(PaginationRequest request) {

		Sort sort = request.getOrder().equalsIgnoreCase("ascending") ? Sort.by(request.getOrderBy()).ascending()
				: Sort.by(request.getOrderBy()).descending();

		Pageable paging = PageRequest.of(request.getStart(), request.getEnd(), sort);

		if (StringUtils.isEmpty(request.getViewType())) {
			if (request.getSearchBy() != null) {
				Page<MasterDataTypeDetails> pagedResult = masterDataTypeDetailRepo
						.findAllBydataTypeNameContaining(request.getSearch(), paging);
				logger.info("Data fetched from database successfully");
				try {
					if (pagedResult != null) {
						return pagedResult.getContent();
					} else {
						logger.error("Data not found in database");
						throw new TaException(ErrorCode.NOT_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error occurred from server side");
					throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			} else {
				Page<MasterDataTypeDetails> pageresult = masterDataTypeDetailRepo.findAll(paging);
				logger.info("Data fetched suucccessfully");
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
				List<MasterDataTypeDetails> details = masterDataTypeDetailRepo.findAll();
				logger.info("Data fetched suucccessfully");
				return details;
			} catch (Exception e) {
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}

		}

	}

	@Override
	public MasterDataTypeDetails updateMasterDataTypeDetail(MasterDataTypeDetailsUpdateRequest agdetil) {

		try {
			if (agdetil != null && agdetil.getDataTypeId() != null) {

				List<MasterDataTypeDetails> depDBDetails = masterDataTypeDetailRepo
						.findByDataTypeId(agdetil.getDataTypeId());
				logger.info("Data fetched successfully");

				MasterDataTypeDetails depDB = null;

				if (Objects.nonNull(depDBDetails) && CollectionUtils.isNotEmpty(depDBDetails)) {
					depDB = depDBDetails.get(0);
				}

				if (Objects.nonNull(agdetil.getDataTypeId()) && StringUtils.isNotEmpty(agdetil.getDataTypeId())) {
					depDB.setDataTypeId(agdetil.getDataTypeId());
				}

				if (Objects.nonNull(agdetil.getDataTypeName()) && StringUtils.isNotEmpty(agdetil.getDataTypeName())) {
					depDB.setDataTypeName(agdetil.getDataTypeName());
				}

				if (Objects.nonNull(agdetil.getDataTypeStatus())
						&& StringUtils.isNotEmpty(agdetil.getDataTypeStatus())) {
					depDB.setDataTypeStatus(agdetil.getDataTypeStatus());
				}
				return masterDataTypeDetailRepo.save(depDB);
			} else {
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}

		} catch (Exception e) {
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public void deleteMasterDataTypeDetailById(String id) {
		try {
			if (id != null) {
				List<MasterDataTypeDetails> d = masterDataTypeDetailRepo.findByDataTypeId(id);
				logger.info("Data fetched successfully");
				if (!CollectionUtils.isEmpty(d)) {
					masterDataTypeDetailRepo.delete(d.get(0));
				} else {
					throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			} else {
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}
		} catch (Exception e) {
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

}
