package spring.cloud.course.apigateway

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProdConfiguration {

    @Bean
    fun gatewayRouter(builder: RouteLocatorBuilder): RouteLocator = builder
        .routes()
        .route { p ->
            p.path("/get")
                .filters { f ->
                    f.addRequestHeader("MyHeader", "MyValue")
                    f.addRequestParameter("Param", "MyValue")
                }
                .uri("http://httpbig.org:80")
        }
        .route { p -> p.path("/currency-exchange/**").uri("lb://currency-exchange") }
        .route { p -> p.path("/currency-converter/**").uri("lb://currency-converter") }
        .route { p ->
            p.path("/currency-converter-new/**")
                .filters{ f ->
                    f.rewritePath(
                        "/currency-converter-new/(?<segment>.*)",
                        "/currency-converter-feign/\${segment}"
                    )
                }
                .uri("lb://currency-converter")
        }
        .build()
}