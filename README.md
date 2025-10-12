# guardrails4J

GuardRails4J is a Java library for building **adaptive query guardrails** for your application.  
It allows developers to block sensitive or competitor-related queries using a **layered guard system**: simple text-based, vector-based semantic matching, and LLM-powered guards.

---

## Features

- **Text-based guards**: Block queries with exact keywords or patterns.  
- **Vector-based semantic guards**: Compare user queries against a set of blocked phrases using vector embeddings.  
- **LLM-based competitor guard**: Use a language model to detect competitor comparison queries.  
- **Configurable guard order**: Specify which guards run first using `application.properties`.  
- **Adaptive learning**: Optionally, add blocked queries to the vector store at runtime for faster future blocking.  
- **Seamless integration**: Provides a web interceptor for Spring Boot applications to enforce guardrails transparently.  
- **Pluggable chat client**: Use any `ChatClient` implementation to connect your LLM; configure via properties.

---

## Installation

Add the library to your Maven or Gradle project:

```xml
<dependency>
    <groupId>com.github.phati</groupId>
    <artifactId>guardrails4j</artifactId>
    <version>2.0.5-SNAPSHOT</version>
</dependency>
```



```
guardrails.enabled=true
guard.competitors.competitor-list=HDFC,ICICI,AXIS
guard.competitors.our-company-name=JandhanBank
guard.competitors.default-response=Sorry, I am not able to assist with that request as it is against our competitor policy guidelines.

guard.competitors.simple-text-based.enabled=true
guard.competitors.simple-text-based.order=1

guard.competitors.llm-based.enabled=true
guard.competitors.llm-based.order=2
guard.competitors.llm-based.chat-client-bean-name=myChatClient
```

# ChatClient configuration(required for LLMBAsedGuardrails)
Define your LLM/chat client in your Spring Boot application:
```
@Bean
@Qualifier("myChatClient")
public ChatClient chatClient() {
    return new OpenAIChatClient("api-key");
}
```


# Web Interceptor Integration

Add the GuardRailInterceptor to your Spring Boot web configuration:
```
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private GuardRailInterceptor guardRailInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(guardRailInterceptor)
                .addPathPatterns("/api/**");
    }
}
```



