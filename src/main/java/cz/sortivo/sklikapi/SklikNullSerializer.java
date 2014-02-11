package cz.sortivo.sklikapi;

import org.apache.xmlrpc.serializer.NullSerializer;
import org.apache.xmlrpc.serializer.XmlRpcWriter;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class SklikNullSerializer extends NullSerializer {

    public void write(ContentHandler pHandler, Object pObject) throws SAXException {
        pHandler.startElement("", VALUE_TAG, VALUE_TAG, ZERO_ATTRIBUTES);
        pHandler.startElement(XmlRpcWriter.EXTENSIONS_URI, NIL_TAG, NIL_TAG, ZERO_ATTRIBUTES);
        pHandler.endElement(XmlRpcWriter.EXTENSIONS_URI, NIL_TAG, NIL_TAG);
        pHandler.endElement("", VALUE_TAG, VALUE_TAG);
    }
}
