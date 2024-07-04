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
import com.ta.ditec.services.model.UserBillingMasterDetails;
import com.ta.ditec.services.repo.UserBillingMasterDetailsRepo;
import com.ta.ditec.services.request.PaginationRequest;
import com.ta.ditec.services.request.UserBillingMasterDetailsFindIdRequest;
import com.ta.ditec.services.request.UserMasterBillingCheckDatarequest;
import com.ta.ditec.services.service.UserBillingMasterDetailsService;
import com.ta.ditec.services.utils.TAUtils;

@Service
public class UserBillingMasterDetailsServiceimpl implements UserBillingMasterDetailsService {
	
	 private static final Logger logger = LoggerFactory.getLogger(UserBillingMasterDetailsServiceimpl.class);

	@Autowired
	private UserBillingMasterDetailsRepo userBillingMasterDetailsRepo;

	@Override
	public UserBillingMasterDetails userbillmastdetil(UserBillingMasterDetails userbillmastdetil) {
		try {
//			List<UserBillingMasterDetails> details = userBillingMasterDetailsRepo
//					.findByUserId(userbillmastdetil.getUserId());
//			System.out.println(details);
//
//			if (CollectionUtils.isNotEmpty(details)) {
//
//				String BillingId = details.get(0).getBillingId();
//				userbillmastdetil.setBillingId(BillingId);
//				return updateUserBillingMasterDetails(userbillmastdetil);
//			}

			UserBillingMasterDetails userbillmd = userBillingMasterDetailsRepo.save(userbillmastdetil);
			logger.info("Data saved successfully");
			userbillmd.setEntitylevel("Sub AUA-KUA");
			userbillmastdetil.setBillingId("USE" + TAUtils.getId(userbillmastdetil.getId()));
			System.out.println("repository before");
			System.out.println(userbillmd);
			logger.info("Data saved successfully");
			return userBillingMasterDetailsRepo.save(userbillmd);

		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public Page<UserBillingMasterDetails> fetchRegisterAgencyDetailsList(PaginationRequest request) {

		if (StringUtils.isEmpty(request.getViewType())) {
			if (StringUtils.isNotEmpty(request.getOrderBy()) && StringUtils.isNotEmpty(request.getOrder())) {

				if (!StringUtils.isAllEmpty(request.getSearchBy())) {

					Pageable paging = PageRequest.of(request.getStart(), request.getEnd());

					Page<UserBillingMasterDetails> pagedResult = userBillingMasterDetailsRepo
							.findAllByBillingtypeContainingOrAgencybillingCycleContaining(request.getSearch(),
									request.getSearch(), paging);

					if (pagedResult != null) {
						return pagedResult;
					} else {
						logger.error("Data not found in the database");
						throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
					}
				}
				Sort sort = request.getOrder().equalsIgnoreCase("ascending") ? Sort.by(request.getOrderBy()).ascending()
						: Sort.by(request.getOrderBy()).descending();

				Pageable paging = PageRequest.of(request.getStart(), request.getEnd(), sort);

				Page<UserBillingMasterDetails> details = userBillingMasterDetailsRepo.findAll(paging);
				logger.info("Data fetched successfully");

				return details;

			} else {
				Pageable paging = PageRequest.of(request.getStart(), request.getEnd());
				Page<UserBillingMasterDetails> details = userBillingMasterDetailsRepo.findAll(paging);
				logger.info("Data fetched successfully");
				return details;
			}

		} else {
			try {
				Pageable paging = PageRequest.of(0, Integer.MAX_VALUE);
				System.out.println("else part exceuted");
				Page<UserBillingMasterDetails> details = userBillingMasterDetailsRepo.findAll(paging);
				logger.info("Data fetched successfully");
				return details;
			} catch (Exception e) {
				logger.info("Data not found in database  ");
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}

		}

	}

	@Override
	public UserBillingMasterDetails updateUserBillingMasterDetails(UserBillingMasterDetails agdetil) {

		System.out.println("update the formmmm" + agdetil);

		if (StringUtils.isNotEmpty(agdetil.getBillingId())) {

			List<UserBillingMasterDetails> depDBDetails = userBillingMasterDetailsRepo
					.findByBillingId(agdetil.getBillingId());
			

			logger.info("Data fetched successfully");
			UserBillingMasterDetails depDB = null;

			if (Objects.nonNull(depDBDetails) && CollectionUtils.isNotEmpty(depDBDetails)) {
				depDB = depDBDetails.get(0);
			}

//				if (Objects.nonNull(agdetil.getEntitylevel()) && StringUtils.isNotEmpty(agdetil.getEntitylevel())) {
//					depDB.setEntitylevel(agdetil.getEntitylevel());
//				}

			if (Objects.nonNull(agdetil.getBillingtype()) && StringUtils.isNotEmpty(agdetil.getBillingtype())) {
				depDB.setBillingtype(agdetil.getBillingtype());
			}

			if (Objects.nonNull(agdetil.getAgencybillingCycle())
					&& StringUtils.isNotEmpty(agdetil.getAgencybillingCycle())) {
				depDB.setAgencybillingCycle(agdetil.getAgencybillingCycle());
			}

			if (Objects.nonNull(agdetil.getDepartmentName()) && StringUtils.isNotEmpty(agdetil.getDepartmentName())) {
				depDB.setDepartmentName(agdetil.getDepartmentName());
			}

			if (Objects.nonNull(agdetil.getServicesStartDateandtime())
					&& StringUtils.isNotEmpty(agdetil.getServicesStartDateandtime())) {
				depDB.setServicesStartDateandtime(agdetil.getServicesStartDateandtime());
			}

			if (Objects.nonNull(agdetil.getServicesEndDateandtime())
					&& StringUtils.isNotEmpty(agdetil.getServicesEndDateandtime())) {
				depDB.setServicesEndDateandtime(agdetil.getServicesEndDateandtime());
			}

			if (Objects.nonNull(agdetil.getDeptID()) && StringUtils.isNotEmpty(agdetil.getDeptID())) {
				depDB.setDeptID(agdetil.getDeptID());
			}
logger.info("Data saved successfully to database");
			return userBillingMasterDetailsRepo.save(depDB);

		} else {
			logger.error("Data not found in the database");
			throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
		}
	}

	@Override
	public void deleteUserBillingMasterDetailsById(String id) {
		try {
			if (id != null) {
				List<UserBillingMasterDetails> d = userBillingMasterDetailsRepo.findByBillingId(id);
				
				logger.info("Data fetched successfully");
				if (!CollectionUtils.isEmpty(d)) {
					userBillingMasterDetailsRepo.delete(d.get(0));
				} else {
					logger.error("Data not found in the database");
					throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			} else {
				logger.error("Data not found in the database");
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public UserBillingMasterDetails fetchFindAng(UserBillingMasterDetailsFindIdRequest req) {
		try {
			if (req != null) {
				List<UserBillingMasterDetails> d = userBillingMasterDetailsRepo.findByBillingId(req.getBillingId());
				
				logger.info("Data fetched successfully");
				if (d != null) {

					return d.get(0);
				} else {
					logger.error("Data not found in the database");
					throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			} 
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}
		return null;

	}

	@Override
	public UserBillingMasterDetails checkUserBillingData(UserMasterBillingCheckDatarequest request) {
		List<UserBillingMasterDetails> details = userBillingMasterDetailsRepo.findByUserId(request.getUserId());
		logger.info("Data fetched successfully");
		if (CollectionUtils.isNotEmpty(details)) {
			return details.get(0);
		} else {
			logger.error("Data not found in the database");
			throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
		}
	}

}
