package ua.shpp.model;

import lombok.Getter;

@Getter
public enum OrgRole {
    ADMIN(1),
    MANAGER(2);

    private final int accessLevel;

    OrgRole(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    /**
     * Checks if this role has enough privilege to perform an action requiring the given role.
     *
     * @param requiredAccessRole the minimum role required to perform the action
     * @return true if this role has sufficient access level, false otherwise
     */
    public boolean hasAccessLevelTo(OrgRole requiredAccessRole) {
        return this.accessLevel <= requiredAccessRole.accessLevel;
    }
}