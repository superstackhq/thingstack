package one.superstack.thingstack.service;

import one.superstack.thingstack.auth.actor.AuthenticatedActor;
import one.superstack.thingstack.enums.ActorType;
import one.superstack.thingstack.exception.ClientException;
import one.superstack.thingstack.exception.NotFoundException;
import one.superstack.thingstack.model.Group;
import one.superstack.thingstack.model.GroupMember;
import one.superstack.thingstack.model.User;
import one.superstack.thingstack.repository.GroupRepository;
import one.superstack.thingstack.request.GroupCreationRequest;
import one.superstack.thingstack.request.GroupMemberRequest;
import one.superstack.thingstack.request.GroupUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    private final GroupMemberService groupMemberService;

    private final UserService userService;

    private final AccessService accessService;

    @Autowired
    public GroupService(GroupRepository groupRepository, GroupMemberService groupMemberService, UserService userService, AccessService accessService) {
        this.groupRepository = groupRepository;
        this.groupMemberService = groupMemberService;
        this.userService = userService;
        this.accessService = accessService;
    }

    public Group create(GroupCreationRequest groupCreationRequest, AuthenticatedActor creator) {
        if (nameExists(groupCreationRequest.getName(), creator.getOrganizationId())) {
            throw new ClientException("Group " + groupCreationRequest.getName() + " already exists");
        }

        Group group = new Group(groupCreationRequest.getName(),
                groupCreationRequest.getDescription(),
                creator.getOrganizationId(),
                creator.getType(),
                creator.getId());

        return groupRepository.save(group);
    }

    public List<Group> listAll(String organizationId, Pageable pageable) {
        return groupRepository.findByOrganizationId(organizationId, pageable);
    }

    public List<Group> list(String userId, Pageable pageable) {
        List<String> groupIds = groupMemberService.listGroups(userId, pageable).stream().map(GroupMember::getGroupId).toList();
        return get(groupIds);
    }

    public Group get(String groupId) throws Throwable {
        return groupRepository.findById(groupId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Group not found"));
    }

    public Group get(String groupId, String organizationId) throws Throwable {
        return groupRepository.findByIdAndOrganizationId(groupId, organizationId)
                .orElseThrow((Supplier<Throwable>) () -> new NotFoundException("Group not found"));
    }

    @Async
    public CompletableFuture<List<Group>> asyncGet(List<String> groupIds) {
        return CompletableFuture.completedFuture(get(groupIds));
    }

    public List<Group> get(List<String> groupIds) {
        return groupRepository.findByIdIn(groupIds);
    }

    public Group update(String groupId, GroupUpdateRequest groupUpdateRequest, String organizationId) throws Throwable {
        Group group = get(groupId, organizationId);

        if (null != groupUpdateRequest.getName() && !groupUpdateRequest.getName().isBlank()) {
            if (groupRepository.existsByIdNotAndNameAndOrganizationId(groupId, groupUpdateRequest.getName(), organizationId)) {
                throw new ClientException("Group " + groupUpdateRequest.getName() + " already exists");
            }

            group.setName(groupUpdateRequest.getName());
        }

        group.setDescription(groupUpdateRequest.getDescription());
        group.setModifiedOn(new Date());

        return groupRepository.save(group);
    }

    public Group delete(String groupId, String organizationId) throws Throwable {
        Group group = get(groupId, organizationId);
        groupRepository.delete(group);
        groupMemberService.deleteAllForGroup(groupId);
        accessService.deleteAllForActor(ActorType.GROUP, groupId);
        return group;
    }

    public User addMember(String groupId, GroupMemberRequest groupMemberRequest, AuthenticatedActor creator) throws Throwable {
        if (!exists(groupId, creator.getOrganizationId())) {
            throw new NotFoundException("Group not found");
        }

        User user = userService.get(groupMemberRequest.getUserId(), creator.getOrganizationId());
        groupMemberService.add(groupId, groupMemberRequest.getUserId(), creator.getType(), creator.getId());
        return user;
    }

    public GroupMember deleteMember(String groupId, GroupMemberRequest groupMemberRequest, String organizationId) throws Throwable {
        if (!exists(groupId, organizationId)) {
            throw new NotFoundException("Group not found");
        }

        return groupMemberService.delete(groupId, groupMemberRequest.getUserId());
    }

    public List<User> listMembers(String groupId, String organizationId, Pageable pageable) {
        if (!exists(groupId, organizationId)) {
            throw new NotFoundException("Group not found");
        }

        List<String> userIds = groupMemberService.listUsers(groupId, pageable).stream().map(GroupMember::getUserId).toList();
        return userService.get(userIds);
    }

    public Boolean exists(String groupId, String organizationId) {
        return groupRepository.existsByIdAndOrganizationId(groupId, organizationId);
    }

    public List<Group> search(String query, String organizationId, Pageable pageable) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(query);
        return groupRepository.findByOrganizationIdOrderByScoreDesc(organizationId, textCriteria, pageable);
    }

    private Boolean nameExists(String name, String organizationId) {
        return groupRepository.existsByNameAndOrganizationId(name, organizationId);
    }
}
