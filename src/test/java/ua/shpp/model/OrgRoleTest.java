package ua.shpp.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrgRoleTest {

    @ParameterizedTest
    @CsvSource({
            "ADMIN, ADMIN",
            "ADMIN, MANAGER",
            "MANAGER, MANAGER"
    })
    void positiveHasAccessLevelTo(OrgRole currentUserRole, OrgRole requiredAccessRole) {
        assertTrue(currentUserRole.hasAccessLevelTo(requiredAccessRole));
    }

    @ParameterizedTest
    @CsvSource({
            "MANAGER, ADMIN"
    })
    void negativeHasAccessLevelTo(OrgRole currentUserRole, OrgRole requiredAccessRole) {
        assertFalse(currentUserRole.hasAccessLevelTo(requiredAccessRole));
    }
}