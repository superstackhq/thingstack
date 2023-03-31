package one.superstack.thingstack.controller;

import jakarta.validation.Valid;
import one.superstack.thingstack.auth.actor.AuthenticatedController;
import one.superstack.thingstack.enums.ActorType;
import one.superstack.thingstack.exception.NotAllowedException;
import one.superstack.thingstack.model.Group;
import one.superstack.thingstack.model.GroupMember;
import one.superstack.thingstack.model.User;
import one.superstack.thingstack.request.GroupCreationRequest;
import one.superstack.thingstack.request.GroupMemberRequest;
import one.superstack.thingstack.request.GroupUpdateRequest;
import one.superstack.thingstack.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class GroupController extends AuthenticatedController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping(value = "/groups")
    public Group create(@Valid @RequestBody GroupCreationRequest groupCreationRequest) {
        checkFullAccess();
        return groupService.create(groupCreationRequest, getActor());
    }

    @GetMapping(value = "/groups/all")
    public List<Group> listAll(Pageable pageable) {
        return groupService.listAll(getOrganizationId(), pageable);
    }

    @GetMapping(value = "/groups")
    public List<Group> list(Pageable pageable) {
        if (ActorType.USER.equals(getActorType())) {
            throw new NotAllowedException();
        }

        return groupService.list(getActorId(), pageable);
    }

    @GetMapping(value = "/groups/{groupId}")
    public Group get(@PathVariable String groupId) throws Throwable {
        return groupService.get(groupId, getOrganizationId());
    }

    @PutMapping(value = "/groups/{groupId}")
    public Group update(@PathVariable String groupId, @Valid @RequestBody GroupUpdateRequest groupUpdateRequest) throws Throwable {
        checkFullAccess();
        return groupService.update(groupId, groupUpdateRequest, getOrganizationId());
    }

    @DeleteMapping(value = "/groups/{groupId}")
    public Group delete(@PathVariable String groupId) throws Throwable {
        checkFullAccess();
        return groupService.delete(groupId, getOrganizationId());
    }

    @PostMapping(value = "/groups/{groupId}/members")
    public User addMember(@PathVariable String groupId, @Valid @RequestBody GroupMemberRequest groupMemberRequest) throws Throwable {
        checkFullAccess();
        return groupService.addMember(groupId, groupMemberRequest, getActor());
    }

    @DeleteMapping(value = "/groups/{groupId}/members")
    public GroupMember deleteMember(@PathVariable String groupId, @Valid @RequestBody GroupMemberRequest groupMemberRequest) throws Throwable {
        checkFullAccess();
        return groupService.deleteMember(groupId, groupMemberRequest, getOrganizationId());
    }

    @GetMapping(value = "/groups/{groupId}/members")
    public List<User> listMembers(@PathVariable String groupId, Pageable pageable) {
        return groupService.listMembers(groupId, getOrganizationId(), pageable);
    }

    @GetMapping(value = "/groups/search")
    public List<Group> search(@RequestParam String query, Pageable pageable) {
        return groupService.search(query, getOrganizationId(), pageable);
    }
}
