package ua.shpp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.shpp.dto.RoomRequestDTO;
import ua.shpp.dto.RoomResponseDTO;
import ua.shpp.entity.BranchEntity;
import ua.shpp.entity.RoomEntity;
import ua.shpp.exception.BranchNotFoundException;
import ua.shpp.exception.RoomAlreadyExistsException;
import ua.shpp.exception.RoomNotFoundException;
import ua.shpp.mapper.RoomEntityToRoomDTOMapper;
import ua.shpp.repository.BranchRepository;
import ua.shpp.repository.RoomRepository;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final BranchRepository branchRepository;
    private final RoomEntityToRoomDTOMapper mapper;

    public RoomResponseDTO create(Long orgId, Long branchId, RoomRequestDTO requestDTO) {
        BranchEntity branchEntity = validateBranch(orgId, branchId);

        if (roomRepository.existsByNameAndBranchId(requestDTO.name(), branchId)) {
            throw new RoomAlreadyExistsException("Room with name " + requestDTO.name() + " already exists in this branch");
        }

        RoomEntity roomEntity = RoomEntity.builder()
                .name(requestDTO.name())
                .branch(branchEntity)
                .build();

        roomRepository.save(roomEntity);

        return mapper.roomEntityToRoomDTO(roomEntity);
    }

    public RoomResponseDTO get(Long orgId, Long branchId, Long roomId) {
        validateBranch(orgId, branchId);

        RoomEntity room = validateRoom(branchId, roomId);

        return mapper.roomEntityToRoomDTO(room);
    }

    public RoomResponseDTO patch(Long orgId, Long branchId, Long roomId, RoomRequestDTO requestDTO) {
        validateBranch(orgId, branchId);

        RoomEntity room = validateRoom(branchId, roomId);

        room.setName(requestDTO.name());

        roomRepository.save(room);

        return mapper.roomEntityToRoomDTO(room);
    }

    public RoomResponseDTO delete(Long orgId, Long branchId, Long roomId) {
        validateBranch(orgId, branchId);

        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room with id " + roomId + " not found"));

        roomRepository.delete(room);

        return mapper.roomEntityToRoomDTO(room);
    }

    private BranchEntity validateBranch(Long orgId, Long branchId) {
        BranchEntity branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException("Branch with id: " + branchId + " not found"));

        if (!branch.getOrganization().getId().equals(orgId)) {
            throw new IllegalArgumentException("Branch does not belong to the given organization");
        }

        return branch;
    }

    private RoomEntity validateRoom(Long branchId, Long roomId) {
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room with id " + roomId + " not found"));

        if (!room.getBranch().getId().equals(branchId)) {
            throw new IllegalArgumentException("Room does not belong to the branch");
        }

        return room;
    }


}
