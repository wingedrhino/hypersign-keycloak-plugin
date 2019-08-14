We want to implement simple REST api in Keycloak. 

To do that we need to implement two interfaces : `RealmResourceProviderFactory` and `RealmResourceProvider`

## Step 1 

Implement `RealmResourceProviderFactory` interface. 

`HelloResourceProviderFactory.java` file

```java
public class HelloResourceProviderFactory implements RealmResourceProviderFactory {

  public static final String ID = "hello";

  @Override
  public String getId() {
      return ID;
  }

  @Override
  public RealmResourceProvider create(KeycloakSession session) {
      return new HelloResourceProvider(session);
  }

  @Override
  public void init(Scope config) {
  }

  @Override
  public void postInit(KeycloakSessionFactory factory) {
  }

  @Override
  public void close() {
  }

}


```

## Step 2

Implement `RealmResourceProvider` interface. 

`HelloResourceProvider.java` file

```java

public class HelloResourceProvider implements RealmResourceProvider {

    private KeycloakSession session;

    public HelloResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return this;
    }


    // Here you can implement APIs in get, post, put requests etc.
    @GET
    @Produces("text/plain; charset=utf-8")
    public String get() {
        String name = session.getContext().getRealm().getDisplayName();
        if (name == null) {
            name = session.getContext().getRealm().getName();
        }
        return "Hello " + name;
    }

    @Override
    public void close() {
    }

}

```

[Here](https://github.com/keycloak/keycloak/tree/master/examples/providers/rest/src/main/java/org/keycloak/examples/rest) is the full implementation.

## Deployement into Keycloak

Build

```
cd keycloak/examples/providers/rest/
mvn clean install
```

Copy 

```
cp keycloak/examples/providers/rest/target/hello-rest-example.jar /home/vishswasb/work/proj/hm/keycloak/keycloak-8.0.0-SNAPSHOT

```

Run this inside Keycloak's home directory to add module in keycloak

```
./bin/jboss-cli.sh --command="module add --name=org.keycloak.examples.hello-rest-example --resources=./hello-rest-example.jar --dependencies=org.keycloak.keycloak-core,org.keycloak.keycloak-server-spi,org.keycloak.keycloak-server-spi-private,javax.ws.rs.api"
```

And finally update `standalone.xml` file present in `keycloak-8.0.0-SNAPSHOT/standalone/configuration/` directory.

```
<provider>module:org.keycloak.examples.hello-rest-example</provider>
```

- add this into `providers` tags. 
- Re-run the keycloak server
- Test the api : `http://localhost:8080/auth/realms/master/hello`



