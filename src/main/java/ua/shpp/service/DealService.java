package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.shpp.dto.*;
import ua.shpp.entity.payment.CheckEntity;
import ua.shpp.entity.payment.Checkable;
import ua.shpp.entity.payment.OneTimeInfoEntity;
import ua.shpp.entity.payment.SubscriptionInfoEntity;
import ua.shpp.exception.CheckNotFoundException;
import ua.shpp.exception.DealNotFoundException;
import ua.shpp.exception.VisitAlreadyUsedException;
import ua.shpp.mapper.CheckMapper;
import ua.shpp.mapper.OneTimeInfoMapper;
import ua.shpp.mapper.SubscriptionInfoMapper;
import ua.shpp.model.PaymentMethod;
import ua.shpp.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealService {
    private final OneTimeInfoRepository oneTimeInfoRepository;
    private final OneTimeOfferRepository oneTimeOfferRepository;
    private final SubscriptionInfoRepository subscriptionInfoRepository;
    private final SubscriptionOfferRepository subscriptionOfferRepository;
    private final CheckRepository checkRepository;
    private final ClientRepository clientRepository;

    private final OneTimeInfoMapper oneTimeInfoMapper;
    private final SubscriptionInfoMapper subscriptionInfoMapper;
    private final CheckMapper checkMapper;


    public OneTimeInfoResponseDto createOneTime(OneTimeInfoRequestDto dto, PaymentMethod paymentMethod) {
        log.info("createOneTime() called with DTO: {}, payment method: {}", dto, paymentMethod);
        var entity = oneTimeInfoMapper.toEntity(dto,
                clientRepository,
                oneTimeOfferRepository,
                checkRepository);
        entity.setId(null);
        var check = createCheck(entity, paymentMethod);
        log.debug("Created check entity for one-time deal {}", check);
        entity.setPaymentCheck(check);
        entity = oneTimeInfoRepository.save(entity);
        log.info("Created one-time deal (id={})", entity.getId());
        log.debug("create() one-time deal Entity: {}", entity);
        return oneTimeInfoMapper.toDto(entity);
    }

    public SubscriptionInfoResponseDto createSubscription(SubscriptionInfoRequestDto dto, PaymentMethod paymentMethod) {
        log.info("create() called with DTO: {}", dto);
        var entity = subscriptionInfoMapper.toEntity(dto,
                clientRepository,
                subscriptionOfferRepository,
                checkRepository);
        entity.setId(null);
        var check = createCheck(entity, paymentMethod);
        log.debug("Created check entity for subscription deal {}", check);
        entity.setPaymentCheck(check);
        entity = subscriptionInfoRepository.save(entity);
        log.info("Created subscription deal (id={})", entity.getId());
        log.debug("Created subscription deal entity: {}", entity);
        return subscriptionInfoMapper.toDto(entity);
    }

    public OneTimeInfoResponseDto getOneTimeById(Long id) {
        log.info("getOneTimeById() called with id: {}", id);
        var entity = getOneTimeEntityById(id);
        log.info("Fetching one-time deal (id={})", id);
        log.debug("Fetching one-time deal entity: {}", entity);
        return oneTimeInfoMapper.toDto(entity);
    }

    public SubscriptionInfoResponseDto getSubscriptionById(Long id) {
        log.info("getSubscriptionById() called with id: {}", id);
        var entity = getSubscriptionEntityById(id);
        log.info("Fetching subscription deal (id={})", id);
        log.debug("Fetching subscription deal entity: {}", entity);
        return subscriptionInfoMapper.toDto(entity);
    }

    public OneTimeInfoResponseDto visitOneTimeById(Long id) {
        log.info("visitOneTimeById() called with id: {}", id);
        var entity = getOneTimeEntityById(id);
        log.debug("Fetching one-time deal entity: {}", entity);
        if (Boolean.TRUE.equals(entity.getVisitUsed())) {
            throw new VisitAlreadyUsedException(String.format("One-time visit id: %d already used", id));
        }
        entity.setVisitUsed(true);
        entity = oneTimeInfoRepository.save(entity);
        log.info("OneTime with id: {}, visit: {}", id, entity.getVisitUsed());
        log.debug("Updated one-time deal entity: {}", entity);
        return oneTimeInfoMapper.toDto(entity);
    }

    public SubscriptionInfoResponseDto subscriptionVisitById(Long id) {
        log.info("subscriptionVisitById() called with id: {}", id);
        var entity = getSubscriptionEntityById(id);
        log.debug("Fetching subscription deal entity: {}", entity);
        if (entity.getVisits() <= 0) {
            throw new VisitAlreadyUsedException(String.format("Subscription visits id: %d already used", id));
        }
        Integer visits = entity.getVisits();
        entity.setVisits(--visits);
        entity = subscriptionInfoRepository.save(entity);
        log.info("Subscription with id: {}, visit: {}", id, entity.getVisits());
        log.debug("Updated subscription deal entity: {}", entity);
        return subscriptionInfoMapper.toDto(entity);
    }

    public CheckDto getCheckById(Long id) {
        log.info("getCheckById(() called with id: {}", id);
        var entity = checkRepository.findById(id).orElseThrow(
                () -> new CheckNotFoundException(String.format("Check id: %d not found", id)));
        log.debug("Fetching Check entity: {}", entity);
        return checkMapper.toDto(entity);
    }

    private OneTimeInfoEntity getOneTimeEntityById(Long id) {
        return oneTimeInfoRepository.findById(id).orElseThrow(
                () -> new DealNotFoundException(String.format("One-time visit id: %s, not found", id)));
    }

    public ValidVisitsDealDto validOneTimeVisitById(Long id) {
        return new ValidVisitsDealDto(id,
                getOneTimeEntityById(id).getVisitUsed(),
                1);
    }

    public ValidVisitsDealDto validSubscriptionById(Long id) {
        var entity = getSubscriptionEntityById(id);
        return new ValidVisitsDealDto(id,
                entity.getVisits() > 0,
                entity.getVisits());
    }


    private SubscriptionInfoEntity getSubscriptionEntityById(Long id) {
        return subscriptionInfoRepository.findById(id).orElseThrow(
                () -> new DealNotFoundException(String.format(" Subscription id: %s, not found", id)));
    }

    private <T extends Checkable> CheckEntity createCheck(T entity, PaymentMethod paymentMethod) {
        return switch (entity) {
            case OneTimeInfoEntity oneTimeInfoEntity -> CheckEntity
                    .builder()
                    .createdAt(LocalDateTime.now())
                    .organizationName(oneTimeInfoEntity
                            .getOneTimeService()
                            .getActivity()
                            .getBranch()
                            .getOrganization()
                            .getName())
                    .branchAddress(oneTimeInfoEntity
                            .getOneTimeService()
                            .getActivity()
                            .getBranch()
                            .getName() + " - no branch address") //todo Add Branch address and phone number
                    .branchPhoneNumber("no branch phone number")
                    .customerName(oneTimeInfoEntity.getClient().getName())
                    .customerPhoneNumber(oneTimeInfoEntity.getClient().getPhone())
                    .offerName(oneTimeInfoEntity.getOneTimeService().getActivity().getName())
                    .price(BigDecimal.valueOf(oneTimeInfoEntity.getOneTimeService().getPrice()))
                    .paymentMethod(paymentMethod)
                    .build();
            case SubscriptionInfoEntity subscriptionInfo -> CheckEntity
                    .builder()
                    .createdAt(LocalDateTime.now())
                    .organizationName(subscriptionInfo
                            .getSubscriptionService()
                            .getActivities().getFirst()
                            .getBranch()
                            .getOrganization()
                            .getName())
                    .branchAddress(subscriptionInfo
                            .getSubscriptionService()
                            .getActivities().getFirst()
                            .getBranch()
                            .getName() + " - no branch address") //  no address
                    .branchPhoneNumber("no branch phone number")
                    .customerName(subscriptionInfo.getClient().getName())
                    .customerPhoneNumber(subscriptionInfo.getClient().getPhone())
                    .offerName(subscriptionInfo.getSubscriptionService().getName())
                    .price(BigDecimal.valueOf(subscriptionInfo.getSubscriptionService().getPrice()))
                    .paymentMethod(paymentMethod)
                    .build();
            default -> throw new IllegalStateException("Unexpected value: " + entity.getClass().getName());
        };
    }


}
