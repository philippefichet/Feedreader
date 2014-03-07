package fr.feedreader.buisness;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import fr.feedreader.models.FeedItem;

public class FeedProxyHandler extends DefaultHandler {
	
	private FeedAbstract feedAbstractHandler = null;
	
	/**
     * Receive notification of the start of an element.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the start of
     * each element (such as allocating a new tree node or writing
     * output to a file).</p>
     *
     * @param uri The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     * @param attributes The attributes attached to the element.  If
     *        there are no attributes, it shall be an empty
     *        Attributes object.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
    public void startElement (String uri, String localName,
                              String qName, Attributes attributes)
        throws SAXException
    {
    	if (qName.equalsIgnoreCase("rss") && feedAbstractHandler == null) {
    		feedAbstractHandler = new FeedRss();
    	} else if (qName.equalsIgnoreCase("feed") && feedAbstractHandler == null) {
    		feedAbstractHandler = new FeedAtom();
    	}
    	
    	if (feedAbstractHandler != null) {
    		feedAbstractHandler.startElement(uri, localName, qName, attributes);
    	}
    }

    /**
     * Receive notification of character data inside an element.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method to take specific actions for each chunk of character data
     * (such as adding the data to a node or buffer, or printing it to
     * a file).</p>
     *
     * @param ch The characters.
     * @param start The start position in the character array.
     * @param length The number of characters to use from the
     *               character array.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#characters
     */
    public void characters (char ch[], int start, int length)
        throws SAXException
    {
    	if (feedAbstractHandler != null) {
    		feedAbstractHandler.characters(ch, start, length);
    	}
    }
    
    /**
     * Receive notification of the end of an element.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the end of
     * each element (such as finalising a tree node or writing
     * output to a file).</p>
     *
     * @param uri The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void endElement (String uri, String localName, String qName)
        throws SAXException
    {
    	if (feedAbstractHandler != null) {
    		feedAbstractHandler.endElement(uri, localName, qName);
    	}
    }
    
    
    public List<FeedItem> getFeedItems() {
    	if (feedAbstractHandler != null) {
    		return feedAbstractHandler.getFeedItems();
    	} else {
    		return new ArrayList<FeedItem>();
    	}
    }
    
    public String getType() {
    	if (feedAbstractHandler != null) {
    		return feedAbstractHandler.getType();
    	} else {
    		return null;
    	}
    }
}
