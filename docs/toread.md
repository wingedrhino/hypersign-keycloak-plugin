Developing RESTful Web Services with JAX-RS

https://docs.oracle.com/javaee/6/tutorial/doc/gilik.html

The JAX-RS API uses Java programming language annotations to simplify the development of RESTful web services. 

```java

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.MediaType;

// The Java class will be hosted at the URI path "/helloworld"
@Path("/helloworld")
public class HelloWorldResource {
    
    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
    @Produces("text/plain")
    public String getClichedMessage() {
        // Return some cliched textual content
        return "Hello World";
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    public String doPost2(FormURLEncodedProperties formData) {
        ...
    }
}

```