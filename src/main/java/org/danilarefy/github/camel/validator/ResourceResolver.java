package org.danilarefy.github.camel.validator;

import org.apache.camel.CamelContext;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;

public class ResourceResolver implements LSResourceResolver {

    private final CamelContext camelContext;
    private final String resourceUri;

    ResourceResolver(CamelContext camelContext, String resourceUri) {
        this.camelContext = camelContext;
        this.resourceUri = resourceUri;
    }

    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        // systemId should be mandatory
        if (systemId == null) {
            throw new IllegalArgumentException(String.format("Resource: %s refers an invalid resource without SystemId."
                    + " Invalid resource has type: %s, namespaceURI: %s, publicId: %s, systemId: %s, baseURI: %s", resourceUri, type, namespaceURI, publicId, systemId, baseURI));
        }

        // calculate systemId and baseURI based on resourceUri

        try {

            URI uri = baseURI == null ? new URI(resourceUri) : new URI(baseURI);

            uri = uri.resolve(systemId);

            URL url = uri.isAbsolute() ? uri.toURL() : camelContext.getClassResolver().loadResourceAsURL((uri.toString()));

            baseURI = url.toString();
            systemId = url.getFile();

            return new DefaultLSInput(publicId, systemId, baseURI, camelContext.getClassResolver().loadResourceAsStream(url.toString()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private final class DefaultLSInput implements LSInput {

        private final String publicId;
        private final String systemId;
        private final String baseURI;
        private final InputStream in;

        private DefaultLSInput(String publicId, String systemId, String baseURI, InputStream inputStream) {
            this.publicId = publicId;
            this.systemId = systemId;
            this.baseURI = baseURI;
            this.in = inputStream;
        }

        @Override
        public Reader getCharacterStream() {
            return null;
        }

        @Override
        public void setCharacterStream(Reader reader) {
        }

        @Override
        public InputStream getByteStream() {
            return in;
        }

        @Override
        public void setByteStream(InputStream inputStream) {
        }

        @Override
        public String getStringData() {
            return null;
        }

        @Override
        public void setStringData(String stringData) {
        }

        @Override
        public String getSystemId() {
            return systemId;
        }

        @Override
        public void setSystemId(String systemId) {
        }

        @Override
        public String getPublicId() {
            return publicId;
        }

        @Override
        public void setPublicId(String publicId) {
        }

        @Override
        public String getBaseURI() {
            return baseURI;
        }

        @Override
        public void setBaseURI(String baseURI) {
        }

        @Override
        public String getEncoding() {
            return null;
        }

        @Override
        public void setEncoding(String encoding) {
        }

        @Override
        public boolean getCertifiedText() {
            return false;
        }

        @Override
        public void setCertifiedText(boolean certifiedText) {
        }

        @Override
        public String toString() {
            return "DefaultLSInput[" + systemId + "]";
        }
    }
}