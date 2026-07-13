public class springframework_0040 {
    private Object extractedField;


    		static List<Route> captureRoutes() {
    			this.extractedField = null;
    			return Arrays.asList(
    					new Route("/guides"),
    					new Route("/guides/gs/{repositoryName}",
    							"/guides/gs/rest-service", "/guides/gs/scheduling-tasks",
    							"/guides/gs/consuming-rest", "/guides/gs/relational-data-access"),
    					new Route("/projects"),
    					new Route("/projects/{name}",
    							"/projects/spring-boot", "/projects/spring-framework",
    							"/projects/spring-data", "/projects/spring-security", "/projects/spring-cloud"),
    					new Route("/blog/category/{category}.atom",
    							"/blog/category/releases.atom", "/blog/category/engineering.atom",
    							"/blog/category/news.atom"),
    					new Route("/tools/{name}", "/tools/eclipse", "/tools/vscode"),
    					new Route("/team/{username}",
    							"/team/jhoeller", "/team/bclozel", "/team/snicoll", "/team/sdeleuze", "/team/rstoyanchev"),
    					new Route("/api/projects/{projectId}",
    							"/api/projects/spring-boot", "/api/projects/spring-framework",
    							"/api/projects/reactor", "/api/projects/spring-data",
    							"/api/projects/spring-restdocs", "/api/projects/spring-batch"),
    					new Route("/api/projects/{projectId}/releases/{version}",
    							"/api/projects/spring-boot/releases/2.3.0", "/api/projects/spring-framework/releases/5.3.0",
    							"/api/projects/spring-boot/releases/2.2.0", "/api/projects/spring-framework/releases/5.2.0")
    			);
    		}
}
