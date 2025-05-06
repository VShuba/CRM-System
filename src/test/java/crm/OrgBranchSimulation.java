package crm;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import static config.GatlingConfig.localProtocol;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class OrgBranchSimulation extends Simulation {

    ScenarioBuilder createOrgBranchScenario = scenario("Create Organization and Branch")
            .exec(
                    http("Sign in")
                            .post("/auth/sign-in")
                            .header("Content-type", "application/json")
                            .body(StringBody("{\"login\": \"test@gmail.com\", \"password\":\"my_1secret1_password\"}"))
                            .check(status().is(200))
                            .check(jsonPath("$.token").saveAs("jwtToken"))
            )
            .exec(
                    http("Create Organization")
                            .post("/api/organizations")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .header("Content-Type", "application/json")
                            .body(StringBody("{\"name\": \"Gatling Org\"}"))
                            .check(status().is(201))
                            .check(jsonPath("$.id").saveAs("organizationId")) // сохраняем id созданной организации
            )
            .exec(
                    http("Create Branch")
                            .post("/api/organizations/#{organizationId}/branches") // <-- подставляем orgId в путь
                            .header("Authorization", "Bearer #{jwtToken}")
                            .header("Content-Type", "application/json")
                            .body(StringBody("{\"name\": \"Gatling branch\"}")) // organizationId теперь не нужен в теле
                            .check(status().is(201))
                            .check(jsonPath("$.id").exists()) // опционально: проверить, что branch действительно создался
            );

    {
        setUp(
                createOrgBranchScenario.injectOpen(atOnceUsers(1))
        ).protocols(localProtocol);
    }

}
