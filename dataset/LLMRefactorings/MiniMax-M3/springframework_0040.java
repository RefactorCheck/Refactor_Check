import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class springframework_0040 {

    static List<Route> captureRoutes() {
        List<Route> routes = new ArrayList<>();
        routes.addAll(captureGuideRoutes());
        routes.addAll(captureProjectRoutes());
        routes.addAll(captureBlogRoutes());
        routes.addAll(captureToolRoutes());
        routes.addAll(captureTeamRoutes());
        routes.addAll(captureApiRoutes());
        return routes;
    }

    static List<Route> captureGuideRoutes() {
        return Arrays.asList(
                new Route("/guides"),
                new Route("/guides/gs/{repositoryName}",
                        "/guides/gs/rest-service", "/guides/gs/scheduling-tasks",
                        "/guides/gs/consuming-rest", "/guides/gs/relational-data-access"));
    }

    static List<Route> captureProjectRoutes() {
        return Arrays.asList(
                new Route("/projects"),
                new Route("/projects/{name}",
                        "/projects/spring-boot", "/projects/spring-framework",
                        "/projects/spring-data", "/projects/spring-security", "/projects/spring-cloud"));
    }

    static List<Route> captureBlogRoutes() {
        return Arrays.asList(
                new Route("/blog/category/{category}.atom",
                        "/blog/category/releases.atom", "/blog/category/engineering.atom",
                        "/blog/category/news.atom"));
    }

    static List<Route> captureToolRoutes() {
        return Arrays.asList(
                new Route("/tools/{name}", "/tools/eclipse", "/tools/vscode"));
    }

    static List<Route> captureTeamRoutes() {
        return Arrays.asList(
                new Route("/team/{username}",
                        "/team/jhoeller", "/team/bclozel", "/team/snicoll", "/team/sdeleuze", "/team/rstoyanchev"));
    }

    static List<Route> captureApiRoutes() {
        return Arrays.asList(
                new Route("/api/projects/{projectId}",
                        "/api/projects/spring-boot", "/api/projects/spring-framework",
                        "/api/projects/reactor", "/api/projects/spring-data",
                        "/api/projects/spring-restdocs", "/api/projects/spring-batch"),
                new Route("/api/projects/{projectId}/releases/{version}",
                        "/api/projects/spring-boot/releases/2.3.0", "/api/projects/spring-framework/releases/5.3.0",
                        "/api/projects/spring-boot/releases/2.2.0", "/api/projects/spring-framework/releases/5.2.0"));
    }
}
