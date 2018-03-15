
== What you'll build

You'll build a microservice application that uses Netflix Eureka, Hystrix, Ribbon and Spring Cloud to provide client-side load balancing, service discovery and registry.
To achieve the goal, we will create three microservices.

a service registry (Eureka Server)
a REST service which registers itself at the registry (Eureka Client) and
a client application, which is consuming the REST service as a registry-aware client.

== Eureka Server
To implement a Eureka Server for using as service registry is as easy as: adding spring-cloud-starter-eureka-server to the dependencies, enable the Eureka Server in a @SpringBootApplication per annotate it with @EnableEurekaServer.

http://localhost:8090

== Write a Eureka Client as REST service

For a @SpringBootApplication to be discovery-aware, we have to include Spring Discovery Client, then we need to annotate a @Configuration with either @EnableDiscoveryClient or @EnableEurekaClient. The latter tells Spring Boot to use Spring Netflix Eureka for service discovery explicitly.

Our "server" service is called Say Hello-Service. It will return a random greeting (picked out of a static list of three) from an endpoint accessible at `/greeting`. We're going to run multiple instances of this application locally alongside a client service application.

== Access from a client service

The Hello-Client application will be what our user sees. It will make a call to the Hello-Service application to get a greeting and then send that to our user when the user visits the endpoint at `/hello`.
http://localhost:8093/hystrix/
http://localhost:8093/hystrix.stream

== Load balance across server instances

Now we can access `/hello` on the Hello-Service and see a friendly greeting:

== Trying it out
Run the Eureka Server using port 8090:

----
$ mvn spring-boot:run -Dserver.port=8090
----

Run the Hello-Service using port 8091/8092:

----
$ mvn spring-boot:run -Dserver.port=8091
$ mvn spring-boot:run -Dserver.port=8092
----

Run the Client application on port 8093:

----
$ mvn spring-boot:run -Dserver.port=8093
----

Access `http://localhost:8888/hello` and then watch the Hello-Service instances. You can see Ribbon's pings arriving every 15 seconds:

----
2016-03-09 21:13:22.115  INFO 90046 --- [nio-8090-exec-1] hello.SayHelloApplication                : Access /
2016-03-09 21:13:22.629  INFO 90046 --- [nio-8090-exec-3] hello.SayHelloApplication                : Access /
----

And your requests to the Hello-Service should result in calls to Say Hello being spread across the running instances in round-robin form:

----
2016-03-09 21:15:28.915  INFO 90046 --- [nio-8090-exec-7] hello.SayHelloApplication                : Access /greeting
----

Now shut down a Hello-Service server instance. Once Ribbon has pinged the down instance and considers it down, you should see requests begin to be balanced across the remaining instances.

== Summary

Congratulations! You've just developed a Spring application that performs client-side load balancing for calls to another application.


Source:
https://github.com/spring-guides/gs-client-side-load-balancing
Reference:
https://cloud.spring.io/spring-cloud-netflix/single/spring-cloud-netflix.html#_circuit_breaker_hystrix_dashboard