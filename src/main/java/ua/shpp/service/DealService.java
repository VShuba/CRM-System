package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.shpp.dto.*;
import ua.shpp.entity.Organization;
import ua.shpp.entity.payment.CheckEntity;
import ua.shpp.entity.payment.Checkable;
import ua.shpp.entity.payment.OneTimeDealEntity;
import ua.shpp.entity.payment.SubscriptionDealEntity;
import ua.shpp.exception.CheckNotFoundException;
import ua.shpp.exception.DealNotFoundException;
import ua.shpp.exception.VisitAlreadyUsedException;
import ua.shpp.mapper.CheckMapper;
import ua.shpp.mapper.OneTimeDealMapper;
import ua.shpp.mapper.SubscriptionDealMapper;
import ua.shpp.model.ClientEventStatus;
import ua.shpp.model.PaymentMethod;
import ua.shpp.repository.*;
import ua.shpp.service.history.SubscriptionHistoryService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealService {
    private final OneTimeDealRepository oneTimeDealRepository;
    private final OneTimeOfferRepository oneTimeOfferRepository;
    private final SubscriptionDealRepository subscriptionDealRepository;
    private final SubscriptionOfferRepository subscriptionOfferRepository;
    private final CheckRepository checkRepository;
    private final ClientRepository clientRepository;
    private final OneTimeDealMapper oneTimeDealMapper;
    private final SubscriptionDealMapper subscriptionDealMapper;
    private final CheckMapper checkMapper;
    private final EventClientService eventClientService;
    private final SubscriptionHistoryService subscriptionHistoryService;

    public OneTimeDealResponseDto createOneTime(OneTimeDealRequestDto dto, PaymentMethod paymentMethod) {
        log.info("createOneTime() called with DTO: {}, payment method: {}", dto, paymentMethod);
        var entity = oneTimeDealMapper.toEntity(dto,
                clientRepository,
                oneTimeOfferRepository,
                checkRepository);
        isClientAndOfferFromSameOrganization(entity);
        var check = createCheck(entity, paymentMethod);
        log.info("Created check id:{} for one-time deal", check.getId());
        log.debug("Created check entity for one-time deal {}", check);
        entity.setPaymentCheck(check);
        entity = oneTimeDealRepository.save(entity);
        log.info("Created one-time deal (id={})", entity.getId());
        log.debug("create() one-time deal Entity: {}", entity);
        return oneTimeDealMapper.toDto(entity);
    }

    public SubscriptionDealResponseDto createSubscription(SubscriptionDealRequestDto dto, PaymentMethod paymentMethod) {
        log.info("create() called with DTO: {} , payment method: {}", dto, paymentMethod);
        var entity = subscriptionDealMapper.toEntity(dto,
                clientRepository,
                subscriptionOfferRepository,
                checkRepository);
        isClientAndOfferFromSameOrganization(entity);
        var check = createCheck(entity, paymentMethod);
        log.info("Created check id:{} for for subscription deal", check.getId());
        log.debug("Created check entity for subscription deal {}", check);
        entity.setPaymentCheck(check);
        entity = subscriptionDealRepository.save(entity);
        log.info("Created subscription deal (id={})", entity.getId());
        log.debug("Created subscription deal entity: {}", entity);

        // Після покупки підписки автоматично запис в історію підписок
        subscriptionHistoryService.createSubscriptionHistory(entity);
        log.debug("Requested creation of subscription history for SubscriptionInfo ID: {}", entity.getId());

        log.debug("Mapping saved SubscriptionInfo back to Response DTO");
        return subscriptionDealMapper.toDto(entity);
    }

    public OneTimeDealResponseDto getOneTimeById(Long id) {
        log.info("getOneTimeById() called with id: {}", id);
        var entity = getOneTimeEntityById(id);
        log.info("Fetching one-time deal (id={})", id);
        log.debug("Fetching one-time deal entity: {}", entity);
        return oneTimeDealMapper.toDto(entity);
    }

    public SubscriptionDealResponseDto getSubscriptionById(Long id) {
        log.info("getSubscriptionById() called with id: {}", id);
        var entity = getSubscriptionEntityById(id);
        log.info("Fetching subscription deal (id={})", id);
        log.debug("Fetching subscription deal entity: {}", entity);
        return subscriptionDealMapper.toDto(entity);
    }

    @Transactional
    public OneTimeDealResponseDto visitOneTimeByIdAndScheduleEventId(Long oneTimeId, Long scheduleEventId) {
        log.info("visitOneTimeByIdAndScheduleEventId() called with one-time id: {}, schedule id: {} ",
                oneTimeId, scheduleEventId);
        var entity = getOneTimeEntityById(oneTimeId);
        log.debug("Fetching one-time deal entity: {}", entity);
        if (Boolean.TRUE.equals(entity.getVisitUsed())) {
            throw new VisitAlreadyUsedException(String.format("One-time visit id: %d already used", oneTimeId));
        }
        entity.setVisitUsed(true);
        entity = oneTimeDealRepository.save(entity);
        log.info("OneTime with id: {}, visit: {}", oneTimeId, entity.getVisitUsed());
        log.debug("Updated one-time deal entity: {}", entity);

        eventClientService
                .changeClientStatus(new EventClientDto(entity.getClient().getId(),
                        scheduleEventId, ClientEventStatus.USED, oneTimeId, null));
        log.info("Set one-time id: {}, schedule id: {},  status  {}",
                oneTimeId, scheduleEventId, ClientEventStatus.USED);
        return oneTimeDealMapper.toDto(entity);
    }

    @Transactional
    public SubscriptionDealResponseDto subscriptionVisitByIdAndScheduleEventId( // todo fix relation
                                                                                Long subscriptionId, Long scheduleEventId) {
        log.info("subscriptionVisitByIdAndScheduleEventId() called with subscription id: {}, schedule id: {}",
                subscriptionId, scheduleEventId);
        var entity = getSubscriptionEntityById(subscriptionId);
        log.debug("Fetching subscription deal entity: {}", entity);
        if (entity.getVisits() <= 0) {
            throw new VisitAlreadyUsedException(String.format("Subscription visits id: %d already used", subscriptionId));
        }
        Integer visits = entity.getVisits();
        entity.setVisits(--visits);
        entity = subscriptionDealRepository.save(entity);
        log.info("Subscription with id: {}, visit: {}", subscriptionId, entity.getVisits());
        log.debug("Updated subscription deal entity: {}", entity);

        // Викликаємо сервіс історії для оновлення visits_left, передаючи оновлену сутність
        subscriptionHistoryService.updateHistoryVisitsRemaining(entity);
        log.debug("Requested update of visits_left in history for SubscriptionInfo ID: {}", entity.getId());

        eventClientService
                .changeClientStatus(new EventClientDto(entity.getClient().getId(),
                        scheduleEventId, ClientEventStatus.USED, null, subscriptionId));
        log.info("Set subscription id: {}, schedule id: {},  status  {}",
                subscriptionId, scheduleEventId, ClientEventStatus.USED);
        return subscriptionDealMapper.toDto(entity);
    }

    public CheckDto getCheckById(Long id) {
        log.info("getCheckById(() called with id: {}", id);
        var entity = checkRepository.findById(id).orElseThrow(
                () -> new CheckNotFoundException(String.format("Check id: %d not found", id)));
        log.debug("Fetching Check entity: {}", entity);
        return checkMapper.toDto(entity);
    }

    private OneTimeDealEntity getOneTimeEntityById(Long id) {
        return oneTimeDealRepository.findById(id).orElseThrow(
                () -> new DealNotFoundException(String.format("One-time visit id: %s, not found", id)));
    }

    private SubscriptionDealEntity getSubscriptionEntityById(Long id) {
        return subscriptionDealRepository.findById(id).orElseThrow(
                () -> new DealNotFoundException(String.format(" Subscription id: %s, not found", id)));
    }

    private <T extends Checkable> CheckEntity createCheck(T entity, PaymentMethod paymentMethod) {
        return switch (entity) {
            case OneTimeDealEntity oneTimeDealEntity -> CheckEntity
                    .builder()
                    .createdAt(LocalDateTime.now())
                    .organizationName(oneTimeDealEntity
                            .getOneTimeService()
                            .getActivity()
                            .getBranch()
                            .getOrganization()
                            .getName())
                    .branchAddress(oneTimeDealEntity
                            .getOneTimeService()
                            .getActivity()
                            .getBranch()
                            .getAddress())
                    .branchPhoneNumber(oneTimeDealEntity
                            .getOneTimeService()
                            .getActivity()
                            .getBranch()
                            .getPhoneNumber())
                    .customerName(oneTimeDealEntity.getClient().getName())
                    .customerPhoneNumber(oneTimeDealEntity.getClient().getPhone())
                    .offerName(oneTimeDealEntity.getOneTimeService().getActivity().getName())
                    .price(BigDecimal.valueOf(oneTimeDealEntity.getOneTimeService().getPrice()))
                    .paymentMethod(paymentMethod)
                    .build();
            case SubscriptionDealEntity subscriptionDealEntity -> CheckEntity
                    .builder()
                    .createdAt(LocalDateTime.now())
                    .organizationName(subscriptionDealEntity
                            .getSubscriptionService()
                            .getActivities().getFirst()
                            .getBranch()
                            .getOrganization()
                            .getName())
                    .branchAddress(subscriptionDealEntity
                            .getSubscriptionService()
                            .getActivities().getFirst()
                            .getBranch()
                            .getAddress()) //  no address
                    .branchPhoneNumber(subscriptionDealEntity
                            .getSubscriptionService()
                            .getActivities().getFirst()
                            .getBranch()
                            .getPhoneNumber())
                    .customerName(subscriptionDealEntity.getClient().getName())
                    .customerPhoneNumber(subscriptionDealEntity.getClient().getPhone())
                    .offerName(subscriptionDealEntity.getSubscriptionService().getName())
                    .price(BigDecimal.valueOf(subscriptionDealEntity.getSubscriptionService().getPrice()))
                    .paymentMethod(paymentMethod)
                    .build();
        };
    }

    private <T extends Checkable> void isClientAndOfferFromSameOrganization(T entity) {
        Organization clientOrganisation = null;
        Organization offerOrganisation = null;

        if (entity instanceof OneTimeDealEntity oneTimeDealEntity) {
            clientOrganisation = oneTimeDealEntity
                    .getClient()
                    .getOrganization();
            offerOrganisation = oneTimeDealEntity
                    .getOneTimeService()
                    .getActivity()
                    .getBranch()
                    .getOrganization();
        } else if (entity instanceof SubscriptionDealEntity subscriptionDealEntity) {
            clientOrganisation = subscriptionDealEntity
                    .getClient()
                    .getOrganization();
            offerOrganisation = subscriptionDealEntity
                    .getSubscriptionService()
                    .getActivities().getFirst()
                    .getBranch()
                    .getOrganization();
        }
        if (Objects.nonNull(clientOrganisation)
                && Objects.nonNull(offerOrganisation)
                && !clientOrganisation.equals(offerOrganisation)) {
            log.warn(String.format("""
                            create() called with diferent odganisations id!
                            Clien oganisation id: %d;
                            Subscription offer organisation id: %d;
                            """,
                    clientOrganisation.getId(),
                    offerOrganisation.getId()));
            throw new AccessDeniedException("Client and subscription offer belong to different organizations");
        }
    }
}