package cz.sortivo.sklikapi;

import org.apache.ws.commons.util.NamespaceContextImpl;
import org.apache.xmlrpc.common.TypeFactoryImpl;
import org.apache.xmlrpc.common.XmlRpcController;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.apache.xmlrpc.parser.NullParser;
import org.apache.xmlrpc.parser.TypeParser;
import org.apache.xmlrpc.serializer.NullSerializer;
import org.apache.xmlrpc.serializer.TypeSerializer;
import org.xml.sax.SAXException;
 
/**
 * This class extends Type Factory Impl to provide concrete type factory for handling
 * Null values. In this case the tag comparison uses NullSerializer.NIL_TAG as opposed
 * to EX_NIL_TAG
 *
 * @author zsugiart
 * http://www.zugiart.com/2011/07/apache-xml-rpc-handle-null-nil/
 *
 */
public class XmlRpcTypeNil extends TypeFactoryImpl {
 
    public XmlRpcTypeNil(XmlRpcController pController) {
            super(pController);
    }
 
    @Override
    public TypeParser getParser(XmlRpcStreamConfig pConfig, NamespaceContextImpl pContext, String pURI, String pLocalName) {
            if (NullSerializer.NIL_TAG.equals(pLocalName) || NullSerializer.EX_NIL_TAG.equals(pLocalName) )return new NullParser();
            else return super.getParser(pConfig, pContext, pURI, pLocalName);
    }
 
    @Override
    public TypeSerializer getSerializer(XmlRpcStreamConfig pConfig, Object pObject) throws SAXException {
            if (pObject == null) return new SklikNullSerializer();
            else return super.getSerializer(pConfig, pObject);
    }
}
