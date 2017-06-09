Usage:
```xml
<bean id="resourceResolverFactory" 
      class="org.danilarefy.github.camel.validator.ResourceResolverFactory" />
...
<to uri="validator:path/to.xsd?resourceResolverFactory=#resourceResolverFactory" />
```